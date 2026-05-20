package com.tactical.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    public enum Direction {
        NONE, UP, DOWN, LEFT, RIGHT
    }

    private volatile Direction currentDirection = Direction.NONE;
    private volatile boolean restartRequested = false;

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP    -> currentDirection = Direction.UP;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN   -> currentDirection = Direction.DOWN;
            case KeyEvent.VK_A, KeyEvent.VK_LEFT   -> currentDirection = Direction.LEFT;
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT  -> currentDirection = Direction.RIGHT;
            case KeyEvent.VK_R                     -> restartRequested = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_UP,
                 KeyEvent.VK_S, KeyEvent.VK_DOWN,
                 KeyEvent.VK_A, KeyEvent.VK_LEFT,
                 KeyEvent.VK_D, KeyEvent.VK_RIGHT -> currentDirection = Direction.NONE;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public Direction consumeDirection() {
        Direction dir = currentDirection;
        currentDirection = Direction.NONE;
        return dir;
    }

    public Direction peekDirection() {
        return currentDirection;
    }

    public boolean consumeRestart() {
        boolean restart = restartRequested;
        restartRequested = false;
        return restart;
    }
}
