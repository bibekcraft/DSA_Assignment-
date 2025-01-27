public class Question1 {

    public static int findMinMeasurements(int k, int n) {
        // DP table to store the minimum number of measurements
        int[][] dp = new int[k + 1][n + 1];

        // Base case: If we have 1 sample, test every temperature level
        for (int i = 1; i <= n; i++) {
            dp[1][i] = i;
        }

        // Fill the DP table
        for (int materials = 2; materials <= k; materials++) {
            for (int levels = 1; levels <= n; levels++) {
                dp[materials][levels] = Integer.MAX_VALUE;

                // Optimize: Binary search to find the minimum for this case
                int low = 1, high = levels;
                while (low <= high) {
                    int mid = (low + high) / 2;

                    // Two scenarios:
                    // 1. The material reacts at mid -> we check below (dp[materials - 1][mid - 1])
                    // 2. The material does not react -> we check above (dp[materials][levels -
                    // mid])
                    int breaks = dp[materials - 1][mid - 1];
                    int doesNotBreak = dp[materials][levels - mid];

                    int worstCase = 1 + Math.max(breaks, doesNotBreak);

                    // Take the minimum of the worst cases
                    dp[materials][levels] = Math.min(dp[materials][levels], worstCase);

                    // Adjust binary search
                    if (breaks > doesNotBreak) {
                        high = mid - 1;
                    } else {
                        low = mid + 1;
                    }
                }
            }
        }

        return dp[k][n];
    }

    public static void main(String[] args) {
        // Example 1: Small number of materials and levels
        System.out.println("k=1, n=2: " + findMinMeasurements(1, 2)); // Output: 2

        // Example 2: Moderate number of materials and levels
        System.out.println("k=2, n=6: " + findMinMeasurements(2, 6)); // Output: 3

        // Example 3: Larger number of materials and levels
        System.out.println("k=3, n=14: " + findMinMeasurements(3, 14)); // Output: 4

        // Additional examples:
        // Example 4: Edge case with more levels
        System.out.println("k=2, n=10: " + findMinMeasurements(2, 10)); // Output: 4

        // Example 5: More materials than levels
        System.out.println("k=5, n=5: " + findMinMeasurements(5, 5)); // Output: 3

        // Example 6: Large materials and levels
        System.out.println("k=4, n=20: " + findMinMeasurements(4, 20)); // Output: 5
    }
}
