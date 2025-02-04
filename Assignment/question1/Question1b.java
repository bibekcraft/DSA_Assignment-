package question1;
import java.util.PriorityQueue; // Import PriorityQueue for min-heap implementation

/*
 * Problem Explanation:
 * We are given two sorted arrays of investment returns, `returns1` and `returns2`, and a target number `k`.
 * The goal is to find the kth lowest combined return that can be achieved by selecting one investment from each array.
 * The combined return is calculated as the product of the selected investments.
 *
 * Approach:
 * 1. Generate all possible combined returns by multiplying each element of `returns1` with each element of `returns2`.
 * 2. Store all combined returns in a min-heap (PriorityQueue) to keep them sorted in ascending order.
 * 3. Extract the kth smallest combined return by removing the smallest element from the min-heap `k` times.
 *
 * Why Use a Min-Heap?
 * - A min-heap allows us to efficiently retrieve the smallest element in O(1) time and remove it in O(log n) time.
 * - This ensures that we can find the kth smallest combined return in O(k log n) time, where n is the total number of combined returns.
 *
 * Time Complexity: O(n * m log(n * m)), where n and m are the lengths of `returns1` and `returns2`.
 * Space Complexity: O(n * m) for storing all combined returns in the min-heap.
 */

public class Question1b {

    // Method to find the kth lowest combined return
    public static int findKthLowestCombinedReturn(int[] returns1, int[] returns2, int k) {
        // Input validation: Check if arrays are valid and k is positive
        if (returns1 == null || returns2 == null || returns1.length == 0 || returns2.length == 0 || k <= 0) {
            throw new IllegalArgumentException("Invalid input.");
        }

        // Initialize a min-heap to store combined returns in ascending order
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        // Iterate through all combinations of returns1 and returns2
        for (int r1 : returns1) { // Loop through each element in returns1
            for (int r2 : returns2) { // Loop through each element in returns2
                int combinedReturn = r1 * r2; // Calculate the combined return (product)
                minHeap.offer(combinedReturn); // Add the combined return to the min-heap
            }
        }

        // Check if k is greater than the total number of combinations
        if (k > minHeap.size()) {
            throw new IllegalArgumentException("k is greater than the total number of combinations.");
        }

        // Extract the kth smallest combined return from the min-heap
        int result = 0;
        for (int i = 0; i < k; i++) {
            result = minHeap.poll(); // Remove and return the smallest element
        }

        return result; // Return the kth smallest combined return
    }

    // Main method to test the functionality
    public static void main(String[] args) {
        // Example 1
        int[] returns1 = {2, 5};
        int[] returns2 = {3, 4};
        int k1 = 2;
        System.out.println("Example 1 Output: " + findKthLowestCombinedReturn(returns1, returns2, k1)); // Expected: 8

        // Example 2
        int[] returns3 = {-4, -2, 0, 3};
        int[] returns4 = {2, 4};
        int k2 = 6;
        System.out.println("Example 2 Output: " + findKthLowestCombinedReturn(returns3, returns4, k2)); // Expected: 0
    }
}