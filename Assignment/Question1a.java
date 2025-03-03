

public class Question1a {

    public static int findMinMeasurements(int k, int n) {
        // Early exit for optimal cases when k is high
        if (k >= Math.ceil(Math.log(n) / Math.log(2))) {
            return (int) Math.ceil(Math.log(n) / Math.log(2));
        }

        // Space-optimized DP approach
        int[] prev = new int[n + 1]; // Previous row
        int[] curr = new int[n + 1]; // Current row

        // Base case: When k = 1, test sequentially (worst case)
        for (int i = 1; i <= n; i++) {
            prev[i] = i;
        }

        // DP table filling with space optimization
        for (int materials = 2; materials <= k; materials++) {
            for (int levels = 1; levels <= n; levels++) {
                curr[levels] = Integer.MAX_VALUE;

                // Binary search to optimize worst-case minimization
                int low = 1, high = levels;
                while (low <= high) {
                    int mid = (low + high) / 2;

                    int breaks = prev[mid - 1]; // If it breaks, check below
                    int doesNotBreak = curr[levels - mid]; // If it doesn't, check above

                    int worstCase = 1 + Math.max(breaks, doesNotBreak);
                    curr[levels] = Math.min(curr[levels], worstCase);

                    // Binary search adjustment
                    if (breaks > doesNotBreak) {
                        high = mid - 1;
                    } else {
                        low = mid + 1;
                    }
                }
            }
            // Move current row to previous for next iteration
            System.arraycopy(curr, 0, prev, 0, n + 1);
        }

        return prev[n];
    }

    public static void main(String[] args) {
        // Test cases
        System.out.println("k=1, n=2: " + findMinMeasurements(1, 2)); // Output: 2
        System.out.println("k=2, n=6: " + findMinMeasurements(2, 6)); // Output: 3
        System.out.println("k=3, n=14: " + findMinMeasurements(3, 14)); // Output: 4
        System.out.println("k=2, n=10: " + findMinMeasurements(2, 10)); // Output: 4
        System.out.println("k=5, n=5: " + findMinMeasurements(5, 5)); // Output: 3
        System.out.println("k=4, n=20: " + findMinMeasurements(4, 20)); // Output: 5

        // Edge cases
        System.out.println("k=1, n=1: " + findMinMeasurements(1, 1)); // Output: 1
        System.out.println("k=10, n=100: " + findMinMeasurements(10, 100)); // Large case
        System.out.println("k=2, n=100: " + findMinMeasurements(2, 100)); // Larger range
    }
}


///javac Question1a.java
///java Question1a
