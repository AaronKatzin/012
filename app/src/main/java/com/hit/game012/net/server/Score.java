package com.hit.game012.net.server;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Class representing the score.
 * Comparable for the leader board calculation, Serializable for saving the leaderboard.
 */
public class Score implements Comparable<Score>, Serializable {
    private int score;
    private int boardSize;

    public Score(int score, int boardSize) {
        this.score = score;
        this.boardSize = boardSize;
    }

    public int getScore() {
        return score;
    }

    public int getBoardSize() {
        return boardSize;
    }

    @Override
    public int compareTo(@NotNull Score o) {
        return score - o.score;
    }

    @Override
    public String toString() {
        return score + "-" + boardSize;
    }
}
