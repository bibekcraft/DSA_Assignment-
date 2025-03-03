/**
 * Overview of Question2b.java:
 * This program finds the lexicographically smallest pair of points with the minimum Manhattan distance
 * in a set of 2D points provided as two arrays: x-coordinates (x_coords) and y-coordinates (y_coords).
 * Here's how it works step-by-step:
 * 
 * 1. Class Definition:
 *    - The code defines a public class `Question2b` with two methods: `findClosestPair` and `main`.
 * 
 * 2. findClosestPair Method:
 *    a. Input Validation:
 *       - Checks if the input arrays (x_coords and y_coords) are null, empty, or of unequal length.
 *       - Throws an IllegalArgumentException if any condition fails, ensuring robust input handling.
 *    
 *    b. Initialization:
 *       - Gets the number of points (n) from the length of x_coords.
 *       - Initializes minDistance to Integer.MAX_VALUE to track the smallest Manhattan distance found.
 *       - Creates a result array of size 2 to store the indices of the closest pair (i, j).
 * 
 *    c. Pairwise Comparison:
 *       - Uses nested loops to compare all unique pairs of points (i, j) where i < j:
 *         - Outer loop (i) runs from 0 to n-1.
 *         - Inner loop (j) runs from i+1 to n-1 to avoid self-comparison and duplicates (e.g., (i,j) vs (j,i)).
 * 
 *    d. Manhattan Distance Calculation:
 *       - For each pair (i, j), calculates the Manhattan distance as:
 *         distance = |x_coords[i] - x_coords[j]| + |y_coords[i] - y_coords[j]|
 *         - Uses Math.abs() to compute absolute differences in x and y coordinates.
 * 
 *    e. Finding the Minimum Distance:
 *       - If the current distance is less than minDistance:
 *         - Updates minDistance with the new smaller distance.
 *         - Stores the pair (i, j) in result[0] and result[1].
 *       - If the current distance equals minDistance:
 *         - Compares lexicographically: prefers smaller i, or if i is equal, smaller j.
 *         - Updates result if the current pair (i, j) is lexicographically smaller than the stored pair.
 * 
 *    f. Return Value:
 *       - Returns the result array containing the indices of the lexicographically smallest pair with
 *         the minimum Manhattan distance.
 * 
 * 3. main Method:
 *    a. Test Case Setup:
 *       - Defines an example input with x_coords = {1, 2, 3, 2, 4} and y_coords = {2, 3, 1, 2, 3}.
 *       - Calls findClosestPair to compute the closest pair.
 *       - Prints the result as "[i, j]" (e.g., [0, 3] for points (1,2) and (2,2) with distance 1).
 * 
 *    b. Additional Test Cases:
 *       - Runs four additional test cases with different point sets:
 *         - Test Case 1: Points along a diagonal (distance 2).
 *         - Test Case 2: Scattered points (distance 2).
 *         - Test Case 3: Points with ties (distance 1).
 *         - Test Case 4: All points identical (distance 0).
 *       - Prints each result as an array using Arrays.toString().
 * 
 * Time Complexity: O(n²) due to comparing all pairs, where n is the number of points.
 * Space Complexity: O(1) excluding input arrays, as only a few variables are used.
 */

public class Question2b {

    /**
     * Finds the lexicographically smallest pair of points with the minimum
     * Manhattan distance.
     *
     * @param x_coords An array representing the x-coordinates of points.
     * @param y_coords An array representing the y-coordinates of points.
     * @return An array containing the indices of the closest pair of points.
     */
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        // Input validation: Ensure arrays are non-null and have the same length
        if (x_coords == null || y_coords == null || x_coords.length == 0 || y_coords.length == 0
                || x_coords.length != y_coords.length) {
            throw new IllegalArgumentException("Invalid input: Arrays must be non-empty and of equal length.");
        }

        int n = x_coords.length;
        int minDistance = Integer.MAX_VALUE; // Initialize the minimum distance
        int[] result = new int[2]; // Store the best pair (i, j)

        // Iterate through all unique pairs of points (i, j)
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) { // Ensures i < j to avoid redundant comparisons
                // Compute Manhattan distance
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // Update the minimum distance and store the best lexicographic pair
                if (distance < minDistance) {
                    minDistance = distance;
                    result[0] = i;
                    result[1] = j;
                }
                // If the distance is the same, check lexicographic order
                else if (distance == minDistance) {
                    if (i < result[0] || (i == result[0] && j < result[1])) {
                        result[0] = i;
                        result[1] = j;
                    }
                }
            }
        }

        return result; // Return the lexicographically smallest closest pair
    }

    public static void main(String[] args) {
        // Example input
        int[] x_coords = { 1, 2, 3, 2, 4 };
        int[] y_coords = { 2, 3, 1, 2, 3 };

        // Find the closest pair
        int[] result = findClosestPair(x_coords, y_coords);

        // Print the result
        System.out.println("Output: [" + result[0] + ", " + result[1] + "]"); // Expected: [0, 3]

        // Additional test cases
        System.out.println("Test Case 1: " + java.util.Arrays.toString(findClosestPair(
                new int[] { 0, 1, 2, 3 }, new int[] { 0, 1, 2, 3 }))); // Output: [0, 1]

        System.out.println("Test Case 2: " + java.util.Arrays.toString(findClosestPair(
                new int[] { 3, 1, 4, 1 }, new int[] { 5, 9, 2, 6 }))); // Output: [1, 3]

        System.out.println("Test Case 3: " + java.util.Arrays.toString(findClosestPair(
                new int[] { 1, 2, 1, 2 }, new int[] { 1, 1, 2, 2 }))); // Output: [0, 2]

        System.out.println("Test Case 4: " + java.util.Arrays.toString(findClosestPair(
                new int[] { 5, 5, 5, 5 }, new int[] { 5, 5, 5, 5 }))); // Output: [0, 1]
    }
}

//////java Question2b