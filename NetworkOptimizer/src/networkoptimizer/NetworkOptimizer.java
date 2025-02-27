package networkoptimizer;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class NetworkOptimizer extends JFrame {
    public static List<Node> nodes = new ArrayList<>();
    public static List<Edge> edges = new ArrayList<>();
    
    private GraphPanel graphPanel;
    private JTextField nodeField, edgeField;
    private JTextArea resultArea;

    public NetworkOptimizer() {
        setTitle("Network Topology Optimizer");
        setSize(900, 700); // Slightly larger for better visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Add padding
        getContentPane().setBackground(new Color(240, 240, 245)); // Light gray background

        // Control Panel with modern styling
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        controlPanel.setBackground(new Color(255, 255, 255)); // White background
        controlPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 200)), "Control Panel")); // Blue border
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding between components

        // Node Input
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        JLabel nodeLabel = new JLabel("Node (x, y):");
        nodeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        controlPanel.add(nodeLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        nodeField = new JTextField("100,100", 15);
        nodeField.setFont(new Font("Arial", Font.PLAIN, 12));
        controlPanel.add(nodeField, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        JButton addNodeBtn = createStyledButton("Add Node", new Color(50, 150, 50));
        addNodeBtn.addActionListener(e -> addNode());
        controlPanel.add(addNodeBtn, gbc);

        // Edge Input
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        JLabel edgeLabel = new JLabel("Edge (n1, n2, cost, bw):");
        edgeLabel.setFont(new Font("Arial", Font.BOLD, 12));
        controlPanel.add(edgeLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        edgeField = new JTextField("0,1,10,100", 15);
        edgeField.setFont(new Font("Arial", Font.PLAIN, 12));
        controlPanel.add(edgeField, gbc);

        gbc.gridx = 2; gbc.fill = GridBagConstraints.NONE;
        JButton addEdgeBtn = createStyledButton("Add Edge", new Color(50, 150, 50));
        addEdgeBtn.addActionListener(e -> addEdge());
        controlPanel.add(addEdgeBtn, gbc);

        // Optimize Button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        JButton optimizeBtn = createStyledButton("Optimize Network (MST)", new Color(0, 120, 200));
        optimizeBtn.addActionListener(e -> optimizeNetwork());
        controlPanel.add(optimizeBtn, gbc);

        // Shortest Path Button
        gbc.gridy = 3;
        JButton pathBtn = createStyledButton("Find Shortest Path", new Color(0, 120, 200));
        pathBtn.addActionListener(e -> findShortestPath());
        controlPanel.add(pathBtn, gbc);

        // Clear Button
        gbc.gridy = 4;
        JButton clearBtn = createStyledButton("Clear Network", new Color(200, 50, 50));
        clearBtn.addActionListener(e -> clearNetwork());
        controlPanel.add(clearBtn, gbc);

        add(controlPanel, BorderLayout.NORTH);

        // Graph Panel
        graphPanel = new GraphPanel();
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(graphPanel, BorderLayout.CENTER);

        // Result Area
        resultArea = new JTextArea(10, 30);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setBackground(new Color(245, 245, 250));
        resultArea.setBorder(BorderFactory.createTitledBorder("Results"));
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.SOUTH);
    }

    // Helper method to create styled buttons
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        return button;
    }

    private void addNode() {
        try {
            String[] coords = nodeField.getText().split(",");
            int x = Integer.parseInt(coords[0].trim());
            int y = Integer.parseInt(coords[1].trim());
            nodes.add(new Node(nodes.size(), x, y));
            graphPanel.repaint();
            resultArea.append("Added Node " + (nodes.size() - 1) + " at (" + x + "," + y + ")\n");
        } catch (Exception e) {
            resultArea.append("Error adding node: " + e.getMessage() + "\n");
        }
    }

    private void addEdge() {
        try {
            String[] data = edgeField.getText().split(",");
            int n1 = Integer.parseInt(data[0].trim());
            int n2 = Integer.parseInt(data[1].trim());
            int cost = Integer.parseInt(data[2].trim());
            int bandwidth = Integer.parseInt(data[3].trim());
            if (n1 >= nodes.size() || n2 >= nodes.size()) throw new Exception("Node index out of range");
            edges.add(new Edge(n1, n2, cost, bandwidth));
            graphPanel.repaint();
            resultArea.append("Added Edge " + n1 + " to " + n2 + ": Cost=" + cost + ", Bandwidth=" + bandwidth + "\n");
        } catch (Exception e) {
            resultArea.append("Error adding edge: " + e.getMessage() + "\n");
        }
    }

    private void optimizeNetwork() {
        List<Edge> mst = kruskalMST();
        int totalCost = mst.stream().mapToInt(e -> e.cost).sum();
        resultArea.append("\nOptimized Network (MST):\n");
        for (Edge e : mst) {
            resultArea.append("Edge " + e.n1 + " to " + e.n2 + ": Cost=" + e.cost + ", Bandwidth=" + e.bandwidth + "\n");
        }
        resultArea.append("Total Cost: " + totalCost + "\n");
        graphPanel.setMST(mst);
    }

    private void findShortestPath() {
        try {
            String input = JOptionPane.showInputDialog(this, "Enter source,target (e.g., 0,1):", "Shortest Path", JOptionPane.PLAIN_MESSAGE);
            String[] nodesInput = input.split(",");
            int source = Integer.parseInt(nodesInput[0].trim());
            int target = Integer.parseInt(nodesInput[1].trim());
            List<Integer> path = dijkstra(source, target);
            resultArea.append("\nShortest Path from " + source + " to " + target + ":\n");
            resultArea.append(path.toString() + "\n");
            graphPanel.setPath(path);
        } catch (Exception e) {
            resultArea.append("Error finding path: " + e.getMessage() + "\n");
        }
    }

    private void clearNetwork() {
        nodes.clear();
        edges.clear();
        graphPanel.clear();
        resultArea.setText("Network cleared.\n");
    }

    private List<Edge> kruskalMST() {
        List<Edge> mst = new ArrayList<>();
        edges.sort(Comparator.comparingInt(e -> e.cost));
        UnionFind uf = new UnionFind(nodes.size());

        for (Edge edge : edges) {
            if (!uf.connected(edge.n1, edge.n2)) {
                uf.union(edge.n1, edge.n2);
                mst.add(edge);
            }
        }
        return mst;
    }

    private List<Integer> dijkstra(int source, int target) {
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

            for (Edge e : edges) {
                int v = (e.n1 == u) ? e.n2 : (e.n2 == u) ? e.n1 : -1;
                if (v == -1) continue;
                double latency = 1.0 / e.bandwidth;
                if (!visited[v] && dist[u] + latency < dist[v]) {
                    dist[v] = dist[u] + latency;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NetworkOptimizer().setVisible(true));
    }
}