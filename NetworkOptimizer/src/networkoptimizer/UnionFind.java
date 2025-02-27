package networkoptimizer;

public class UnionFind {
    int[] parent, rank;
    UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }
    int find(int u) {
        if (parent[u] != u) parent[u] = find(parent[u]);
        return parent[u];
    }
    void union(int u, int v) {
        int pu = find(u), pv = find(v);
        if (pu == pv) return;
        if (rank[pu] < rank[pv]) parent[pu] = pv;
        else if (rank[pu] > rank[pv]) parent[pv] = pu;
        else { parent[pv] = pu; rank[pu]++; }
    }
    boolean connected(int u, int v) {
        return find(u) == find(v);
    }
}