package com.hit.game012.gameplay;

import com.hit.game012.gamelogic.checker.BoardChecker;
import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.generator.BoardGenerator;
import com.hit.game012.gamelogic.solver.BoardSolver;

public class GamePlay {
    private Board board;
    private BoardGenerator boardGenerator;
    private BoardSolver boardSolver;
    private Timer timer;

    public GamePlay() {
        boardGenerator = new BoardGenerator();
        timer = new Timer();
    }
    public void startGame(int size){
        board = boardGenerator.generate(size);
        boardSolver = new BoardSolver(board);
        timer.start();

        /*
        TODO:
            - CellView Obj -> build the cell + draw
            - GameBoard Obj -> build the board with cells + draw
            - Timer Obj -> to implement
            - event listeners to all cells
            - locked cell mechanics
            - validate (Obj?) the solved board
            - hints support
         */


    }
}
