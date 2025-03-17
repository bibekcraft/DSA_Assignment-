

import java.util.PriorityQueue;



public class Question1b {

    static class Pair {
        int i, j, product;

        Pair(int i, int j, int product) {
            this.i = i;
            this.j = j;
            this.product = product;
        }
    }

    public static int findKthSmallestProduct(int[] returns1, int[] returns2, int k) {
        PriorityQueue<Pair> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.product, b.product));

        // Insert first k possible pairs (only the first element from returns2)
        for (int i = 0; i < Math.min(returns1.length, k); i++) {
            minHeap.offer(new Pair(i, 0, returns1[i] * returns2[0]));
        }

        // Extract the kth smallest element
        int count = 0;
        while (!minHeap.isEmpty()) {
            Pair curr = minHeap.poll();
            count++;

            if (count == k)
                return curr.product;

            // Move to the next element in returns2 for the same index in returns1
            int nextJ = curr.j + 1;
            if (nextJ < returns2.length) {
                minHeap.offer(new Pair(curr.i, nextJ, returns1[curr.i] * returns2[nextJ]));
            }
        }
        return -1; // This case won't happen as k is always valid
    }

    public static void main(String[] args) {
        System.out.println("returns1=[2,5], returns2=[3,4], k=2 -> "
                + findKthSmallestProduct(new int[] { 2, 5 }, new int[] { 3, 4 }, 2)); 
        System.out.println("returns1=[-4,-2,0,3], returns2=[2,4], k=6 -> "
                + findKthSmallestProduct(new int[] { -4, -2, 0, 3 }, new int[] { 2, 4 }, 6)); 

        System.out.println("returns1=[1,2,3], returns2=[1,2,3], k=4 -> "
                + findKthSmallestProduct(new int[] { 1, 2, 3 }, new int[] { 1, 2, 3 }, 4)); 
        System.out.println("returns1=[-3,-2,-1], returns2=[-2,-1], k=3 -> "
                + findKthSmallestProduct(new int[] { -3, -2, -1 }, new int[] { -2, -1 }, 3));
        System.out.println(
                "returns1=[1], returns2=[1], k=1 -> " + findKthSmallestProduct(new int[] { 1 }, new int[] { 1 }, 1));
        System.out.println("returns1=[-5,1], returns2=[-2,3], k=3 -> "
                + findKthSmallestProduct(new int[] { -5, 1 }, new int[] { -2, 3 }, 3)); 
    }
}

///javac Question1b.java
///java Question1a