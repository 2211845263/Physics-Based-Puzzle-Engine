package com.tactical.entity;

import com.tactical.Main;

import java.awt.Color;
import java.awt.Graphics;

public class Door extends GameObject {

    private static final Color CLOSED_COLOR = new Color(50, 50, 50);
    private static final Color OPEN_COLOR = new Color(0, 200, 0);

    private boolean passable = false;

    public Door(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics g) {
        g.setColor(passable ? OPEN_COLOR : CLOSED_COLOR);
        g.fillRect(x * Main.TILE_SIZE, y * Main.TILE_SIZE, width, height);
    }

    public boolean isPassable() {
        return passable;
    }

    public void setPassable(boolean passable) {
        this.passable = passable;
    }
}
