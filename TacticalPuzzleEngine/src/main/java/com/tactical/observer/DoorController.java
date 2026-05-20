package com.tactical.observer;

import com.tactical.entity.Door;

public class DoorController implements GameObserver {

    private final Door door;

    public DoorController(Door door) {
        this.door = door;
    }

    @Override
    public void onEvent(String eventType) {
        if ("ALL_COINS_COLLECTED".equals(eventType)) {
            door.setVisible(true);
            door.setPassable(true);
        }
    }

    public Door getDoor() {
        return door;
    }
}
