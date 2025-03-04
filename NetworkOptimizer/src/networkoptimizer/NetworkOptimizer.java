package networkoptimizer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkOptimizer extends JFrame {
    public static List<Node> nodes = new ArrayList<>();
    public static List<Edge> edges = new ArrayList<>();
    private Map<Integer, List<Edge>> adjList = new HashMap<>(); // Adjacency list for faster lookups

    private GraphPanel graphPanel;
    private JTextArea resultArea;
    private JComboBox<String> metricComboBox;
    private JLabel statusLabel;
    private JSlider costSlider, latencySlider;
    private JCheckBox normalizeCheckBox;

    public NetworkOptimizer() {
        setTitle("Advanced Network Optimizer");
        setSize(1200, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 245));

        JPanel controlPanel = setupControlPanel();
        add(controlPanel, BorderLayout.WEST);

        graphPanel = new GraphPanel(this);
        add(graphPanel, BorderLayout.CENTER);

        resultArea = new JTextArea(10, 40);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        updateAdjacencyList();
    }

    private JPanel setupControlPanel() {
        JPanel controlPanel = new JPanel(new GridBagLayout());
        controlPanel.setBackground(Color.WHITE);
        controlPanel.setBorder(BorderFactory.createTitledBorder("Control Panel"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        statusLabel = new JLabel("Click to add nodes, click two nodes to add edge");
        controlPanel.add(statusLabel, gbc);

        gbc.gridy = 1; gbc.gridwidth = 1;
        controlPanel.add(new JLabel("Optimization Metric:"), gbc);
        gbc.gridx = 1;
        String[] metrics = {"Cost", "Bandwidth", "Latency", "Combined"};
        metricComboBox = new JComboBox<>(metrics);
        metricComboBox.setSelectedItem("Combined");
        metricComboBox.setToolTipText("Select metric for optimization");
        controlPanel.add(metricComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        controlPanel.add(new JLabel("Cost Weight:"), gbc);
        gbc.gridx = 1;
        costSlider = new JSlider(0, 100, 50);
        costSlider.setMajorTickSpacing(25);
        costSlider.setPaintTicks(true);
        costSlider.setPaintLabels(true);
        controlPanel.add(costSlider, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        controlPanel.add(new JLabel("Latency Weight:"), gbc);
        gbc.gridx = 1;
        latencySlider = new JSlider(0, 100, 50);
        latencySlider.setMajorTickSpacing(25);
        latencySlider.setPaintTicks(true);
        latencySlider.setPaintLabels(true);
        controlPanel.add(latencySlider, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        normalizeCheckBox = new JCheckBox("Normalize Metrics", true);
        normalizeCheckBox.setToolTipText("Normalize cost/latency for fair comparison");
        controlPanel.add(normalizeCheckBox, gbc);

        gbc.gridy = 5;
        JButton optimizeBtn = createStyledButton("Optimize Network", new Color(0, 120, 200));
        optimizeBtn.addActionListener(e -> optimizeNetwork());
        controlPanel.add(optimizeBtn, gbc);

        gbc.gridy = 6;
        JButton pathBtn = createStyledButton("Find Optimal Path", new Color(0, 120, 200));
        pathBtn.addActionListener(e -> findOptimalPath());
        controlPanel.add(pathBtn, gbc);

        gbc.gridy = 7;
        JButton analyzeBtn = createStyledButton("Network Analysis", new Color(150, 100, 0));
        analyzeBtn.addActionListener(e -> analyzeNetwork());
        controlPanel.add(analyzeBtn, gbc);

        gbc.gridy = 8;
        JButton saveBtn = createStyledButton("Save Network", new Color(0, 150, 0));
        saveBtn.addActionListener(e -> saveNetwork());
        controlPanel.add(saveBtn, gbc);

        gbc.gridy = 9;
        JButton loadBtn = createStyledButton("Load Network", new Color(0, 150, 0));
        loadBtn.addActionListener(e -> loadNetwork());
        controlPanel.add(loadBtn, gbc);

        gbc.gridy = 10;
        JButton clearBtn = createStyledButton("Clear Network", new Color(200, 50, 50));
        clearBtn.addActionListener(e -> clearNetwork());
        controlPanel.add(clearBtn, gbc);

        return controlPanel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setToolTipText(text);
        return button;
    }

    public void addNode(int x, int y) {
        String name = "Node" + nodes.size();
        nodes.add(new Node(nodes.size(), x, y, name));
        resultArea.append("Added " + name + " (" + x + "," + y + ")\n");
        updateAdjacencyList();
        graphPanel.repaint();
    }

    public void addEdge(int n1, int n2) {
        if (n1 == n2 || edges.stream().anyMatch(e -> (e.n1 == n1 && e.n2 == n2) || (e.n1 == n2 && e.n2 == n1))) {
            resultArea.append("Error: Duplicate edge or self-loop not allowed\n");
            return;
        }
        try {
            String costStr = JOptionPane.showInputDialog(this, "Enter cost for edge " + nodes.get(n1).name + "-" + nodes.get(n2).name + ":");
            int cost = Integer.parseInt(costStr);
            String bwStr = JOptionPane.showInputDialog(this, "Enter bandwidth (Mbps):");
            int bandwidth = Integer.parseInt(bwStr);
            String latStr = JOptionPane.showInputDialog(this, "Enter latency (ms):");
            int latency = Integer.parseInt(latStr);

            edges.add(new Edge(n1, n2, cost, bandwidth, latency));
            resultArea.append(String.format("Added Edge %s-%s: Cost=%d, BW=%d, Latency=%d%n",
                    nodes.get(n1).name, nodes.get(n2).name, cost, bandwidth, latency));
            updateAdjacencyList();
            graphPanel.repaint();
        } catch (NumberFormatException e) {
            resultArea.append("Error: Invalid input for edge properties (must be integers)\n");
        }
    }

    public void removeEdge(int n1, int n2) {
        edges.removeIf(e -> (e.n1 == n1 && e.n2 == n2) || (e.n1 == n2 && e.n2 == n1));
        resultArea.append("Removed edge between " + nodes.get(n1).name + " and " + nodes.get(n2).name + "\n");
        updateAdjacencyList();
        graphPanel.repaint();
    }

    private void updateAdjacencyList() {
        adjList.clear();
        for (Node node : nodes) {
            adjList.putIfAbsent(node.id, new ArrayList<>());
        }
        for (Edge e : edges) {
            adjList.get(e.n1).add(e);
            adjList.get(e.n2).add(new Edge(e.n2, e.n1, e.cost, e.bandwidth, e.latency)); // Bidirectional
        }
    }

    private void optimizeNetwork() {
        if (nodes.isEmpty() || edges.isEmpty()) {
            resultArea.append("Error: Network is empty\n");
            return;
        }
        String metric = (String) metricComboBox.getSelectedItem();
        double costWeight = costSlider.getValue() / 100.0;
        double latencyWeight = latencySlider.getValue() / 100.0;
        boolean normalize = normalizeCheckBox.isSelected();
        List<Edge> optimalEdges = optimizeByMetric(metric, costWeight, latencyWeight, normalize);
        double totalCost = calculateTotalMetric(optimalEdges, "Cost");
        double totalLatency = calculateTotalMetric(optimalEdges, "Latency");
        resultArea.append(String.format("%nOptimal Network (Cost=%.2f, Latency=%.2f):%n", totalCost, totalLatency));
        for (Edge e : optimalEdges) {
            resultArea.append(String.format("%s-%s: Cost=%d, BW=%d, Latency=%d%n",
                    nodes.get(e.n1).name, nodes.get(e.n2).name, e.cost, e.bandwidth, e.latency));
        }
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
            double costWeight = costSlider.getValue() / 100.0;
            double latencyWeight = latencySlider.getValue() / 100.0;
            boolean normalize = normalizeCheckBox.isSelected();
            List<Integer> path = findPathByMetric(source, target, metric, costWeight, latencyWeight, normalize);
            if (path.isEmpty()) {
                resultArea.append("No path found between " + source + " and " + target + "\n");
                return;
            }
            double pathCost = calculatePathMetric(path, "Cost");
            double pathLatency = calculatePathMetric(path, "Latency");
            resultArea.append(String.format("%nOptimal Path from %s to %s (Cost=%.2f, Latency=%.2f):%n",
                    nodes.get(source).name, nodes.get(target).name, pathCost, pathLatency));
            resultArea.append(pathToString(path) + "\n");
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
                .mapToInt(n -> adjList.getOrDefault(n.id, new ArrayList<>()).size())
                .max().orElse(0);
        resultArea.append("Max Node Degree: " + maxDegree + "\n");
    }

    private List<Edge> optimizeByMetric(String metric, double costWeight, double latencyWeight, boolean normalize) {
        double maxCost = edges.stream().mapToDouble(e -> e.cost).max().orElse(1);
        double maxLatency = edges.stream().mapToDouble(e -> e.latency).max().orElse(1);

        edges.sort(Comparator.comparingDouble(e -> {
            double score = 0;
            if (metric.equals("Combined")) {
                double normCost = normalize ? (e.cost / maxCost) : e.cost;
                double normLatency = normalize ? (e.latency / maxLatency) : e.latency;
                score = costWeight * normCost + latencyWeight * normLatency;
            } else {
                score = switch (metric) {
                    case "Cost" -> e.cost;
                    case "Bandwidth" -> -e.bandwidth; // Maximize bandwidth
                    case "Latency" -> e.latency;
                    default -> e.cost;
                };
            }
            return score;
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

    private List<Integer> findPathByMetric(int source, int target, String metric, double costWeight, double latencyWeight, boolean normalize) {
        int n = nodes.size();
        double[] dist = new double[n];
        int[] prev = new int[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prev, -1);
        dist[source] = 0;

        PriorityQueue<double[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[0]));
        pq.offer(new double[]{0, source});

        double maxCost = edges.stream().mapToDouble(e -> e.cost).max().orElse(1);
        double maxLatency = edges.stream().mapToDouble(e -> e.latency).max().orElse(1);

        while (!pq.isEmpty()) {
            int u = (int) pq.poll()[1];
            if (visited[u]) continue;
            visited[u] = true;
            if (u == target) break;

            for (Edge e : adjList.getOrDefault(u, new ArrayList<>())) {
                int v = e.n1 == u ? e.n2 : e.n1;
                double weight = switch (metric) {
                    case "Cost" -> e.cost;
                    case "Bandwidth" -> -e.bandwidth;
                    case "Latency" -> e.latency;
                    case "Combined" -> costWeight * (normalize ? e.cost / maxCost : e.cost) +
                            latencyWeight * (normalize ? e.latency / maxLatency : e.latency);
                    default -> e.cost;
                };
                if (!visited[v] && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    prev[v] = u;
                    pq.offer(new double[]{dist[v], v});
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

    private void saveNetwork() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileChooser.getSelectedFile()))) {
                oos.writeObject(nodes);
                oos.writeObject(edges);
                resultArea.append("Network saved to " + fileChooser.getSelectedFile().getName() + "\n");
            } catch (IOException e) {
                resultArea.append("Error saving network: " + e.getMessage() + "\n");
            }
        }
    }

    private void loadNetwork() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileChooser.getSelectedFile()))) {
                nodes = (List<Node>) ois.readObject();
                edges = (List<Edge>) ois.readObject();
                updateAdjacencyList();
                graphPanel.repaint();
                resultArea.append("Network loaded from " + fileChooser.getSelectedFile().getName() + "\n");
            } catch (IOException | ClassNotFoundException e) {
                resultArea.append("Error loading network: " + e.getMessage() + "\n");
            }
        }
    }

    private void clearNetwork() {
        nodes.clear();
        edges.clear();
        adjList.clear();
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