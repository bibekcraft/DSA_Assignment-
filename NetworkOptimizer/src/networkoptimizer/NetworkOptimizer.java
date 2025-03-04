package networkoptimizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class NetworkOptimizer extends JFrame {
    public static List<Node> nodes = new ArrayList<>();
    public static List<Edge> edges = new ArrayList<>();
    
    private GraphPanel graphPanel;
    private JTextArea resultArea;
    private JComboBox<String> metricComboBox;
    private JLabel statusLabel;

    public NetworkOptimizer() {
        setTitle("Click-Based Network Optimizer");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 245));

        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createTitledBorder("Control Panel"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        statusLabel = new JLabel("Click to add nodes, click two nodes to add edge");
        controlPanel.add(statusLabel, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        controlPanel.add(new JLabel("Optimization Metric:"), gbc);
        gbc.gridx = 1;
        String[] metrics = {"Cost", "Bandwidth", "Latency"};
        metricComboBox = new JComboBox<>(metrics);
        controlPanel.add(metricComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        JButton optimizeBtn = createStyledButton("Optimize Network", new Color(0, 120, 200));
        optimizeBtn.addActionListener(e -> optimizeNetwork());
        controlPanel.add(optimizeBtn, gbc);

        gbc.gridy = 3;
        JButton pathBtn = createStyledButton("Find Optimal Path", new Color(0, 120, 200));
        pathBtn.addActionListener(e -> findOptimalPath());
        controlPanel.add(pathBtn, gbc);

        gbc.gridy = 4;
        JButton analyzeBtn = createStyledButton("Network Analysis", new Color(150, 100, 0));
        analyzeBtn.addActionListener(e -> analyzeNetwork());
        controlPanel.add(analyzeBtn, gbc);

        gbc.gridy = 5;
        JButton clearBtn = createStyledButton("Clear Network", new Color(200, 50, 50));
        clearBtn.addActionListener(e -> clearNetwork());
        controlPanel.add(clearBtn, gbc);

        add(controlPanel, BorderLayout.WEST);

        graphPanel = new GraphPanel(this);
        add(graphPanel, BorderLayout.CENTER);

        resultArea = new JTextArea(10, 40);
        resultArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        add(new JScrollPane(resultArea), BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    public void addNode(int x, int y) {
        String name = "Node" + nodes.size();
        nodes.add(new Node(nodes.size(), x, y, name));
        resultArea.append("Added " + name + " (" + x + "," + y + ")\n");
    }

    public void addEdge(int n1, int n2) {
        // Default values for demonstration
        int cost = 10;
        int bandwidth = 100;
        int latency = 5;
        edges.add(new Edge(n1, n2, cost, bandwidth, latency));
        resultArea.append(String.format("Added Edge %s-%s: Cost=%d, BW=%d, Latency=%d%n",
            nodes.get(n1).name, nodes.get(n2).name, cost, bandwidth, latency));
        graphPanel.repaint();
    }

    private void optimizeNetwork() {
        if (nodes.isEmpty() || edges.isEmpty()) {
            resultArea.append("Error: Network is empty\n");
            return;
        }
        String metric = (String) metricComboBox.getSelectedItem();
        List<Edge> optimalEdges = optimizeByMetric(metric);
        double totalMetric = calculateTotalMetric(optimalEdges, metric);
        resultArea.append(String.format("%nOptimal Network (%s-based MST):%n", metric));
        for (Edge e : optimalEdges) {
            resultArea.append(String.format("%s-%s: Cost=%d, BW=%d, Latency=%d%n",
                nodes.get(e.n1).name, nodes.get(e.n2).name, e.cost, e.bandwidth, e.latency));
        }
        resultArea.append(String.format("Total %s: %.2f%n", metric, totalMetric));
        graphPanel.setOptimalEdges(optimalEdges);
        graphPanel.resetFirstNode();
    }

    private void findOptimalPath() {
        try {
            String input = JOptionPane.showInputDialog(this, "Enter source,target nodes (e.g., 0,1):");
            if (input == null) return;
            String[] nodesInput = input.split(",");
            int source = Integer.parseInt(nodesInput[0].trim());
            int target = Integer.parseInt(nodesInput[1].trim());
            if (source >= nodes.size() || target >= nodes.size() || source < 0 || target < 0)
                throw new Exception("Node index out of range");
            String metric = (String) metricComboBox.getSelectedItem();
            List<Integer> path = findPathByMetric(source, target, metric);
            if (path.isEmpty()) {
                resultArea.append("No path found between " + source + " and " + target + "\n");
                return;
            }
            double pathCost = calculatePathMetric(path, metric);
            resultArea.append(String.format("%nOptimal Path (%s) from %s to %s:%n",
                metric, nodes.get(source).name, nodes.get(target).name));
            resultArea.append(pathToString(path) + String.format(" (%.2f)%n", pathCost));
            graphPanel.setPath(path);
            graphPanel.resetFirstNode();
        } catch (Exception e) {
            resultArea.append("Error finding path: " + e.getMessage() + "\n");
        }
    }

    private void analyzeNetwork() {
        if (nodes.isEmpty()) {
            resultArea.append("Network is empty\n");
            return;
        }
        resultArea.append("\nNetwork Analysis:\n");
        resultArea.append("Nodes: " + nodes.size() + "\n");
        resultArea.append("Edges: " + edges.size() + "\n");
        
        if (!edges.isEmpty()) {
            double avgCost = edges.stream().mapToInt(e -> e.cost).average().orElse(0);
            double avgBw = edges.stream().mapToInt(e -> e.bandwidth).average().orElse(0);
            double avgLatency = edges.stream().mapToInt(e -> e.latency).average().orElse(0);
            
            resultArea.append(String.format("Avg Cost: %.2f%n", avgCost));
            resultArea.append(String.format("Avg Bandwidth: %.2f%n", avgBw));
            resultArea.append(String.format("Avg Latency: %.2f%n", avgLatency));
        }
        
        int maxDegree = nodes.stream()
            .mapToInt(n -> (int) edges.stream().filter(e -> e.n1 == n.id || e.n2 == n.id).count())
            .max().orElse(0);
        resultArea.append("Max Node Degree: " + maxDegree + "\n");
    }

    private List<Edge> optimizeByMetric(String metric) {
        edges.sort(Comparator.comparingDouble(e -> switch (metric) {
            case "Cost" -> e.cost;
            case "Bandwidth" -> -e.bandwidth;
            case "Latency" -> e.latency;
            default -> e.cost;
        }));
        UnionFind uf = new UnionFind(nodes.size());
        List<Edge> result = new ArrayList<>();
        for (Edge e : edges) {
            if (!uf.connected(e.n1, e.n2)) {
                uf.union(e.n1, e.n2);
                result.add(e);
            }
        }
        return result;
    }

    private List<Integer> findPathByMetric(int source, int target, String metric) {
        int n = nodes.size();
        double[] dist = new double[n];
        int[] prev = new int[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);
        dist[source] = 0;

        PriorityQueue<double[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[0]));
        pq.offer(new double[]{0, source});

        while (!pq.isEmpty()) {
            int u = (int) pq.poll()[1];
            if (visited[u]) continue;
            visited[u] = true;
            if (u == target) break;

            for (Edge e : edges) {
                int v = (e.n1 == u) ? e.n2 : (e.n2 == u) ? e.n1 : -1;
                if (v == -1) continue;
                double weight = switch (metric) {
                    case "Cost" -> e.cost;
                    case "Bandwidth" -> 1.0 / e.bandwidth;
                    case "Latency" -> e.latency;
                    default -> e.cost;
                };
                if (!visited[v] && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    prev[v] = u;
                    pq.offer(new double[]{dist[v], v]);
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        for (int at = target; at != -1; at = prev[at]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path.get(0) == source ? path : new ArrayList<>();
    }

    private double calculateTotalMetric(List<Edge> edges, String metric) {
        return edges.stream().mapToDouble(e -> switch (metric) {
            case "Cost" -> e.cost;
            case "Bandwidth" -> e.bandwidth;
            case "Latency" -> e.latency;
            default -> e.cost;
        }).sum();
    }

    private double calculatePathMetric(List<Integer> path, String metric) {
        double total = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            int n1 = path.get(i);
            int n2 = path.get(i + 1);
            total += edges.stream()
                .filter(e -> (e.n1 == n1 && e.n2 == n2) || (e.n1 == n2 && e.n2 == n1))
                .findFirst()
                .map(e -> switch (metric) {
                    case "Cost" -> (double) e.cost;
                    case "Bandwidth" -> (double) e.bandwidth;
                    case "Latency" -> (double) e.latency;
                    default -> (double) e.cost;
                }).orElse(0.0);
        }
        return total;
    }

    private String pathToString(List<Integer> path) {
        return path.stream().map(i -> nodes.get(i).name).collect(Collectors.joining(" -> "));
    }

    private void clearNetwork() {
        nodes.clear();
        edges.clear();
        graphPanel.clear();
        resultArea.setText("Network cleared.\n");
        updateStatus("Click to add nodes, click two nodes to add edge");
    }

    public void updateStatus(String message) {
        statusLabel.setText(message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NetworkOptimizer optimizer = new NetworkOptimizer();
            optimizer.setVisible(true);
        });
    }
}