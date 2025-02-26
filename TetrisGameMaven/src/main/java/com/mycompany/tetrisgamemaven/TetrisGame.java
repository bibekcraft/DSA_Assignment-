package com.mycompany.tetrisgamemaven;

import javax.swing.*;
import java.awt.event.*;

public class TetrisGame extends JFrame {
    private GameBoard board;

    public TetrisGame() {
        setTitle("Tetris Game");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        board = new GameBoard();
        add(board);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Block block = board.currentBlock;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (board.canMove(block, -1, 0)) block.x--;
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (board.canMove(block, 1, 0)) block.x++;
                        break;
                    case KeyEvent.VK_DOWN:
                        board.moveBlockDown();
                        break;
                    case KeyEvent.VK_UP:
                        rotateBlock();
                        break;
                }
                board.repaint();
            }

            private void rotateBlock() {
                Block block = board.currentBlock; // Use the current block from GameBoard
                int[][] rotated = new int[block.shape.length][2];
                for (int i = 0; i < block.shape.length; i++) {
                    rotated[i][0] = -block.shape[i][1];
                    rotated[i][1] = block.shape[i][0];
                }
                int[][] oldShape = block.shape;
                block.shape = rotated;
                if (!board.canMove(block, 0, 0)) {
                    block.shape = oldShape; // Revert if the rotation causes a collision
                }
            }
        });
        setFocusable(true);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TetrisGame::new);
    }
}