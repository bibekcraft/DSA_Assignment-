package networkoptimizer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GraphPanel extends JPanel {
    private List<Edge> mstEdges = new ArrayList<>();
    private List<Integer> path = new ArrayList<>();

    public GraphPanel() {
        setBackground(new Color(230, 235, 240)); // Light blue-gray background
    }

    void setMST(List<Edge> mst) {
        mstEdges = mst;
        repaint();
    }

    void setPath(List<Integer> p) {
        path = p;
        repaint();
    }

    void clear() {
        mstEdges.clear();
        path.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw grid background
        g2d.setColor(new Color(200, 210, 220));
        for (int i = 0; i < getWidth(); i += 20) {
            g2d.drawLine(i, 0, i, getHeight());
        }
        for (int i = 0; i < getHeight(); i += 20) {
            g2d.drawLine(0, i, getWidth(), i);
        }

        // Draw all edges
        g2d.setColor(new Color(150, 150, 150));
        g2d.setStroke(new BasicStroke(1.5f));
        for (Edge e : NetworkOptimizer.edges) {
            Node n1 = NetworkOptimizer.nodes.get(e.n1);
            Node n2 = NetworkOptimizer.nodes.get(e.n2);
            g2d.drawLine(n1.x, n1.y, n2.x, n2.y);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawString("C:" + e.cost + ",B:" + e.bandwidth, (n1.x + n2.x) / 2 + 5, (n1.y + n2.y) / 2 - 5);
            g2d.setColor(new Color(150, 150, 150));
        }

        // Draw MST edges
        g2d.setColor(new Color(0, 100, 200));
        g2d.setStroke(new BasicStroke(2.5f));
        for (Edge e : mstEdges) {
            Node n1 = NetworkOptimizer.nodes.get(e.n1);
            Node n2 = NetworkOptimizer.nodes.get(e.n2);
            g2d.drawLine(n1.x, n1.y, n2.x, n2.y);
        }

        // Draw shortest path
        g2d.setColor(new Color(200, 50, 50));
        g2d.setStroke(new BasicStroke(2.5f));
        for (int i = 0; i < path.size() - 1; i++) {
            Node n1 = NetworkOptimizer.nodes.get(path.get(i));
            Node n2 = NetworkOptimizer.nodes.get(path.get(i + 1));
            g2d.drawLine(n1.x, n1.y, n2.x, n2.y);
        }

        // Draw nodes
        g2d.setStroke(new BasicStroke(1.0f));
        for (Node n : NetworkOptimizer.nodes) {
            g2d.setColor(new Color(50, 50, 150)); // Blue fill
            g2d.fillOval(n.x - 8, n.y - 8, 16, 16);
            g2d.setColor(Color.WHITE);
            g2d.drawString(String.valueOf(n.id), n.x - 3, n.y + 4);
        }
    }
}