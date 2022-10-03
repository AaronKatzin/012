package com.hit.game012.gameplay;

import com.hit.game012.gamelogic.game.Index;

public class Move {
    private Index index;
    private char color;
    private long time;

    public Move(Index index, char color) {
        this.index = index;
        this.color = color;
        this.time = System.currentTimeMillis();
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public char getColor() {
        return color;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setColor(char color) {
        this.color = color;
    }
}
