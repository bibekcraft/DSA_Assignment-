package com.mycompany.tetrisgamemaven;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class GameBoard extends JPanel {
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;
    private int[][] board; // 2D array for the game board
    protected Block currentBlock; // Changed to protected for access in TetrisGame
    private Queue<Block> blockQueue;
    private int score;

    public GameBoard() {
        setPreferredSize(new Dimension(BOARD_WIDTH * 40, BOARD_HEIGHT * 30));
        setBackground(Color.BLACK);
        board = new int[BOARD_HEIGHT][BOARD_WIDTH]; // 0 = empty, 1 = filled
        blockQueue = new LinkedList<>();
        score = 0;
        spawnBlock();
        startGameLoop();
    }

    private void spawnBlock() {
        if (blockQueue.isEmpty()) {
            blockQueue.add(new Block()); // Add a new block
        }
        currentBlock = blockQueue.poll();
    }

    private void startGameLoop() {
        new Timer(500, e -> {
            moveBlockDown();
            repaint();
        }).start();
    }

    public void moveBlockDown() { // Changed to public for access in TetrisGame
        if (canMove(currentBlock, 0, 1)) {
            currentBlock.y++;
        } else {
            placeBlock();
            checkLines();
            spawnBlock();
            if (isGameOver()) {
                JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
                System.exit(0);
            }
        }
    }

    public boolean canMove(Block block, int dx, int dy) { // Changed to public
        int newX = block.x + dx;
        int newY = block.y + dy;
        for (int[] pos : block.shape) {
            int x = newX + pos[0];
            int y = newY + pos[1];
            if (x < 0 || x >= BOARD_WIDTH || y >= BOARD_HEIGHT || (y >= 0 && board[y][x] != 0)) {
                return false;
            }
        }
        return true;
    }

    private void placeBlock() {
        for (int[] pos : currentBlock.shape) {
            int x = currentBlock.x + pos[0];
            int y = currentBlock.y + pos[1];
            if (y >= 0) board[y][x] = 1;
        }
    }

    private void checkLines() {
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            boolean full = true;
            for (int x = 0; x < BOARD_WIDTH; x++) {
                if (board[y][x] == 0) full = false;
            }
            if (full) {
                score += 10;
                for (int i = y; i > 0; i--) {
                    board[i] = board[i - 1].clone();
                }
                board[0] = new int[BOARD_WIDTH];
            }
        }
    }

    private boolean isGameOver() {
        return !canMove(currentBlock, 0, 0);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the board
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                if (board[y][x] != 0) {
                    g.setColor(Color.BLUE);
                    g.fillRect(x * 40, y * 30, 40, 30);
                }
            }
        }
        // Draw the current block
        if (currentBlock != null) {
            g.setColor(currentBlock.color);
            for (int[] pos : currentBlock.shape) {
                int x = (currentBlock.x + pos[0]) * 40;
                int y = (currentBlock.y + pos[1]) * 30;
                if (currentBlock.y + pos[1] >= 0) {
                    g.fillRect(x, y, 40, 30);
                }
            }
        }
        // Draw score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
    }
}