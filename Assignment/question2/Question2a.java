package question2;
/*
 * Problem Explanation:
 * We are given an array of employee performance ratings, and we need to assign rewards to each employee based on the following rules:
 * 1. Every employee must receive at least one reward.
 * 2. Employees with a higher rating must receive more rewards than their adjacent colleagues.
 *
 * Goal:
 * Determine the minimum number of rewards needed to distribute to the employees.
 *
 * Approach:
 * 1. Initialize an array `rewards` where each employee is given at least 1 reward.
 * 2. Traverse the ratings array from left to right:
 *    - If the current employee has a higher rating than the previous one, assign them one more reward than the previous employee.
 * 3. Traverse the ratings array from right to left:
 *    - If the current employee has a higher rating than the next one, ensure they receive at least one more reward than the next employee.
 * 4. Sum up all the rewards in the `rewards` array to get the minimum total number of rewards needed.
 *
 * Time Complexity: O(n), where n is the number of employees.
 * Space Complexity: O(n) for storing the rewards array.
 */

 public class Question2a {

    // Method to calculate the minimum number of rewards
    public static int minRewards(int[] ratings) {
        // Input validation: Check if the ratings array is valid
        if (ratings == null || ratings.length == 0) {
            throw new IllegalArgumentException("Invalid input.");
        }

        int n = ratings.length;
        int[] rewards = new int[n]; // Initialize the rewards array

        // Step 1: Give each employee at least 1 reward
        for (int i = 0; i < n; i++) {
            rewards[i] = 1;
        }

        // Step 2: Traverse from left to right
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1; // Assign one more reward than the previous employee
            }
        }

        // Step 3: Traverse from right to left
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1); // Ensure the current employee gets at least one more reward than the next employee
            }
        }

        // Step 4: Calculate the total number of rewards
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }

        return totalRewards; // Return the total number of rewards
    }

    // Main method to test the functionality
    public static void main(String[] args) {
        // Example 1
        int[] ratings1 = {1, 0, 2};
        System.out.println("Example 1 Output: " + minRewards(ratings1)); // Expected: 5

        // Example 2
        int[] ratings2 = {1, 2, 2};
        System.out.println("Example 2 Output: " + minRewards(ratings2)); // Expected: 4
    }
}