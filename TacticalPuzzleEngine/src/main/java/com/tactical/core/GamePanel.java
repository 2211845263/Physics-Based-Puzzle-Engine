package com.tactical.core;

import com.tactical.Main;
import com.tactical.audio.AudioManager;
import com.tactical.composite.GameGroup;
import com.tactical.level.LevelLoader;
import com.tactical.observer.GameEventSystem;
import com.tactical.observer.GameObserver;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

public class GamePanel extends JPanel implements GameObserver {

    private enum GameState {
        PLAYING, WIN, LOSE
    }

    private static final Color FLOOR_COLOR = new Color(200, 200, 200);

    private final InputHandler inputHandler;
    private GameGroup worldGroup;
    private GameEventSystem eventSystem;
    private GameLoop gameLoop;
    private GameState gameState = GameState.PLAYING;
    private int currentLevel = 1;

    public GamePanel() {
        setPreferredSize(new Dimension(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);

        this.inputHandler = new InputHandler();
        addKeyListener(inputHandler);

        loadCurrentLevel();
    }

    public void startGame() {
        this.gameLoop = new GameLoop(this);
        gameLoop.start();
    }

    public void updateGame() {
        if (inputHandler.consumeRestart()) {
            restartGame();
            return;
        }

        if (gameState == GameState.PLAYING) {
            worldGroup.update();
        }
    }

    private void loadCurrentLevel() {
        this.eventSystem = new GameEventSystem();
        this.eventSystem.registerObserver(this);
        this.eventSystem.registerObserver(AudioManager.getInstance());

        this.worldGroup = new GameGroup("worldGroup");

        LevelLoader levelLoader = new LevelLoader();
        levelLoader.loadLevel(currentLevel, worldGroup, eventSystem, inputHandler);

        this.gameState = GameState.PLAYING;
    }

    private void restartGame() {
        currentLevel = 1;
        loadCurrentLevel();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameState == GameState.PLAYING) {
            drawFloor(g);
            worldGroup.render(g);
        } else {
            drawFloor(g);
            worldGroup.render(g);
            drawEndScreen(g);
        }
    }

    private void drawFloor(Graphics g) {
        g.setColor(FLOOR_COLOR);
        g.fillRect(0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
    }

    private void drawEndScreen(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);

        String message;
        Color color;

        if (gameState == GameState.WIN) {
            message = "You Completed All Levels!";
            color = Color.GREEN;
        } else {
            message = "Game Over!";
            color = Color.RED;
        }

        g.setColor(color);
        g.setFont(new Font("Arial", Font.BOLD, 48));

        int textWidth = g.getFontMetrics().stringWidth(message);
        int xPos = (Main.SCREEN_WIDTH - textWidth) / 2;
        int yPos = Main.SCREEN_HEIGHT / 2;

        g.drawString(message, xPos, yPos);

        String restartMessage = "Press R to Restart";
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        int restartWidth = g.getFontMetrics().stringWidth(restartMessage);
        int restartX = (Main.SCREEN_WIDTH - restartWidth) / 2;
        int restartY = yPos + 40;

        g.drawString(restartMessage, restartX, restartY);
    }

    @Override
    public void onEvent(String eventType) {
        switch (eventType) {
            case "PLAYER_WIN" -> {
                if (currentLevel < 3) {
                    currentLevel++;
                    loadCurrentLevel();
                } else {
                    gameState = GameState.WIN;
                }
            }
            case "PLAYER_LOSE" -> gameState = GameState.LOSE;
        }
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public GameGroup getWorldGroup() {
        return worldGroup;
    }

    public GameEventSystem getEventSystem() {
        return eventSystem;
    }
}
