package question6;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * Multithreaded Web Crawler.
 * This class creates a thread pool to crawl multiple web pages concurrently.
 */
public class Question6b {

    // A thread-safe queue to store URLs to be crawled
    private static final ConcurrentLinkedQueue<String> urlQueue = new ConcurrentLinkedQueue<>();

    // ExecutorService to manage the thread pool (with 10 threads for concurrent
    // crawling)
    private static final ExecutorService executor = Executors.newFixedThreadPool(10); // 10 threads

    public static void main(String[] args) {
        // Add initial URLs to be crawled
        urlQueue.add("https://example.com");
        urlQueue.add("https://example.org");

        // Submit tasks to the thread pool (10 tasks in this example)
        for (int i = 0; i < 10; i++) {
            executor.submit(new CrawlTask());
        }

        // Shut down the executor once all tasks are done
        executor.shutdown();
    }

    /**
     * CrawlTask is a Runnable task that fetches and processes a web page.
     */
    static class CrawlTask implements Runnable {

        @Override
        public void run() {
            // Loop until the queue is empty
            while (!urlQueue.isEmpty()) {
                String url = urlQueue.poll(); // Get a URL from the queue
                if (url != null) {
                    try {
                        // Fetch the web page asynchronously using Jsoup
                        Document doc = Jsoup.connect(url).get();

                        // Extract and process the data from the page (e.g., extract title)
                        String title = doc.title();
                        System.out.println("Title of " + url + ": " + title);

                        // Optionally, add new URLs to the queue for further crawling
                        doc.select("a[href]").forEach(link -> {
                            String newUrl = link.absUrl("href");
                            if (!newUrl.isEmpty() && !urlQueue.contains(newUrl)) {
                                urlQueue.add(newUrl); // Add new URLs to the queue
                            }
                        });

                    } catch (IOException e) {
                        // Handle any errors that occur during web page fetching
                        System.out.println("Error fetching URL: " + url);
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
