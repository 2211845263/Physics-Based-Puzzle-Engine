package com.tactical.observer;

public class CoinManager implements GameObserver {

    private final GameEventSystem eventSystem;
    private int totalCoins;
    private int collectedCoins;

    public CoinManager(int totalCoins, GameEventSystem eventSystem) {
        this.totalCoins = totalCoins;
        this.collectedCoins = 0;
        this.eventSystem = eventSystem;
    }

    @Override
    public void onEvent(String eventType) {
        if ("COIN_COLLECTED".equals(eventType)) {
            collectedCoins++;
            if (collectedCoins >= totalCoins) {
                eventSystem.fireEvent("ALL_COINS_COLLECTED");
            }
        }
    }

    public int getCollectedCoins() {
        return collectedCoins;
    }

    public int getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(int totalCoins) {
        this.totalCoins = totalCoins;
    }
}
