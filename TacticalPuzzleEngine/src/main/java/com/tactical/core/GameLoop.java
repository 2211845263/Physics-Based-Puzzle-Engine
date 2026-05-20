package com.tactical.core;

public class GameLoop implements Runnable {

    private static final int TARGET_FPS = 60;
    private static final long OPTIMAL_TIME = 1_000_000_000L / TARGET_FPS;

    private final GamePanel gamePanel;
    private Thread gameThread;
    private volatile boolean running;

    public GameLoop(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this, "GameLoop");
        gameThread.setDaemon(true);
        gameThread.start();
    }

    public void stop() {
        running = false;
        try {
            if (gameThread != null) {
                gameThread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long accumulator = 0;

        while (running) {
            long currentTime = System.nanoTime();
            long elapsed = currentTime - lastTime;
            lastTime = currentTime;
            accumulator += elapsed;

            while (accumulator >= OPTIMAL_TIME) {
                gamePanel.updateGame();
                accumulator -= OPTIMAL_TIME;
            }

            gamePanel.repaint();

            try {
                long sleepTime = (OPTIMAL_TIME - (System.nanoTime() - lastTime)) / 1_000_000;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public boolean isRunning() {
        return running;
    }
}
