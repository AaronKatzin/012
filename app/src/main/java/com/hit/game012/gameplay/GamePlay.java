package com.hit.game012.gameplay;

import com.hit.game012.gamelogic.checker.BoardChecker;
import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.generator.BoardGenerator;
import com.hit.game012.gamelogic.solver.BoardSolver;

import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GamePlay {
    private Board board;
    private BoardGenerator boardGenerator;
    private BoardSolver boardSolver;
    private Timer timer;
    private Stack<Move> moves;

    public Board getBoard() {
        return board;
    }

    public GamePlay() {
        boardGenerator = new BoardGenerator();
        timer = new Timer();
        moves = new Stack<>();
    }

    public void startGame(int size){

//        board = boardGenerator.generate(size);
        ThreadPoolExecutor threadpool= new ThreadPoolExecutor(1,1,
                100, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<>());
        try {
            board=threadpool.submit(new GetBoardThreaded(size)).get();
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



    public void onTileClick(Index index){
        long time = System.currentTimeMillis();
        Move lastMove = moves.pop();
        long delay = 500; // 1/2 Second
        if (time - lastMove.getTime() > delay){
            moves.push(lastMove);
        }
        moves.push(new Move(index, board.getTile(index).getSerialized()));

    }
}
