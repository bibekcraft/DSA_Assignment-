package com.mycompany.tetrisgamemaven;

import java.awt.Color;
import java.util.Random;

public class Block {
    public int[][] shape; // 2D array representing the block shape
    public Color color;
    public int x, y; // Position on the board
    private static final int[][][] SHAPES = {
        {{0, 0}, {0, 1}, {1, 0}, {1, 1}}, // Square
        {{0, 0}, {0, 1}, {0, 2}, {0, 3}}, // Line
        {{0, 0}, {0, 1}, {0, 2}, {1, 1}}  // T-shape
    };
    private static final Color[] COLORS = {Color.YELLOW, Color.CYAN, Color.MAGENTA};

    public Block() {
        Random rand = new Random();
        int type = rand.nextInt(SHAPES.length);
        shape = SHAPES[type];
        color = COLORS[type];
        x = 4; // Start in the middle
        y = -2; // Start above the board
    }
}