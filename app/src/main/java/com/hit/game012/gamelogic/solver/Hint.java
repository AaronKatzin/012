package com.hit.game012.gamelogic.solver;

import com.hit.game012.gamelogic.game.Index;

import java.util.List;

public class Hint {
    private int message;
    private List<Index> involvedTiles;

    public Hint(int message, List<Index> involvedTiles) {
        this.message = message;
        this.involvedTiles = involvedTiles;
    }

    public int getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Hint{" +
                "message='" + message + '\'' +
                ", involvedTiles=" + involvedTiles +
                '}';
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public List<Index> getInvolvedTiles() {
        return involvedTiles;
    }

}
