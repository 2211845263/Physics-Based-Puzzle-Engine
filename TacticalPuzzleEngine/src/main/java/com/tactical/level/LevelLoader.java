package com.tactical.level;

import com.tactical.Main;
import com.tactical.composite.GameGroup;
import com.tactical.composite.GameLeaf;
import com.tactical.core.InputHandler;
import com.tactical.entity.*;
import com.tactical.observer.CoinManager;
import com.tactical.observer.DoorController;
import com.tactical.observer.GameEventSystem;
import com.tactical.prototype.EnemySpawner;

import java.util.ArrayList;
import java.util.List;

public class LevelLoader {

    private static final int[][] LEVEL_1 = {
        {1,1,1,1,1,1,1,1,1,1,1,1},
        {1,6,0,0,2,0,0,0,0,0,0,1},
        {1,0,1,1,0,1,0,1,1,1,0,1},
        {1,0,0,0,0,1,0,0,2,4,0,1},
        {1,2,1,3,0,0,0,1,0,1,0,1},
        {1,0,0,0,0,1,0,0,0,0,0,1},
        {1,0,1,0,4,0,1,0,1,1,0,1},
        {1,0,0,0,0,0,0,0,0,2,0,1},
        {1,0,1,1,0,1,0,1,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,5,1}
    };

    private static final int[][] LEVEL_2 = {
        {1,1,1,1,1,1,1,1,1,1,1,1},
        {1,6,0,1,0,0,1,0,0,0,0,1},
        {1,0,3,0,0,1,0,3,0,1,0,1},
        {1,1,0,1,0,0,0,0,2,0,0,1},
        {1,0,0,0,2,1,3,1,0,1,0,1},
        {1,0,1,3,0,0,0,0,0,0,2,1},
        {1,0,0,0,0,1,0,1,4,0,0,1},
        {1,2,1,1,0,0,3,0,0,1,0,1},
        {1,0,0,0,0,1,0,0,0,0,4,1},
        {1,1,1,1,1,1,1,1,1,1,5,1}
    };

    private static final int[][] LEVEL_3 = {
        {1,1,1,1,1,1,1,1,1,1,1,1},
        {1,6,1,0,0,1,0,0,0,1,0,1},
        {1,0,0,3,0,0,2,1,0,0,0,1},
        {1,1,0,1,1,0,0,0,3,1,0,1},
        {1,0,2,0,0,0,1,0,0,0,2,1},
        {1,0,1,1,3,0,0,1,0,1,0,1},
        {1,0,0,0,0,2,0,0,0,0,0,1},
        {1,1,0,1,0,1,4,1,0,1,3,1},
        {1,0,0,0,0,0,0,0,2,0,4,1},
        {1,1,1,1,1,1,1,1,1,1,5,1}
    };

    public void loadLevel(int levelNumber, GameGroup worldGroup, GameEventSystem eventSystem,
                          InputHandler inputHandler) {
        
        int[][] levelData = switch (levelNumber) {
            case 2 -> LEVEL_2;
            case 3 -> LEVEL_3;
            default -> LEVEL_1;
        };

        int ts = Main.TILE_SIZE;
        List<GameObject> levelObjects = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();
        List<Crate> crates = new ArrayList<>();
        Door door = null;
        int coinCount = 0;
        int playerStartX = 1, playerStartY = 1;

        EnemySpawner spawner = new EnemySpawner();
        Enemy patrolTemplate = new Enemy(0, 0, 2, "horizontal");
        spawner.registerTemplate("patrol", patrolTemplate);

        for (int row = 0; row < Main.GRID_ROWS; row++) {
            for (int col = 0; col < Main.GRID_COLS; col++) {
                int tile = levelData[row][col];

                switch (tile) {
                    case 1 -> {
                        Wall wall = new Wall(col, row, ts, ts);
                        levelObjects.add(wall);
                        worldGroup.addComponent(new GameLeaf(wall));
                    }
                    case 2 -> {
                        coinCount++;
                        Coin coin = new Coin(col, row, ts, ts);
                        levelObjects.add(coin);
                        worldGroup.addComponent(new GameLeaf(coin));
                    }
                    case 3 -> {
                        Crate crate = new Crate(col, row, ts, ts);
                        crates.add(crate);
                        levelObjects.add(crate);
                        worldGroup.addComponent(new GameLeaf(crate));
                    }
                    case 4 -> {
                        Enemy enemy = (Enemy) spawner.spawn("patrol", col, row);
                        enemies.add(enemy);
                        levelObjects.add(enemy);
                        worldGroup.addComponent(new GameLeaf(enemy));
                    }
                    case 5 -> {
                        door = new Door(col, row, ts, ts);
                        levelObjects.add(door);
                        worldGroup.addComponent(new GameLeaf(door));
                    }
                    case 6 -> {
                        playerStartX = col;
                        playerStartY = row;
                    }
                }
            }
        }

        for (Enemy enemy : enemies) {
            System.out.println("Spawned enemy 'patrol' at pixel position (" + (enemy.getX() * ts) + ", " + (enemy.getY() * ts) + ")");
        }

        Player player = new Player(playerStartX, playerStartY, ts, ts,
                                   eventSystem, levelObjects, inputHandler);
        levelObjects.add(player);
        worldGroup.addComponent(new GameLeaf(player));

        for (Enemy enemy : enemies) {
            enemy.setLevelObjects(levelObjects);
        }
        for (Crate crate : crates) {
            crate.setLevelObjects(levelObjects);
        }

        CoinManager coinManager = new CoinManager(coinCount, eventSystem);
        eventSystem.registerObserver(coinManager);

        if (door != null) {
            DoorController doorController = new DoorController(door);
            eventSystem.registerObserver(doorController);
        }
    }

    public void loadLevel(String levelName) {
    }
}
