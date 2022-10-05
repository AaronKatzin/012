package com.hit.game012.gameplay;

import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.solver.BoardSolver;

import java.util.concurrent.Callable;

public class GetSolverThreaded implements Callable<BoardSolver> {
    public GetSolverThreaded(Board board) {
        this.board = board;
    }

    private Board board;

    @Override
    public BoardSolver call() throws Exception {
        return new BoardSolver(board);
    }
}
