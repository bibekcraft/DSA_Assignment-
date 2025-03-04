package networkoptimizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GraphPanel extends JPanel {
    private final NetworkOptimizer optimizer;
    private int firstNode = -1;
    private List<Edge> optimalEdges = new ArrayList<>();
    private List<Integer> path = new ArrayList<>();

    public GraphPanel(NetworkOptimizer optimizer) {
        this.optimizer = optimizer;
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    handleLeftClick(e.getX(), e.getY());
                }
            }
        });
    }

    private void handleLeftClick(int x, int y) {
        int clickedNode = findNodeAt(x, y);
        if (clickedNode == -1) {
            optimizer.addNode(x, y);
            optimizer.updateStatus("Node added. Click another node to add an edge.");
        } else if (firstNode == -1) {
            firstNode = clickedNode;
            optimizer.updateStatus("First node selected. Click another node to add an edge.");
        } else if (firstNode != clickedNode) {
            optimizer.addEdge(firstNode, clickedNode);
            firstNode = -1;
            optimizer.updateStatus("Edge added. Click to add more nodes.");
        }
        repaint();
    }

    private int findNodeAt(int x, int y) {
        for (Node node : NetworkOptimizer.nodes) {
            if (Math.abs(node.x - x) < 20 && Math.abs(node.y - y) < 20) {
                return node.id;
            }
        }
        return -1;
    }

    public void setOptimalEdges(List<Edge> edges) {
        this.optimalEdges = edges;
        repaint();
    }

    public void setPath(List<Integer> path) {
        this.path = path;
        repaint();
    }

    public void resetFirstNode() {
        firstNode = -1;
        optimizer.updateStatus("Click to add nodes, click two nodes to add edge");
    }

    public void clear() {
        optimalEdges.clear();
        path.clear();
        firstNode = -1;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw all edges
        g2d.setColor(Color.GRAY);
        for (Edge e : NetworkOptimizer.edges) {
            Node n1 = NetworkOptimizer.nodes.get(e.n1);
            Node n2 = NetworkOptimizer.nodes.get(e.n2);
            g2d.drawLine(n1.x, n1.y, n2.x, n2.y);
            g2d.drawString("C:" + e.cost + ",L:" + e.latency, (n1.x + n2.x) / 2, (n1.y + n2.y) / 2);
        }

        // Draw optimal edges
        g2d.setColor(Color.GREEN);
        for (Edge e : optimalEdges) {
            Node n1 = NetworkOptimizer.nodes.get(e.n1);
            Node n2 = NetworkOptimizer.nodes.get(e.n2);
            g2d.drawLine(n1.x, n1.y, n2.x, n2.y);
        }

        // Draw path
        g2d.setColor(Color.BLUE);
        for (int i = 0; i < path.size() - 1; i++) {
            Node n1 = NetworkOptimizer.nodes.get(path.get(i));
            Node n2 = NetworkOptimizer.nodes.get(path.get(i + 1));
            g2d.drawLine(n1.x, n1.y, n2.x, n2.y);
        }

        // Draw nodes
        g2d.setColor(Color.RED);
        for (Node node : NetworkOptimizer.nodes) {
            g2d.fillOval(node.x - 10, node.y - 10, 20, 20);
            g2d.setColor(Color.BLACK);
            g2d.drawString(node.name, node.x + 10, node.y);
            g2d.setColor(Color.RED);
        }
    }
}