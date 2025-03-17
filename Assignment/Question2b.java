
public class Question2b {

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
        int[] x_coords = { 1, 2, 3, 2, 4 };
        int[] y_coords = { 2, 3, 1, 2, 3 };

        int[] result = findClosestPair(x_coords, y_coords);
        System.out.println("Output: [" + result[0] + ", " + result[1] + "]"); 

        
        System.out.println("Test Case 1: " + java.util.Arrays.toString(findClosestPair(
                new int[] { 0, 1, 2, 3 }, new int[] { 0, 1, 2, 3 }))); 

        System.out.println("Test Case 2: " + java.util.Arrays.toString(findClosestPair(
                new int[] { 3, 1, 4, 1 }, new int[] { 5, 9, 2, 6 }))); 

        System.out.println("Test Case 3: " + java.util.Arrays.toString(findClosestPair(
                new int[] { 1, 2, 1, 2 }, new int[] { 1, 1, 2, 2 }))); 

        System.out.println("Test Case 4: " + java.util.Arrays.toString(findClosestPair(
                new int[] { 5, 5, 5, 5 }, new int[] { 5, 5, 5, 5 }))); 
    }
}

