import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Question6b {
    private final ConcurrentLinkedQueue<String> urlQueue = new ConcurrentLinkedQueue<>();
    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
    private final Map<String, String> crawledData = new ConcurrentHashMap<>();
    private final ExecutorService executor;
    private final int maxDepth;
    private final AtomicInteger activeThreads = new AtomicInteger(0);

    public Question6b(int numThreads, int maxDepth) {
        this.executor = Executors.newFixedThreadPool(numThreads);
        this.maxDepth = maxDepth;
    }

    public void startCrawling(List<String> seedUrls) {
        System.out.println("Starting the crawler with " + seedUrls.size() + " seed URLs...");

        urlQueue.addAll(seedUrls);
        visitedUrls.addAll(seedUrls);

        for (String url : seedUrls) {
            activeThreads.incrementAndGet();
            executor.submit(new CrawlTask(url, 1));
        }

        // Wait for all threads to finish processing
        while (activeThreads.get() > 0) {
            try {
                Thread.sleep(10000);  // Check status every second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        shutdown();
    }

    private void shutdown() {
        System.out.println("Crawling completed. Shutting down executor...");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    public Map<String, String> getCrawledData() {
        return Collections.unmodifiableMap(crawledData);
    }

    class CrawlTask implements Runnable {
        private final String url;
        private final int depth;

        public CrawlTask(String url, int depth) {
            this.url = url;
            this.depth = depth;
        }

        @Override
        public void run() {
            try {
                crawl();
            } finally {
                activeThreads.decrementAndGet();  // Decrement active thread count when done
            }
        }

        private void crawl() {
            try {
                Document doc = Jsoup.connect(url).timeout(15000).get();
                String title = doc.title();
                crawledData.put(url, title);

                System.out.println("Depth " + depth + " - Crawled: " + url + " - " + title);

                // Extract links only if max depth is not reached
                if (depth < maxDepth) {
                    for (Element link : doc.select("a[href]")) {
                        String newUrl = link.absUrl("href");

                        // Check if the URL is valid and hasn't been visited yet
                        if (!newUrl.isEmpty() && newUrl.startsWith("http") && visitedUrls.add(newUrl)) {
                            System.out.println("Adding new URL: " + newUrl);
                            urlQueue.offer(newUrl);
                            activeThreads.incrementAndGet();
                            executor.submit(new CrawlTask(newUrl, depth + 1));
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error crawling " + url + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of threads to use: ");
        int numThreads = scanner.nextInt();

        System.out.print("Enter maximum crawl depth: ");
        int maxDepth = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter URLs separated by spaces: ");
        String input = scanner.nextLine();
        List<String> seedUrls = Arrays.asList(input.split("\\s+"));

        scanner.close();

        Question6b crawler = new Question6b(numThreads, maxDepth);
        crawler.startCrawling(seedUrls);

        Map<String, String> results = crawler.getCrawledData();
        System.out.println("\nCrawling completed. Total pages crawled: " + results.size());

        results.forEach((url, title) -> 
            System.out.println("URL: " + url + "\nTitle: " + title + "\n"));
    }
}
