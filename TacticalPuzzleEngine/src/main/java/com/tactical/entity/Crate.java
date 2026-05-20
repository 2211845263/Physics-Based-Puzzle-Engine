package com.tactical.entity;

import com.tactical.Main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Crate extends GameObject {

    private static final Color CRATE_COLOR = new Color(139, 90, 43);

    private List<GameObject> levelObjects;

    public Crate(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public boolean tryPush(int dx, int dy) {
        int targetX = x + dx;
        int targetY = y + dy;

        for (GameObject obj : levelObjects) {
            if (!obj.isVisible() || obj == this) continue;
            if (obj.getX() == targetX && obj.getY() == targetY) {
                if (obj instanceof Wall || obj instanceof Crate) {
                    return false;
                }
            }
        }

        x = targetX;
        y = targetY;
        return true;
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics g) {
        g.setColor(CRATE_COLOR);
        g.fillRect(x * Main.TILE_SIZE, y * Main.TILE_SIZE, width, height);
        g.setColor(CRATE_COLOR.darker());
        g.drawRect(x * Main.TILE_SIZE, y * Main.TILE_SIZE, width - 1, height - 1);
    }

    public void setLevelObjects(List<GameObject> levelObjects) {
        this.levelObjects = levelObjects;
    }
}
