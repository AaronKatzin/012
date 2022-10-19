package com.hit.game012.gamelogic.generator;

import com.hit.game012.gamelogic.checker.Rules;
import com.hit.game012.gamelogic.game.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Generates solved boards, using binary operations.
 * Each solved board returned in consecutive calls from the same instance
 * is unique from the last CHECK_UNIQUE boards returned.
 */
public class SolvedBoardGenerator {
    public static final int CHECK_UNIQUE = 8;
    private int size;
    private Queue<Board> previousBoards;

    public SolvedBoardGenerator(int size) {
        this.size = size;
        previousBoards = new LinkedList<>();
    }

    /**
     * Generates a solved board of the size of this generator.
     *
     * @return the generated board.
     */
    public Board generateBoard() {
        Board board = new Board(size);
        // Get all the valid integers that represents a binary row
        LinkedList<Integer> validRows = generateRows();
        Collections.shuffle(validRows);

        int rowIndex = 0;
        int[] intInRow = new int[size]; // final board representation by rows
        int[] attempts = new int[size]; // number of the attempts to solve board with current row

        // While the board is not full
        do {
            attempts[rowIndex]++;
            // Get the first binary row from queue and check if board is still valid
            int row = validRows.poll();
            setRow(rowIndex, row, board);
            if (isValid(board)) {
                // board is still valid
                intInRow[rowIndex] = row;
                rowIndex++;
            } else {
                // if not valid, return row to queue and empty the row in board.
                validRows.offer(row);
                setEmptyRow(rowIndex, board);

                // Check if no more options are left - if so, clear the board and start over.
                if (attempts[rowIndex] >= validRows.size()) {
                    // We tried all options in the list
                    attempts[rowIndex] = 0;
                    for (int clearRowIndex = 1; clearRowIndex < rowIndex; clearRowIndex++) {
                        if (intInRow[clearRowIndex] != 0) {
                            validRows.offer(intInRow[clearRowIndex]);
                            intInRow[clearRowIndex] = 0;
                        }
                        setEmptyRow(clearRowIndex, board);
                        attempts[clearRowIndex] = 0;
                    }
                    rowIndex = 1;
                }
            }
        }
        while (rowIndex < size);

        // When grid is completed - add the board to previous board list.
        previousBoards.offer(board);

        if (previousBoards.size() > CHECK_UNIQUE)
            // Remove the first added board if greater than CHECK_UNIQUE.
            previousBoards.poll();
        return board;
    }

    /**
     * Generates all the valid combinations of binary numbers with "size" bits.
     *
     * @return a set of valid integers.
     */
    private LinkedList<Integer> generateRows() {
        return IntStream.range(0, (int) Math.pow(2, size))
                .filter(this::isValidRow)
                .boxed()
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Check if vector is valid
     *
     * @param vector = integer representing a row
     * @return true if vector is valid, false otherwise
     */
    private boolean isValidRow(int vector) {
        //Get all the coordinates of the 1's in the binary vector
        int[] ones = IntStream.range(0, size)
                .sequential()
                .filter(bit -> {
                    int shiftFactor = size - 1 - bit;
                    return (vector & (1 << shiftFactor)) != 0;
                })
                .toArray();

        //Get all the coordinates of the 0's in the binary vector
        int[] zeros = IntStream.range(0, size)
                .sequential()
                .filter(bit -> {
                    int shiftFactor = size - 1 - bit;
                    return (vector & (1 << shiftFactor)) == 0;
                })
                .toArray();

        // Check if the correct amount of 1 and 0 are in the vector
        if (ones.length != size / 2 || zeros.length != size / 2) return false;

        // Ensure that there are a max of two 0 between 1
        int previousBit = -1;
        for (int bit : ones) {
            if (bit - previousBit > 3) return false;
            previousBit = bit;
        }

        //Ensure that there are a max of two 1 between 0
        previousBit = -1;
        for (int bit : zeros) {
            if (bit - previousBit > 3) return false;
            previousBit = bit;
        }

        return true;
    }

    /**
     * Check if the board is still valid and not in previousBoards (unique board)
     *
     * @param board
     * @return true if valid, false otherwise
     */
    private boolean isValid(Board board) {
        return Rules.EQUAL_COLOR_COUNT.check(board)
                && Rules.NO_3_CONSECUTIVE.check(board)
                && (!board.isFull() || Rules.NO_IDENTICAL_ROWS_OR_COLUMNS.check(board))
                && !previousBoards.contains(board);
    }

    /**
     * Set color tiles in board of rowIndex.
     *
     * @param rowIndex
     * @param rowContents integer representation of valid board row.
     * @param board
     */
    private void setRow(int rowIndex, int rowContents, Board board) {
        IntStream.range(0, size).forEach(columnIndex ->
                board.setTile(new Index(rowIndex, columnIndex), (rowContents & (1 << (size - 1 - columnIndex))) == 0 ? Tile.COLOR1 : Tile.COLOR2));
    }

    /**
     * Empty the row in the board.
     *
     * @param rowIndex
     * @param board
     */
    private void setEmptyRow(int rowIndex, Board board) {
        IntStream.range(0, size).forEach(columnIndex -> board.setTile(new Index(rowIndex, columnIndex), Tile.EMPTY));
    }
}