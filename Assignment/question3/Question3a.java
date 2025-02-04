package question3;
/*
 * Problem Explanation:
 * We are given two arrays, `x_coords` and `y_coords`, representing the coordinates of points in a 2D plane.
 * The goal is to find the lexicographically smallest pair of indices (i, j) such that the Manhattan distance
 * between the points (x_coords[i], y_coords[i]) and (x_coords[j], y_coords[j]) is minimized.
 *
 * Manhattan Distance Formula:
 * |x_coords[i] - x_coords[j]| + |y_coords[i] - y_coords[j]|
 *
 * Lexicographical Order:
 * A pair (i1, j1) is lexicographically smaller than (i2, j2) if:
 * - i1 < i2, or
 * - i1 == i2 and j1 < j2.
 *
 * Approach:
 * 1. Iterate through all possible pairs of points (i, j) where i and j are indices of the two arrays.
 * 2. Calculate the Manhattan distance for each pair.
 * 3. Track the pair with the smallest distance. If multiple pairs have the same distance, choose the lexicographically smallest one.
 *
 * Time Complexity: O(n^2), where n is the number of points.
 * Space Complexity: O(1), as we only store the result and a few variables.
 */

 public class Question3a {

    // Method to find the lexicographically smallest pair of closest points
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        // Input validation: Check if the arrays are valid and have the same length
        if (x_coords == null || y_coords == null || x_coords.length == 0 || y_coords.length == 0 || x_coords.length != y_coords.length) {
            throw new IllegalArgumentException("Invalid input.");
        }

        int n = x_coords.length;
        int minDistance = Integer.MAX_VALUE; // Initialize the minimum distance to a large value
        int[] result = new int[2]; // Store the result pair (i, j)

        // Iterate through all possible pairs of points
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // Calculate the Manhattan distance
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // Check if the current distance is smaller than the minimum distance
                if (distance < minDistance) {
                    minDistance = distance; // Update the minimum distance
                    result[0] = i; // Update the result pair
                    result[1] = j;
                }
                // If the distance is equal to the minimum distance, choose the lexicographically smaller pair
                else if (distance == minDistance) {
                    if (i < result[0] || (i == result[0] && j < result[1])) {
                        result[0] = i;
                        result[1] = j;
                    }
                }
            }
        }

        return result; // Return the lexicographically smallest pair
    }

    // Main method to test the functionality
    public static void main(String[] args) {
        // Example input
        int[] x_coords = {1, 2, 3, 2, 4};
        int[] y_coords = {2, 3, 1, 2, 3};

        // Find the closest pair
        int[] result = findClosestPair(x_coords, y_coords);

        // Print the result
        System.out.println("Output: [" + result[0] + ", " + result[1] + "]"); // Expected: [0, 3]
    }
}