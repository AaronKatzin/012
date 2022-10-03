package com.hit.game012.gameplay;

import android.graphics.Color;

import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.game.Tile;

public class TileView {
    private Index index;
    private Tile color;
    private boolean isHighlighted = false;
    private boolean isLocked = false;

    public TileView(Index index) {
        this.index = index;
        this.color = Tile.EMPTY;
    }
    public void setColor (Tile tile){
        color = tile;
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public Tile getColor() {
        return color;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}
