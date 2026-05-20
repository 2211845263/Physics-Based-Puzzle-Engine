package com.tactical;

import com.tactical.core.GamePanel;

import javax.swing.JFrame;

public class Main {

    public static final int TILE_SIZE = 64;
    public static final int GRID_COLS = 12;
    public static final int GRID_ROWS = 10;
    public static final int SCREEN_WIDTH = TILE_SIZE * GRID_COLS;
    public static final int SCREEN_HEIGHT = TILE_SIZE * GRID_ROWS;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Tactical Puzzle Engine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gamePanel.startGame();
    }
}
