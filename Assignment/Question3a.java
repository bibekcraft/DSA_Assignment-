

import java.util.*;

/*
 * Problem Explanation:
We have n devices, and we can either:
Install a communication module on a device at cost modules[i]
 Connect two devices using direct connections at cost connections[j] = [device1, device2, cost]

  Steps to Solve:

    Model the problem as a graph
        Each device is a node.
        Each direct connection is an edge with weight cost.
        Each device can connect itself by installing a module.

    Use Minimum Spanning Tree (MST) + Virtual Node Approach
        Create a virtual node (super-node) and connect it to all devices, with an edge weight = modules[i] (cost to install a module).
        Add all given connections to the graph.
        Use Kruskal’s Algorithm (or Prim’s Algorithm) to find the Minimum Spanning Tree (MST).

    Return the MST Cost
        The MST guarantees the minimum cost to connect all devices, either directly or using modules.
 */
public class Question3a {

    // Disjoint Set Union (DSU) / Union-Find with Path Compression
    static class DSU {
        int[] parent, rank;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY)
                return false; // Already connected

            // Union by rank
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }

    public static int minCostToConnectDevices(int n, int[] modules, int[][] connections) {
        List<int[]> edges = new ArrayList<>();

        // Add edges for installing modules (connecting to a virtual node n)
        for (int i = 0; i < n; i++) {
            edges.add(new int[] { n, i, modules[i] }); // (virtual node, device i, cost)
        }

        // Add the given connections
        for (int[] conn : connections) {
            int device1 = conn[0] - 1; // Convert 1-based to 0-based
            int device2 = conn[1] - 1;
            int cost = conn[2];
            edges.add(new int[] { device1, device2, cost });
        }

        // Sort edges based on cost (for Kruskal's algorithm)
        edges.sort(Comparator.comparingInt(a -> a[2]));

        // Kruskal's algorithm to find MST
        DSU dsu = new DSU(n + 1); // n devices + 1 virtual node
        int totalCost = 0, edgesUsed = 0;

        for (int[] edge : edges) {
            if (dsu.union(edge[0], edge[1])) { // Connect the devices
                totalCost += edge[2];
                edgesUsed++;
                if (edgesUsed == n)
                    break; // MST is complete when we connect n devices
            }
        }

        return totalCost;
    }

    public static void main(String[] args) {
        // Example Test Case
        int n = 3;
        int[] modules = { 1, 2, 2 };
        int[][] connections = { { 1, 2, 1 }, { 2, 3, 1 } };

        // Output: 3
        System.out.println("Minimum Cost: " + minCostToConnectDevices(n, modules, connections));

        // Additional Test Cases
        System.out.println("Test Case 1: " + minCostToConnectDevices(4, new int[] { 4, 3, 2, 1 },
                new int[][] { { 1, 2, 3 }, { 2, 3, 2 }, { 3, 4, 1 } })); // Expected: 3
        System.out.println("Test Case 2: " + minCostToConnectDevices(4, new int[] { 3, 2, 1, 4 },
                new int[][] { { 1, 2, 2 }, { 2, 3, 2 }, { 3, 4, 2 } })); // Expected: 4
        System.out.println(
                "Test Case 3: " + minCostToConnectDevices(2, new int[] { 10, 5 }, new int[][] { { 1, 2, 20 } })); // Expected:
                                                                                                                  // 10
    }
}