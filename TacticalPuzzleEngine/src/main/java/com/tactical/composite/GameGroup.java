package com.tactical.composite;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class GameGroup implements GameComponent {

    private final String name;
    private final List<GameComponent> children = new ArrayList<>();

    public GameGroup(String name) {
        this.name = name;
    }

    public void addComponent(GameComponent c) {
        children.add(c);
    }

    public void removeComponent(GameComponent c) {
        children.remove(c);
    }

    @Override
    public void update() {
        for (GameComponent child : children) {
            child.update();
        }
    }

    @Override
    public void render(Graphics g) {
        for (GameComponent child : children) {
            child.render(g);
        }
    }

    public List<GameComponent> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }
}
