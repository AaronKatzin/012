package com.hit.game012.gamelogic.game;

public class Cell {
    public Tile state= Tile.EMPTY;
    public boolean isLocked = false;

    public boolean isLocked() {
        return isLocked;
    }

    @Override
    public String toString() {
        return state.toString();
    }
}
