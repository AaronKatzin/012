package com.hit.game012.gamelogic.checker;

import com.hit.game012.gamelogic.game.Board;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BoardChecker {
    private Board board;
    private List<Rule> rules;

    public BoardChecker(Board board) {
        this.board = board;
        rules = Arrays.asList(Rules.EQUAL_BLUE_AND_RED, Rules.NO_3_CONSECUTIVE, Rules.NO_IDENTICAL_ROWS_OR_COLUMNS);
    }


    public Boolean isValid() {
        for (Rule rule : rules) {
            if (!rule.check(board))
                return false;
        }
        return true;
    }

    public Boolean isSolved() {
        if (!board.isFull())
            return false;
        return isValid();

    }
}
