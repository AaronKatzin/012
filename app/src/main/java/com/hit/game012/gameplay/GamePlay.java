package com.hit.game012.gameplay;

import com.hit.game012.gamelogic.checker.BoardChecker;
import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.generator.BoardGenerator;
import com.hit.game012.gamelogic.solver.BoardSolver;

import java.util.Stack;

public class GamePlay {
    private Board board;
    private BoardGenerator boardGenerator;
    private BoardSolver boardSolver;
    private Timer timer;
    private Stack<Move> moves;

    public GamePlay() {
        boardGenerator = new BoardGenerator();
        timer = new Timer();
        moves = new Stack<>();
    }

    public void startGame(int size){
        board = boardGenerator.generate(size);
        boardSolver = new BoardSolver(board);

        timer.start();

        // Class viewGameBoard

        // Stack of indexes -> undo


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
