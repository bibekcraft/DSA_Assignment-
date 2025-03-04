package networkoptimizer;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GraphPanel extends JPanel {
    private List<Edge> optimalEdges;
    private List<Integer> path;
    private Integer firstNode = null;  // For edge creation
    
    public GraphPanel(NetworkOptimizer optimizer) {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(700, 500));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {  // Left click
                    handleLeftClick(e.getX(), e.getY(), optimizer);
                }
            }
        });
    }

    private void handleLeftClick(int x, int y, NetworkOptimizer optimizer) {
        // Check if clicking on existing node
        for (Node n : NetworkOptimizer.nodes) {
            if (Math.abs(n.x - x) < 15 && Math.abs(n.y - y) < 15) {
                if (firstNode == null) {
                    firstNode = n.id;
                    optimizer.updateStatus("Selected node " + n.name + ". Click another node for edge.");
                } else if (firstNode != n.id) {
                    optimizer.addEdge(firstNode, n.id);
                    firstNode = null;
                    optimizer.updateStatus("Ready to add nodes or select first node for edge.");
                }
                repaint();
                return;
            }
        }
        
        // If no node clicked, add new node
        optimizer.addNode(x, y);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
            java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.GRAY);
        for (Edge e : NetworkOptimizer.edges) {
            Node n1 = NetworkOptimizer.nodes.get(e.n1);
            Node n2 = NetworkOptimizer.nodes.get(e.n2);
            g2d.drawLine(n1.x, n1.y, n2.x, n2.y);
            g2d.drawString(String.format("%d/%d/%d", e.cost, e.bandwidth, e.latency),
                (n1.x + n2.x) / 2, (n1.y + n2.y) / 2);
        }

        if (optimalEdges != null) {
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(2));
            for (Edge e : optimalEdges) {
                Node n1 = NetworkOptimizer.nodes.get(e.n1);
                Node n2 = NetworkOptimizer.nodes.get(e.n2);
                g2d.drawLine(n1.x, n1.y, n2.x, n2.y);
            }
            g2d.setStroke(new BasicStroke(1));
        }

        if (path != null && path.size() > 1) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2));
            for (int i = 0; i < path.size() - 1; i++) {
                Node n1 = NetworkOptimizer.nodes.get(path.get(i));
                Node n2 = NetworkOptimizer.nodes.get(path.get(i + 1));
                g2d.drawLine(n1.x, n1.y, n2.x, n2.y);
            }
            g2d.setStroke(new BasicStroke(1));
        }

        for (Node n : NetworkOptimizer.nodes) {
            g2d.setColor(firstNode != null && firstNode == n.id ? Color.GREEN : Color.BLACK);
            g2d.fillOval(n.x - 10, n.y - 10, 20, 20);
            g2d.setColor(Color.WHITE);
            g2d.drawString(n.name, n.x - 5, n.y + 5);
        }
    }

    public void setOptimalEdges(List<Edge> edges) {
        this.optimalEdges = edges;
        repaint();
    }

    public void setPath(List<Integer> path) {
        this.path = path;
        repaint();
    }

    public void clear() {
        optimalEdges = null;
        path = null;
        firstNode = null;
        repaint();
    }
    
    public void resetFirstNode() {
        firstNode = null;
        repaint();
    }
}