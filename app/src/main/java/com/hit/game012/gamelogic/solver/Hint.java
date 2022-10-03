package com.hit.game012.gamelogic.solver;

import com.hit.game012.gamelogic.game.Index;

import java.util.List;

public class Hint {
    private String message;
    private List<Index> involvedTiles;

    public Hint(String message, List<Index> involvedTiles) {
        this.message = message;
        this.involvedTiles = involvedTiles;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Hint{" +
                "message='" + message + '\'' +
                ", involvedTiles=" + involvedTiles +
                '}';
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Index> getInvolvedTiles() {
        return involvedTiles;
    }

    public void setInvolvedTiles(List<Index> involvedTiles) {
        this.involvedTiles = involvedTiles;
    }
}
