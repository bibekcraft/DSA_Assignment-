package networkoptimizer;

public class Edge {
    int n1, n2, cost, bandwidth;
    Edge(int n1, int n2, int cost, int bandwidth) {
        this.n1 = n1;
        this.n2 = n2;
        this.cost = cost;
        this.bandwidth = bandwidth;
    }
}