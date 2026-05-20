package com.tactical.observer;

import java.util.ArrayList;
import java.util.List;

public class GameEventSystem {

    private final List<GameObserver> listeners = new ArrayList<>();

    public void registerObserver(GameObserver o) {
        listeners.add(o);
    }

    public void fireEvent(String eventType) {
        for (GameObserver listener : listeners) {
            listener.onEvent(eventType);
        }
    }
}
