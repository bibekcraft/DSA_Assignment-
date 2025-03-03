

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EnhancedWebCrawler {
    private final ConcurrentLinkedQueue<String> urlQueue = new ConcurrentLinkedQueue<>();
    private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
    private final Map<String, String> crawledData = new ConcurrentHashMap<>();
    private final ExecutorService executor;
    private final int maxDepth;
    private final AtomicInteger activeThreads = new AtomicInteger(0);
    private final int numThreads;
    
    public EnhancedWebCrawler(int numThreads, int maxDepth) {
        this.numThreads = numThreads;
        this.executor = Executors.newFixedThreadPool(numThreads);
        this.maxDepth = maxDepth;
    }

    public void startCrawling(List<String> seedUrls) {
        urlQueue.addAll(seedUrls);
        visitedUrls.addAll(seedUrls);
        for (String url : seedUrls) {
            executor.submit(new CrawlTask(url, 1));
        }
        while (activeThreads.get() > 0 || !urlQueue.isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        shutdown();
    }

    private void shutdown() {
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
            activeThreads.incrementAndGet();
            try {
                crawl();
            } finally {
                activeThreads.decrementAndGet();
            }
        }

        private void crawl() {
            try {
                Document doc = Jsoup.connect(url).timeout(10000).get();
                String title = doc.title();
                crawledData.put(url, title);
                System.out.println("Depth " + depth + " - Crawled: " + url + " - " + title);
                if (depth < maxDepth) {
                    doc.select("a[href]").forEach(link -> {
                        String newUrl = link.absUrl("href");
                        if (!newUrl.isEmpty() && newUrl.startsWith("http") && visitedUrls.add(newUrl)) {
                            urlQueue.offer(newUrl);
                            executor.submit(new CrawlTask(newUrl, depth + 1));
                        }
                    });
                }
            } catch (IOException e) {
                System.err.println("Error crawling " + url + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        EnhancedWebCrawler crawler = new EnhancedWebCrawler(10, 2);
        List<String> seedUrls = Arrays.asList(
            "https://www.example.com",
            "https://www.wikipedia.org"
        );
        crawler.startCrawling(seedUrls);
        Map<String, String> results = crawler.getCrawledData();
        System.out.println("\nCrawling completed. Total pages: " + results.size());
        results.forEach((url, title) -> 
            System.out.println("URL: " + url + "\nTitle: " + title + "\n"));
    }
}