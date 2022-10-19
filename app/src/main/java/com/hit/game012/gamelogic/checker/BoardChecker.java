package com.hit.game012.gamelogic.checker;

import com.hit.game012.gamelogic.game.Board;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Class to check if board is solved successfully
 * holds a set of Rules.
 */
public class BoardChecker {
    private Board board;
    private List<Rule> rules;

    public BoardChecker(Board board) {
        this.board = board;
        rules = Arrays.asList(Rules.EQUAL_COLOR_COUNT, Rules.NO_3_CONSECUTIVE, Rules.NO_IDENTICAL_ROWS_OR_COLUMNS);
    }


    /**
     * Check if all the rules are satisfied.
     *
     * @return true if valid, false otherwise.
     */
    public Boolean isValid() {
        for (Rule rule : rules) {
            if (!rule.check(board))
                return false;
        }
        return true;
    }

    /**
     * Check if board if full and valid.
     *
     * @return true if solved, false otherwise.
     */
    public Boolean isSolved() {
        if (!board.isFull())
            return false;
        return isValid();

    }
}
