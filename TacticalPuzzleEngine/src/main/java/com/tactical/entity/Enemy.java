package com.tactical.entity;

import com.tactical.Main;
import com.tactical.prototype.EnemyPrototype;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Enemy extends GameObject implements EnemyPrototype {

    private static final int MOVE_INTERVAL = 30;
    private static final Random RANDOM = new Random();

    private int speed;
    private String patrolDirection;
    private int dx = 1;
    private int dy = 0;
    private int moveTick = 0;
    private List<GameObject> levelObjects;

    public Enemy(int x, int y, int speed, String patrolDirection) {
        super(x, y, Main.TILE_SIZE, Main.TILE_SIZE);
        this.speed = speed;
        this.patrolDirection = patrolDirection;
    }

    @Override
    public Enemy clone() {
        return new Enemy(this.x, this.y, this.speed, this.patrolDirection);
    }

    @Override
    public void update() {
        if (levelObjects == null) return;

        moveTick++;
        if (moveTick < MOVE_INTERVAL) return;
        moveTick = 0;

        int targetX = x + dx;
        int targetY = y + dy;

        if (isBlocked(targetX, targetY)) {
            int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            List<int[]> dirList = new ArrayList<>(Arrays.asList(dirs));
            Collections.shuffle(dirList, RANDOM);

            boolean found = false;
            for (int[] d : dirList) {
                if (!isBlocked(x + d[0], y + d[1])) {
                    dx = d[0];
                    dy = d[1];
                    targetX = x + dx;
                    targetY = y + dy;
                    found = true;
                    break;
                }
            }

            if (!found) {
                return;
            }
        }

        x = targetX;
        y = targetY;
    }

    private boolean isBlocked(int targetX, int targetY) {
        if (targetX < 0 || targetY < 0 || targetX >= Main.GRID_COLS || targetY >= Main.GRID_ROWS) {
            return true;
        }

        for (GameObject obj : levelObjects) {
            if (!obj.isVisible() || obj == this) continue;
            if (obj.getX() == targetX && obj.getY() == targetY) {
                if (obj instanceof Wall || obj instanceof Crate || obj instanceof Door) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x * Main.TILE_SIZE, y * Main.TILE_SIZE, width, height);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getPatrolDirection() {
        return patrolDirection;
    }

    public void setPatrolDirection(String patrolDirection) {
        this.patrolDirection = patrolDirection;
    }

    public void setLevelObjects(List<GameObject> levelObjects) {
        this.levelObjects = levelObjects;
    }
}
