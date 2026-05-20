package com.tactical.entity;

import com.tactical.Main;

import java.awt.Color;
import java.awt.Graphics;

public class Wall extends GameObject {

    private static final Color WALL_COLOR = new Color(80, 80, 80);

    public Wall(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics g) {
        g.setColor(WALL_COLOR);
        g.fillRect(x * Main.TILE_SIZE, y * Main.TILE_SIZE, width, height);
    }
}
