package com.hit.game012.gamelogic.checker;

import com.hit.game012.gamelogic.game.Board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BoardChecker {
    private Board board;
    private List<BooleanRule> rules;
    private final Random r = new Random();

    public BoardChecker(Board board) {
        this.board = board;
//        rules = Arrays.asList(Rules.EQUAL_TILE_COUNT_COL, Rules.EQUAL_TILE_COUNT_ROW,
//                Rules.MAX_2_CONSECUTIVE_COL, Rules.MAX_2_CONSECUTIVE_ROW,
//                Rules.NO_IDENTICAL_COLS, Rules.NO_IDENTICAL_ROWS);
        rules = Arrays.asList(BooleanRules.EQUAL_BLUE_AND_RED, BooleanRules.NO_3_CONSECUTIVE, BooleanRules.NO_IDENTICAL_ROWS_OR_COLUMNS);
    }


//    public CheckResult isValid() {
//        List<CheckResult> notValid = new ArrayList<>();
//        rules.forEach( rule -> {
//            CheckResult result = rule.check(board);
//            System.out.println(result);
//            if (result.getResultState() == CheckResult.State.VIOLATES)
//                notValid.add(result);
//        });
//        if (notValid.isEmpty())
//            return new CheckResult(CheckResult.State.SATISFIED, "Valid", null);
//        return notValid.get(r.nextInt(notValid.size()));
//    }
//
//    public CheckResult isSolved(){
//        CheckResult isFull = Rules.NO_EMPTY_TILES.check(board);
//        if (isFull.getResultState() != CheckResult.State.SATISFIED)
//            return isFull;
//        CheckResult valid = isValid();
//        if (valid.getResultState() != CheckResult.State.SATISFIED)
//            return valid;
//        else
//            return new CheckResult(CheckResult.State.SATISFIED, "Solved", null);
//    }
    public Boolean isValidBoolean(){
        for(BooleanRule rule:rules){
            if(!rule.check(board))
                return false;
        }
        return true;
    }

    public Boolean isSolvedBoolean() {
        if(!board.isFull())
            return false;
        if(!isValidBoolean())
            return false;
        return true;

     }
}
