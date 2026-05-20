package com.tactical.entity;

import com.tactical.Main;
import com.tactical.core.InputHandler;
import com.tactical.core.InputHandler.Direction;
import com.tactical.observer.GameEventSystem;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Player extends GameObject {

    private static final int MOVE_DELAY = 8;

    private final GameEventSystem eventSystem;
    private final List<GameObject> levelObjects;
    private final InputHandler inputHandler;
    private int moveCooldown = 0;

    public Player(int x, int y, int width, int height,
                  GameEventSystem eventSystem, List<GameObject> levelObjects,
                  InputHandler inputHandler) {
        super(x, y, width, height);
        this.eventSystem = eventSystem;
        this.levelObjects = levelObjects;
        this.inputHandler = inputHandler;
    }

    @Override
    public void update() {
        checkEnemyCollision();

        if (moveCooldown > 0) {
            moveCooldown--;
            return;
        }

        Direction dir = inputHandler.consumeDirection();
        if (dir == Direction.NONE) return;

        int dx = 0, dy = 0;
        switch (dir) {
            case UP    -> dy = -1;
            case DOWN  -> dy = 1;
            case LEFT  -> dx = -1;
            case RIGHT -> dx = 1;
            case NONE  -> {}
        }

        int targetX = x + dx;
        int targetY = y + dy;
        Wall targetWall = null;
        Crate targetCrate = null;
        Coin targetCoin = null;
        Enemy targetEnemy = null;
        Door targetDoor = null;

        for (GameObject obj : levelObjects) {
            if (!obj.isVisible() || obj == this) continue;
            if (obj.getX() == targetX && obj.getY() == targetY) {
                if (obj instanceof Wall w) targetWall = w;
                else if (obj instanceof Crate c) targetCrate = c;
                else if (obj instanceof Coin c) targetCoin = c;
                else if (obj instanceof Enemy e) targetEnemy = e;
                else if (obj instanceof Door d) targetDoor = d;
            }
        }

        if (targetWall != null) {
        } else if (targetCrate != null) {
            if (targetCrate.tryPush(dx, dy)) {
                x = targetX;
                y = targetY;
                moveCooldown = MOVE_DELAY;
                if (targetCoin != null) {
                    targetCoin.setVisible(false);
                    eventSystem.fireEvent("COIN_COLLECTED");
                }
            }
        } else if (targetCoin != null) {
            targetCoin.setVisible(false);
            eventSystem.fireEvent("COIN_COLLECTED");
            x = targetX;
            y = targetY;
            moveCooldown = MOVE_DELAY;
        } else if (targetEnemy != null) {
            eventSystem.fireEvent("PLAYER_LOSE");
        } else if (targetDoor != null) {
            if (targetDoor.isPassable()) {
                x = targetX;
                y = targetY;
                moveCooldown = MOVE_DELAY;
                eventSystem.fireEvent("PLAYER_WIN");
            }
        } else {
            x = targetX;
            y = targetY;
            moveCooldown = MOVE_DELAY;
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x * Main.TILE_SIZE, y * Main.TILE_SIZE, width, height);
    }

    private void checkEnemyCollision() {
        for (GameObject obj : levelObjects) {
            if (obj.isVisible() && obj instanceof Enemy
                    && obj.getX() == x && obj.getY() == y) {
                eventSystem.fireEvent("PLAYER_LOSE");
                break;
            }
        }
    }
}
