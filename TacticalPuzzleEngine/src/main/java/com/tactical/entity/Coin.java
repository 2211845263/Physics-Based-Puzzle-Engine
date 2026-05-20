package com.tactical.entity;

import com.tactical.Main;

import java.awt.Color;
import java.awt.Graphics;

public class Coin extends GameObject {

    public Coin(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics g) {
        int px = x * Main.TILE_SIZE;
        int py = y * Main.TILE_SIZE;
        int margin = width / 4;
        g.setColor(Color.YELLOW);
        g.fillOval(px + margin, py + margin, width / 2, height / 2);
    }
}
