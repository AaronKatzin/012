package com.hit.game012.gamelogic.solver;

import com.hit.game012.gamelogic.game.Index;

import java.util.List;

/**
 * Class representing a hint for the user.
 * Hint contains message and list of involved tiles.
 */
public class Hint {
    private int message;
    private List<Index> involvedTiles;

    public Hint(int message, List<Index> involvedTiles) {
        this.message = message;
        this.involvedTiles = involvedTiles;
    }

    public List<Index> getInvolvedTiles() {
        return involvedTiles;
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

}
