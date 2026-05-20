package com.tactical.prototype;

import com.tactical.entity.Enemy;

import java.util.HashMap;
import java.util.Map;

public class EnemySpawner {

    private final Map<String, EnemyPrototype> templates = new HashMap<>();

    public void registerTemplate(String name, EnemyPrototype template) {
        templates.put(name, template);
    }

    public EnemyPrototype spawn(String name, int x, int y) {
        EnemyPrototype template = templates.get(name);
        if (template == null) {
            return null;
        }
        EnemyPrototype clone = template.clone();
        if (clone instanceof Enemy enemy) {
            enemy.setX(x);
            enemy.setY(y);
        }
        return clone;
    }
}
