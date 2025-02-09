import java.util.*;

public class Question4b {

    /**
     * Method to determine the minimum number of roads to traverse to collect all
     * packages.
     * 
     * @param packages An array where each index represents a location, and the
     *                 value (0 or 1)
     *                 indicates whether there is a package to be collected.
     * @param roads    A 2D array where each entry [a, b] represents a bidirectional
     *                 road between
     *                 locations a and b.
     * @return The minimum number of roads that need to be traversed to collect all
     *         packages
     *         and return to the starting point.
     */
    public static int minRoadsToCollectAllPackages(int[] packages, int[][] roads) {
        int n = packages.length;

        // Step 1: Build an adjacency list representation of the graph
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        // Populate the adjacency list with roads
        for (int[] road : roads) {
            graph.get(road[0]).add(road[1]);
            graph.get(road[1]).add(road[0]);
        }

        // Step 2: Identify the locations (nodes) that have packages
        Set<Integer> importantNodes = new HashSet<>();
        for (int i = 0; i < n; i++) {
            if (packages[i] == 1) {
                importantNodes.add(i);
            }
        }

        // Step 3: Perform DFS to collect all packages while tracking the minimum road
        // traversal cost
        boolean[] visited = new boolean[n];
        int[] result = new int[1]; // This will store the final number of roads traversed
        dfs(0, -1, graph, importantNodes, visited, result);

        return result[0]; // Return the total roads used
    }

    /**
     * DFS function to traverse the graph and calculate the minimum traversal cost.
     * 
     * @param node           The current location in the graph.
     * @param parent         The previous location to prevent backtracking.
     * @param graph          The adjacency list representation of the city map.
     * @param importantNodes The set of locations that have packages to be
     *                       collected.
     * @param visited        Boolean array to keep track of visited locations.
     * @param result         Array with a single element to store the total number
     *                       of roads traversed.
     * @return True if this subtree contains a package, otherwise False.
     */
    private static boolean dfs(int node, int parent, List<List<Integer>> graph, Set<Integer> importantNodes,
            boolean[] visited, int[] result) {
        visited[node] = true;

        // Check if the current node or any of its descendants have a package
        boolean hasPackageOrSubtree = importantNodes.contains(node);

        // Explore neighboring nodes
        for (int neighbor : graph.get(node)) {
            if (neighbor == parent)
                continue; // Avoid going back to the parent node

            // If the subtree contains a package, update the traversal cost
            if (dfs(neighbor, node, graph, importantNodes, visited, result)) {
                result[0] += 2; // Each valid traversal counts twice (going and returning)
                hasPackageOrSubtree = true;
            }
        }

        return hasPackageOrSubtree; // Return whether this branch of the tree contains a package
    }

    // Main method for testing the function with example cases
    public static void main(String[] args) {
        // Example 1
        int[] packages1 = { 1, 0, 0, 0, 0, 1 };
        int[][] roads1 = { { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 } };
        System.out.println(minRoadsToCollectAllPackages(packages1, roads1)); // Expected Output: 2

        // Example 2
        int[] packages2 = { 0, 0, 0, 1, 1, 0, 0, 1 };
        int[][] roads2 = { { 0, 1 }, { 0, 2 }, { 1, 3 }, { 1, 4 }, { 2, 5 }, { 5, 6 }, { 5, 7 } };
        System.out.println(minRoadsToCollectAllPackages(packages2, roads2)); // Expected Output: 2
    }
}
