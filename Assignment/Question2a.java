

import java.util.Arrays;

public class Question2a {


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
        System.out.println("Example 1: " + minRewards(new int[] { 1, 0, 2 })); 
        System.out.println("Example 2: " + minRewards(new int[] { 1, 2, 2 })); 

        System.out.println("All same ratings: " + minRewards(new int[] { 3, 3, 3, 3 })); 
        System.out.println("Strictly increasing: " + minRewards(new int[] { 1, 2, 3, 4, 5 })); 
        System.out.println("Strictly decreasing: " + minRewards(new int[] { 5, 4, 3, 2, 1 })); 
        System.out.println("Single employee: " + minRewards(new int[] { 1 })); 
        System.out.println("Random distribution: " + minRewards(new int[] { 1, 3, 2, 5, 4, 7, 6 })); 
    }
}
