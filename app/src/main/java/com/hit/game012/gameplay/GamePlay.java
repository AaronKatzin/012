package com.hit.game012.gameplay;

import com.hit.game012.gamelogic.checker.BoardChecker;
import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.generator.BoardGenerator;
import com.hit.game012.gamelogic.solver.BoardSolver;
import com.hit.game012.gamelogic.solver.Hint;

import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GamePlay {
    private Board board;
    //    private BoardGenerator boardGenerator;
    private BoardSolver boardSolver;
    private static Timer timer;
    private static Stack<Move> moves;

    public Board getBoard() {
        return board;
    }

    public GamePlay() {
//        boardGenerator = new BoardGenerator();
        timer = new Timer();
        moves = new Stack<>();
    }

    public void startGame(int size) {

//        board = boardGenerator.generate(size);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2,
                1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        try {
            board = threadPoolExecutor.submit(new GetBoardThreaded(size)).get();
//            boardSolver = threadPoolExecutor.submit(new GetSolverThreaded(board)).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boardSolver = new BoardSolver(board);
        timer.start();
        System.out.println(board);
        // Class viewGameBoard
        // show board

        // Stack of indexes -> undo


        /*
        TODO:
            - event listeners to all cells
            - locked cell mechanics
            - validate (Obj?) the solved board
            - hints support
            - endGame with timer.stop() and animation to win lose
         */

    }


    public static void onTileClick(Move move) {
        long time = System.currentTimeMillis();
        if (!moves.empty()) {
            Move lastMove = moves.pop();
            long delay = 1000; // 1 Second
            if (lastMove.getIndex() != move.getIndex() || time - lastMove.getTime() > delay) {
                moves.push(lastMove);
            }
        }
        moves.push(move);
//        System.out.println(moves);
    }
    public Hint requestHint(){
        return boardSolver.requestHint();
    }
    public long getGameTime(){
        return timer.getTotalTime();
    }

}
