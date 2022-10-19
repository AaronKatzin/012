package com.hit.game012.gamelogic.checker;


import com.hit.game012.gamelogic.game.Board;

/**
 * Created on 02/11/2015.
 * This is a class of rules that are optimized as the result
 * is not concerned with additional metadata.
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
