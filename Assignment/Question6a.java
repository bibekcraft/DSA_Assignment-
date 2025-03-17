
public class Question6a {

    public static class NumberPrinter {
        // Method to print 0
        public void printZero() {
            System.out.print("0");
        }

        // Method to print even numbers
        public void printEven(int num) {
            System.out.print(num);
        }

        // Method to print odd numbers
        public void printOdd(int num) {
            System.out.print(num);
        }
    }

    public static class ThreadController {
        private int n; // The upper limit for printing numbers
        private int counter = 0; // This will track the current number to be printed
        private final Object lock = new Object(); // Lock object to synchronize threads

        public ThreadController(int n) {
            this.n = n;
        }

        // Method to control ZeroThread
        public void zeroThread(NumberPrinter numberPrinter) {
            while (counter < n) {
                synchronized (lock) {
                    if (counter % 3 == 0) { // Print 0 only when the counter is divisible by 3
                        numberPrinter.printZero();
                        counter++;
                        lock.notifyAll(); // Notify other threads
                    } else {
                        try {
                            lock.wait(); // Wait for the turn
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        }

        // Method to control EvenThread
        public void evenThread(NumberPrinter numberPrinter) {
            while (counter <= n) {
                synchronized (lock) {
                    if (counter % 3 == 1) { // Print even numbers when the counter remainder is 1
                        numberPrinter.printEven(counter);
                        counter++;
                        lock.notifyAll(); // Notify other threads
                    } else {
                        try {
                            lock.wait(); // Wait for the turn
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        }

        // Method to control OddThread
        public void oddThread(NumberPrinter numberPrinter) {
            while (counter <= n) {
                synchronized (lock) {
                    if (counter % 3 == 2) { // Print odd numbers when the counter remainder is 2
                        numberPrinter.printOdd(counter);
                        counter++;
                        lock.notifyAll(); // Notify other threads
                    } else {
                        try {
                            lock.wait(); // Wait for the turn
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        }

        // Method to start the threads
        public void startThreads() {
            NumberPrinter numberPrinter = new NumberPrinter();

            // Create the three threads
            Thread threadZero = new Thread(() -> zeroThread(numberPrinter));
            Thread threadEven = new Thread(() -> evenThread(numberPrinter));
            Thread threadOdd = new Thread(() -> oddThread(numberPrinter));

            // Start the threads
            threadZero.start();
            threadEven.start();
            threadOdd.start();

            try {
                // Wait for all threads to finish
                threadZero.join();
                threadEven.join();
                threadOdd.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        int n = 5; // For example, print up to 5
        ThreadController controller = new ThreadController(n);
        controller.startThreads();
    }
}
