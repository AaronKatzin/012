package com.hit.game012.net.server;

import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.generator.BoardGenerator;



public class DailyBoardGenerator implements Runnable {
    private Board[] todayBoards;
    private BoardGenerator generator;
    private int[] sizes = {4, 6, 8, 10};
    private Logger logger;

    public DailyBoardGenerator(Logger logger) {
        generator = new BoardGenerator();
        todayBoards = new Board[sizes.length];
        this.logger = logger;

    }

    private Board getBoardOfSize(int size) {
        return generator.generate(size);
    }

    private void generateDailyBoards() {
        for (Integer size : sizes) {
            todayBoards[(size / 2) - 2] = getBoardOfSize(size);
            logger.info("[SERVER][BOARD " + size + "X" + size + "] " + todayBoards[(size / 2) - 2].stringFromBoard());
        }
    }

    public Board getBoard(int size) {
        Board board = todayBoards[(size / 2) - 2];
        if (board == null){
            generateDailyBoards();
            board = todayBoards[(size / 2) - 2];
        }
        return board;
    }

    @Override
    public void run() {
        logger.info("[SERVER][Scheduler] Generating daily boards");
        generateDailyBoards();
    }

}
