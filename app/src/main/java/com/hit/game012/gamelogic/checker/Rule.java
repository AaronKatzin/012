package com.hit.game012.gamelogic.checker;


import com.hit.game012.gamelogic.game.Board;

/**
 * This is a functional interface for game rules.
 */
@FunctionalInterface
public interface Rule {

    /**
     * Checks if this rule is satisfied by the {@code board}.
     * @param board the board being checked.
     * @return true if the rule holds, false otherwise
     */
    boolean check(Board board);
}
