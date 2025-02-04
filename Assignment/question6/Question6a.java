package question6;

/*
 * This program uses multiple threads to print the sequence "0102030405..." up to a given number 'n'.
 * There are three threads:
 * - ZeroThread: Prints '0'
 * - EvenThread: Prints even numbers
 * - OddThread: Prints odd numbers
 * 
 * These threads must be synchronized to ensure the numbers are printed in the correct order:
 * "0", then even numbers, then odd numbers, interleaved.
 * 
 * The synchronization is handled using:
 * - A shared counter to track the number being printed
 * - A lock object for controlling access to the critical section
 * - wait() and notifyAll() to coordinate the printing order between the threads
 *
 * Each thread waits for its turn based on the counter value, prints the number, and then notifies the other threads to proceed.
 * This ensures that the numbers are printed in sequence: "0102030405..." for n = 5.
 */

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
        private int counter = 0; // This will track the current number
        private final Object lock = new Object(); // Lock object to synchronize threads

        public ThreadController(int n) {
            this.n = n;
        }

        // Method to control ZeroThread
        public void zeroThread(NumberPrinter numberPrinter) {
            while (counter < n) {
                synchronized (lock) {
                    if (counter % 2 == 0) { // Print 0 only when the counter is even
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
            while (counter < n) {
                synchronized (lock) {
                    if (counter % 2 == 1) { // Print even numbers when the counter is odd
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
            while (counter < n) {
                synchronized (lock) {
                    if (counter % 2 == 2) { // Print odd numbers when the counter is even
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
