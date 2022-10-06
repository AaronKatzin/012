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

public class BoardGenerator {
    private final int MAX_PRECENTAGE_THRESHOLD = 40;
    private final int MAX_REMOVE_ATTEMPTS = 32;
    private final int MAX_ATTEMPTS_TO_SOLVE = 6;
    private final SolvedBoardGenerator[] solvedBoardGenerators;
    private final Random random = new Random(System.currentTimeMillis());
    private final int[] boardSizes = {4, 6, 8, 10, 12};

    public BoardGenerator() {
        solvedBoardGenerators = new SolvedBoardGenerator[5];
        for (int i = 0; i < boardSizes.length; i++)
            solvedBoardGenerators[i] = new SolvedBoardGenerator(boardSizes[i]);
    }

    private SolvedBoardGenerator getGenerator(int size) {
        if (isAcceptedSize(size))
            return solvedBoardGenerators[size / 2 - 2];
        return null;
    }

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
//            System.out.println("Attempt #"+attempts);
            Board workingBoard = solvedBoard.copy();
            BoardSolver solver = new BoardSolver(workingBoard);
            allIndexes = buildIndexesToPull(size);
            Set<Index> removedIndexes = new HashSet<>();
            int failedAttempts = 0;
            while (!allIndexes.isEmpty() && failedAttempts < MAX_ATTEMPTS_TO_SOLVE) {
//                System.out.println("Failed attempts #"+failedAttempts);
                Index toRemove = allIndexes.poll();
                Tile removedTile = workingBoard.setTile(toRemove, Tile.EMPTY);
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
            if (bestBoard == null || bestBoard.percentageSolved() < workingBoard.percentageSolved())
                bestBoard = workingBoard;


        }

        //TODO: lock the tiles on the board
        lockRemaining(bestBoard);

        return bestBoard;
    }

    /**
     * Check if generator cam generate a board of given size
     *
     * @param size the size of the board
     * @return true if generator can generate the board.
     */
    public boolean isAcceptedSize(int size) {
        return !(size < 0 || size % 2 == 1 || ((size / 2) - 2) > solvedBoardGenerators.length);
    }

    private Queue<Index> buildIndexesToPull(int size) {
        LinkedList<Index> indexes = new LinkedList<>();
        IntStream.range(0, size * size)
                .forEach(i -> indexes.offer(new Index(i % size, i / size)));
        Collections.shuffle(indexes);
        return indexes;
    }

    private void lockRemaining(Board board) {
        IntStream.range(0, board.getSize() * board.getSize())
                .filter(indexData -> board.getTile(new Index(indexData % board.getSize(), indexData / board.getSize())) != Tile.EMPTY)
                .forEach(indexData -> board.setLocked(new Index(indexData % board.getSize(), indexData / board.getSize()), true));

    }

    private void restoreAfterSolve(Set<Index> empties, Board board) {
        empties.stream().forEach(index -> board.setTile(index, Tile.EMPTY));
    }
}


//import game.Board;
//import game.Index;
//import game.Tile;
//import solver.BoardSolver;
//
//import java.util.*;
//import java.util.stream.IntStream;
//
///**
// * Created on 30/10/2015.
// * Generates solvable boards via the {@link BoardGenerator#generate(int)} method.
// */
//public class BoardGenerator {
//    /**
//     * Returned boards cannot be more solved than this.
//     */
//    private static final int MAX_SOLVED_THRESHOLD = 40;
//
//    /**
//     * The max number of attempts to achieve the desired quality.
//     */
//    private static final int ATTEMPTS_FOR_QUALITY = 32;
//
//    /**
//     * The max number of attempts to remove another tile before giving up.
//     */
//    private static final int ATTEMPTS_TO_SOLVE = 6;
//
//    private SolvedBoardGenerator[] solvedBoardGenerators;
//    private Random random = new Random(System.currentTimeMillis());
//
//    public BoardGenerator() {
//        solvedBoardGenerators = new SolvedBoardGenerator[5];
//        IntStream.of(4, 6, 8, 10, 12)
//                .forEach(size -> solvedBoardGenerators[(size / 2) - 2] = new SolvedBoardGenerator(size));
//    }
//
//    private SolvedBoardGenerator getGenerator(int size) {
//        return solvedBoardGenerators[(size / 2) - 2];
//    }
//
//    /**
//     * Check if the generator can generate a board of the given size.
//     * @param size the size to check.
//     * @return true if this generator can generate a board of size {@code size}.
//     */
//    public boolean isAcceptedSize(int size) {
//        return !(size < 0 || size%2 == 1 || ((size / 2) - 2) > solvedBoardGenerators.length);
//    }
//
//    /**
//     * Generate a board of size {@code size}.
//     *
//     * @param size the size of the generated board.
//     * @return a starter puzzle that can be solved with the starting tiles locked.
//     * See  to check if a tile is locked.<br>
//     * See {@link BoardSolver} for a board solver implementation.<br>
//     * @throws IllegalArgumentException if !{@link BoardGenerator#isAcceptedSize(int)}.
//     */
//    public Board generate(int size) {
//        if (!isAcceptedSize(size)) throw new IllegalArgumentException("Invalid size. " + size);
//        //Generate a new solved board.
//        Board solvedBoard = getGenerator(size).generateBoard();
//        //Take pieces away until it is acceptable
//        Board bestBoard = null;
//        Queue<Index> coordsToPull = null;
//        int attempts = 0;
//        while (attempts++ < ATTEMPTS_FOR_QUALITY) {
//            Board workingBoard = solvedBoard.copy();
//            BoardSolver solver = new BoardSolver(workingBoard);
//            coordsToPull = buildCoordsToPull(size);
//            Set<Index> removed = new HashSet<>();
//            int failedAttempts = 0;
//            while(!coordsToPull.isEmpty() && failedAttempts < ATTEMPTS_TO_SOLVE) {
//                Index toErase = coordsToPull.poll();
//                Tile removedTile = workingBoard.setTileAt(toErase.getRow(), toErase.getCol(), Tile.EMPTY);
//                if (solver.tryToSolve()) {
//                    //It is solvable, take all the removed tiles back
//                    removed.add(toErase);
//                    restore(removed, workingBoard);
//                    failedAttempts = 0;
//                } else {
//                    //Not solvable, put the piece back
//                    restore(removed, workingBoard);
//                    workingBoard.setTileAt(toErase.getRow(), toErase.getCol(), removedTile);
//                    failedAttempts++;
//                }
//            }
//            //Is this board good enough to stop?
//            if (workingBoard.percentageSolved() < MAX_SOLVED_THRESHOLD) {
//                //We did it!
//                bestBoard = workingBoard;
//                break;
//            }
//            //Nope, it might be the best we have done though
//            if (bestBoard == null || bestBoard.percentageSolved() < workingBoard.percentageSolved()) {
//                bestBoard = workingBoard;
//            }
//        }
//        lockRemaining(bestBoard);
//        return bestBoard;
//    }
//
//    private Queue<Index> buildCoordsToPull(int size) {
//        LinkedList<Index> coords = new LinkedList<>();
//        IntStream.range(0, size * size)
//                .forEach(coordData -> coords.offer(new Index(coordData % size, coordData / size)));
//        Collections.shuffle(coords);
//        return coords;
//    }
//
//    private void lockRemaining(Board board) {
//        IntStream.range(0, board.getSize() * board.getSize())
//                .filter(coordData -> board.getTileAt(coordData % board.getSize(), coordData / board.getSize()) != Tile.EMPTY)
//                .forEach(coordData -> board.setLockAt(coordData % board.getSize(), coordData / board.getSize(), true));
//    }
//
//    private void restore(Set<Index> empties, Board board) {
//        empties.stream().forEach(coord -> board.setTileAt(coord.getRow(), coord.getCol(), Tile.EMPTY));
//    }
//}
