package com.hit.game012.gamelogic.checker;

import com.hit.game012.gamelogic.game.Board;

/**
 * Functional interface
 */
public interface Rule {
    CheckResult check(Board board);
}
