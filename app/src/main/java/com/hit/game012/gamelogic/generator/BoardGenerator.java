package com.hit.game012.gamelogic.generator;

import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.game.Tile;
import com.hit.game012.gamelogic.solver.BoardSolver;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * BoardGenerator class to generate a board to solve.
 * contains a final variables to create a solvable board.
 */
public class BoardGenerator {
    private final int MAX_PRECENTAGE_THRESHOLD = 40;
    private final int MAX_REMOVE_ATTEMPTS = 32;
    private final int MAX_ATTEMPTS_TO_SOLVE = 6;
    private final SolvedBoardGenerator[] solvedBoardGenerators;
    private final int[] boardSizes = {4, 6, 8, 10};

    public BoardGenerator() {
        solvedBoardGenerators = new SolvedBoardGenerator[4];
        for (int i = 0; i < boardSizes.length; i++)
            solvedBoardGenerators[i] = new SolvedBoardGenerator(boardSizes[i]);
    }

    private SolvedBoardGenerator getGenerator(int size) {
        if (isAcceptedSize(size))
            return solvedBoardGenerators[size / 2 - 2];
        return null;
    }

    /**
     * Function to generate a board with size param.
     * receive a solved board from solvedBoardGenerators.
     * iteratively  remove tiles from the solved board and checks if board is still solvable.
     * @param size
     * @return board that can be solved
     */
    public Board generate(int size) {
        if (!isAcceptedSize(size))
            throw new IllegalArgumentException("Invalid size");

        // Generate a new solved board
        Board solvedBoard = getGenerator(size).generateBoard();

        // Main loop to remove tiles
        int attempts = 0;
        Board bestBoard = null;
        Queue<Index> allIndexes = null;
        while (attempts++ < MAX_REMOVE_ATTEMPTS) {
            Board workingBoard = solvedBoard.copy();
            BoardSolver solver = new BoardSolver(workingBoard);
            allIndexes = buildIndexesToPull(size);
            Set<Index> removedIndexes = new HashSet<>();
            int failedAttempts = 0;
            while (!allIndexes.isEmpty() && failedAttempts < MAX_ATTEMPTS_TO_SOLVE) {
                Index toRemove = allIndexes.poll();
                Tile removedTile = workingBoard.setTile(toRemove, Tile.EMPTY);
                // Hold the removed tile color in order to return to previous state if board is now unsolvable

                if (solver.tryToSolve()) {
                    // Board is solvable
                    removedIndexes.add(toRemove);
                    restoreAfterSolve(removedIndexes, workingBoard);
                    failedAttempts = 0;
                } else {
                    // Not solvable, place the tile back
                    restoreAfterSolve(removedIndexes, workingBoard);
                    workingBoard.setTile(toRemove, removedTile);
                    failedAttempts++;
                }
            }
            // Check if the board is good enough
            if (workingBoard.percentageSolved() < MAX_PRECENTAGE_THRESHOLD) {
                // Board is good to return
                bestBoard = workingBoard;
                break;
            }

            // Might be the best we have so far
            if (bestBoard == null || bestBoard.percentageSolved() > workingBoard.percentageSolved())
                bestBoard = workingBoard;
        }
        lockRemaining(bestBoard);
        return bestBoard;
    }

    /**
     * Check if generator can generate a board of given size
     * @param size the size of the board
     * @return true if generator can generate the board.
     */
    public boolean isAcceptedSize(int size) {
        return !(size < 0 || size % 2 == 1 || ((size / 2) - 2) > solvedBoardGenerators.length);
    }

    /**
     * @param size
     * @return a list of all indexes on the board randomly shuffled
     */
    private Queue<Index> buildIndexesToPull(int size) {
        LinkedList<Index> indexes = new LinkedList<>();
        IntStream.range(0, size * size)
                .forEach(i -> indexes.offer(new Index(i / size, i % size)));
        Collections.shuffle(indexes);
        return indexes;
    }

    /**
     * Lock all the tiles remaining after removal.
     * @param board that can be solved
     */
    private void lockRemaining(Board board) {
//        new Index(indexData % board.getSize(), indexData / board.getSize())
        IntStream.range(0, board.getSize() * board.getSize())
                .filter(indexData -> board.getTile(board.getIndex(indexData)) != Tile.EMPTY)
                .forEach(indexData -> board.setLocked((board.getIndex(indexData)), true));

    }

    /**
     * return the board to the state before solved
     * @param empties list of all indexes to be emptied
     * @param board
     */
    private void restoreAfterSolve(Set<Index> empties, Board board) {
        empties.stream().forEach(index -> board.setTile(index, Tile.EMPTY));
    }
}
