package com.hit.game012.gameplay;

import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.generator.BoardGenerator;

import java.util.concurrent.Callable;

public class GetBoardThreaded implements Callable<Board> {

    private int size;
    private BoardGenerator boardGenerator;

    public GetBoardThreaded(int size) {
        this.size = size;
        boardGenerator = new BoardGenerator();
    }

    @Override
    public Board call() throws Exception {
        return boardGenerator.generate(size);
    }
}
