package com.hit.game012.net.server;

import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.generator.BoardGenerator;


/**
 * Class to generate and hold the daily boards.
 */
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

    /**
     * Generate a new board from BoardGenerator
     *
     * @param size board size
     * @return Board instance
     */
    private Board getBoardOfSize(int size) {
        return generator.generate(size);
    }

    /**
     * Function to generate boards of all supported sizes and log to logger.
     */
    private void generateDailyBoards() {
        for (Integer size : sizes) {
            todayBoards[(size / 2) - 2] = getBoardOfSize(size);
            logger.info("[SERVER][BOARD " + size + "X" + size + "] " + todayBoards[(size / 2) - 2].stringFromBoard());
        }
    }

    /**
     * Function to return a daily board,
     * If board still not created generates all boards.
     *
     * @param size board size
     * @return board of size parameter
     */
    public Board getBoard(int size) {
        Board board = todayBoards[(size / 2) - 2];
        if (board == null) {
            generateDailyBoards();
            board = todayBoards[(size / 2) - 2];
        }
        return board;
    }

    /**
     * Function to be executed by scheduler.
     */
    @Override
    public void run() {
        logger.info("[SERVER][Scheduler] Generating daily boards");
        generateDailyBoards();
    }

}
