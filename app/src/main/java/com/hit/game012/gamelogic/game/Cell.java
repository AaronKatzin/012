package com.hit.game012.gamelogic.game;

public class Cell {
    public Tile state= Tile.EMPTY;
    public boolean isLocked = false;

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    @Override
    public String toString() {
        return state.toString();
    }
}
