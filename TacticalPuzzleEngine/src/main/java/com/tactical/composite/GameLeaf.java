package com.tactical.composite;

import com.tactical.entity.GameObject;

import java.awt.Graphics;

public class GameLeaf implements GameComponent {

    private final GameObject gameObject;

    public GameLeaf(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    @Override
    public void update() {
        if (gameObject.isVisible()) {
            gameObject.update();
        }
    }

    @Override
    public void render(Graphics g) {
        if (gameObject.isVisible()) {
            gameObject.render(g);
        }
    }

    public GameObject getGameObject() {
        return gameObject;
    }
}
