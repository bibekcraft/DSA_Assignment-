package question2;
/*
 * Problem Explanation:
We need to distribute rewards to employees based on their performance ratings, while ensuring:

    Every employee must receive at least one reward.
    An employee with a higher rating than their adjacent colleague must receive more rewards.

 Step-by-Step Approach

    Initialize rewards array
        Every employee should receive at least one reward, so we initialize an array with all values set to 1.

    Left-to-right pass
        If an employee has a higher rating than the previous employee, they should get more rewards than the previous one.
        Formula:
        rewards[i]=rewards[i−1]+1
        rewards[i]=rewards[i−1]+1
        Ensures increasing sequences are handled.

    Right-to-left pass
        If an employee has a higher rating than the next employee, they should get more rewards than them.
        Since we are already assigning rewards in the first pass, we take the maximum of the current reward and the required one.
        Formula:
        rewards[i]=max⁡(rewards[i],rewards[i+1]+1)
        rewards[i]=max(rewards[i],rewards[i+1]+1)
        Ensures decreasing sequences are handled.

    Sum up all the rewards to get the final answer.
 */

import java.util.Arrays;

public class Question2a {

    /**
     * Determines the minimum number of rewards needed for employees based on their
     * ratings.
     * 
     * @param ratings An array representing the performance ratings of employees.
     * @return The minimum number of rewards required.
     */
    public static int minRewards(int[] ratings) {
        if (ratings == null || ratings.length == 0) {
            throw new IllegalArgumentException("Invalid input: Ratings array cannot be null or empty.");
        }

        int n = ratings.length;
        int[] rewards = new int[n];

        // Step 1: Initialize rewards array with 1 for each employee
        Arrays.fill(rewards, 1);

        // Step 2: Left-to-right pass
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }

        // Step 3: Right-to-left pass
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }

        // Step 4: Sum up the rewards
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }

        return totalRewards;
    }

    public static void main(String[] args) {
        // Example Test Cases
        System.out.println("Example 1: " + minRewards(new int[] { 1, 0, 2 })); // Output: 5
        System.out.println("Example 2: " + minRewards(new int[] { 1, 2, 2 })); // Output: 4

        // Additional Edge Cases
        System.out.println("All same ratings: " + minRewards(new int[] { 3, 3, 3, 3 })); // Output: 4
        System.out.println("Strictly increasing: " + minRewards(new int[] { 1, 2, 3, 4, 5 })); // Output: 15
        System.out.println("Strictly decreasing: " + minRewards(new int[] { 5, 4, 3, 2, 1 })); // Output: 15
        System.out.println("Single employee: " + minRewards(new int[] { 1 })); // Output: 1
        System.out.println("Random distribution: " + minRewards(new int[] { 1, 3, 2, 5, 4, 7, 6 })); // Output: 13
    }
}
