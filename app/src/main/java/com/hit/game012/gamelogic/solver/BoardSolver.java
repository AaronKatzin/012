package com.hit.game012.gamelogic.solver;

import static com.hit.game012.gamelogic.game.Tile.*;
import static com.hit.game012.gamelogic.game.Tile.EMPTY;

import android.content.res.Resources;

import com.hit.game012.R;
import com.hit.game012.gamelogic.checker.BoardChecker;
import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.game.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * In order to solve a board (no backtracking!), we need to follow these steps:
 * 1. Check for an adjacent same color tile pair - set both ends with opposite color.
 * 2. Check for same color tile pair separated by an empty tile - set middle tile to opposite color.
 * 3. Check if row/col contains all required tiles from same color - rest of the tiles can be set to opposite color.
 * 4. Check unique rows and columns.
 */
public class BoardSolver {
    private Board board;

    public BoardSolver(Board b) {
        this.board = b;
    }

    public Board getBoard() {
        return board;
    }
    public void setBoard(Board board){
        this.board = board.copy();
    }

    /**
     * Iteratively try to place a tile in the board.
     * @return true if board is solvable, false otherwise
     */
    public boolean tryToSolve() {
        while (!board.isFull()) {
            if (placeTile() == null)
                // The board is unsolvable
                return false;
        }
        return true;
    }

    /**
     * @return Hint that holds a message and the involved tiles
     */
    public Hint requestHint() {
        if (board.isFull()) {
            return new Hint(R.string.hint_board_full, null);
        }
        List<Index> involvedTiles = new ArrayList<>();
        Index index = findEmptyNextToTwoAdjacentTile(false);
        if (index != null) {
            involvedTiles.add(index);
            return new Hint(R.string.hint_3_color, involvedTiles);
        }
        index = findEmptyBetweenTwoSeperatedTiles(false);
        if (index != null) {
            involvedTiles.add(index);
            return new Hint(R.string.hint_3_color, involvedTiles);
        }
        index = findTilesByColCount(false);
        if (index != null) {
            int col = index.getCol();
            IntStream.range(0, board.getSize()).forEach(row -> involvedTiles.add(new Index(row, col)));
            return new Hint(R.string.hint_col_count, involvedTiles);
        }
        index = findTilesByRowCount(false);
        if (index != null) {
            int row = index.getRow();
            IntStream.range(0, board.getSize()).forEach(col -> involvedTiles.add(new Index(row, col)));
            return new Hint(R.string.hint_row_count, involvedTiles);
        }
        Index[] differentColorCells = findUniqueCol(false);
        if (differentColorCells != null) {
            int col = differentColorCells[0].getCol();
            int matchingCol = differentColorCells[1].getCol();
            IntStream.range(0, board.getSize()).forEach(row ->{
                involvedTiles.add(new Index (row, col));
                involvedTiles.add(new Index (row, matchingCol));
            });
            return new Hint(R.string.hint_unique_col, involvedTiles);
        }
        differentColorCells = findUniqueRow(false);
        if (differentColorCells != null) {
            int row = differentColorCells[0].getRow();
            int matchingRow = differentColorCells[1].getRow();
            IntStream.range(0, board.getSize()).forEach(col ->{
                involvedTiles.add(new Index (row, col));
                involvedTiles.add(new Index (matchingRow, col));
            });
            return new Hint(R.string.hint_unique_row, involvedTiles);
        }
        return new Hint(R.string.hint_dead_end, null);
    }

    /**
     * This function attempts to place a tile in the board.
     * If board is not valid, the function may still place a tile that satisfied some rules.
     * @return Index of the placed tile or null if not found.
     */
    public Index placeTile() {
        Index tilePlaced;
        if ((tilePlaced = findEmptyNextToTwoAdjacentTile(true)) != null)
            return tilePlaced;
        if ((tilePlaced = findEmptyBetweenTwoSeperatedTiles(true)) != null)
            return tilePlaced;
        if ((tilePlaced = findTilesByRowCount(true)) != null)
            return tilePlaced;
        if ((tilePlaced = findTilesByColCount(true)) != null)
            return tilePlaced;
        Index[] diffTiles;
        if ((diffTiles = findUniqueRow(true)) != null)
            return diffTiles[0];
        if ((diffTiles = findUniqueCol(true)) != null)
            return diffTiles[0];
        return null;
    }

    /**
     * Check for empty tile next to two adjacent same color tiles
     * @param toPlace boolean to determine if the found index should be colored in the board
     * @return the involved index
     */
    private Index findEmptyNextToTwoAdjacentTile(boolean toPlace) {
        Tile tile1, tile2;
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Index index = new Index(row, col);
                if (board.getTile(index) == EMPTY) {
                    // Empty tile -> checking adjacent tile to find a pair.
                    if (col >= 2) {
                        // Check pair to the right
                        tile1 = board.getTile(new Index(row, col - 1));
                        tile2 = board.getTile(new Index(row, col - 2));
                        if (tile1 != EMPTY && tile1 == tile2) {
                            // Found a pair
                            if (toPlace)
                                board.setTile(index, tile1.oppositeColor());
                            return index;
                        }
                    }

                    if (col < board.getSize() - 2) {
                        // Check pair to the left
                        tile1 = board.getTile(new Index(row, col + 1));
                        tile2 = board.getTile(new Index(row, col + 2));
                        if (tile1 != EMPTY && tile1 == tile2) {
                            // Found a pair
                            if (toPlace)
                                board.setTile(index, tile1.oppositeColor());
                            return index;
                        }
                    }
                    if (row >= 2) {
                        // Check pair to the top
                        tile1 = board.getTile(new Index(row - 1, col));
                        tile2 = board.getTile(new Index(row - 2, col));
                        if (tile1 != EMPTY && tile1 == tile2) {
                            // Found a pair
                            if (toPlace)
                                board.setTile(index, tile1.oppositeColor());
                            return index;
                        }
                    }
                    if (row < board.getSize() - 2) {
                        // Check pair to the left
                        tile1 = board.getTile(new Index(row + 1, col));
                        tile2 = board.getTile(new Index(row + 2, col));
                        if (tile1 != EMPTY && tile1 == tile2) {
                            // Found a pair
                            if (toPlace)
                                board.setTile(index, tile1.oppositeColor());
                            return index;
                        }
                    }
                }
            }
        }
        return null;
    }
    /**
     * Check for empty tile between two same color tiles
     * @param toPlace boolean to determine if the found index should be colored in the board
     * @return the involved index
     */
    private Index findEmptyBetweenTwoSeperatedTiles(boolean toPlace) {
        Tile tile1, tile2;
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                Index index = new Index(row, col);
                if (board.getTile(index) == EMPTY) {
                    // Empty tile -> Checking both sides
                    if (col >= 1 && col < board.getSize() - 1) {
                        tile1 = board.getTile(new Index(row, col - 1));
                        tile2 = board.getTile(new Index(row, col + 1));
                        if (tile1 != EMPTY && tile1 == tile2) {
                            // Found an empty tile between 2 of the same color
                            if (toPlace)
                                board.setTile(index, tile1.oppositeColor());
                            return index;
                        }
                    }

                    if (row >= 1 && row < board.getSize() - 1) {
                        tile1 = board.getTile(new Index(row - 1, col));
                        tile2 = board.getTile(new Index(row + 1, col));
                        if (tile1 != EMPTY && tile1 == tile2) {
                            // Found an empty tile between 2 of the same color
                            if (toPlace)
                                board.setTile(index, tile1.oppositeColor());
                            return index;
                        }
                    }

                }
            }
        }
        return null;
    }
    /**
     * Check for two rows have two empty tiles in the same columns and rest of the tiles are the same
     * @param toPlace boolean to determine if the found index should be colored in the board
     * @return the involved indexes
     */
    private Index[] findUniqueRow(boolean toPlace) {
        for (int row = 0; row < this.board.getSize(); row++) {
            //We can only solve 2 empties
            if (this.board.countTilesInRow(row, EMPTY) != 2) continue;
            //Look for a similar row
            ComparingRowLoop:
            for (int comparingRow = 0; comparingRow < this.board.getSize(); comparingRow++) {
                if (row == comparingRow) continue;
                if (this.board.countTilesInRow(comparingRow, EMPTY) > 0) continue; //We can compare to a full row only
                int firstEmpty = -1;
                for (int column = 0; column < this.board.getSize(); column++) {
                    if (this.board.getTileAt(row, column) == EMPTY) {
                        if (firstEmpty == -1) firstEmpty = column; //Found the first empty tile in the row
                    } else {
                        if (this.board.getTileAt(row, column) != this.board.getTileAt(comparingRow, column))
                            continue ComparingRowLoop;
                    }
                }
                //We know firstEmpty will have the correct value now because there are 2 empties in the row
                if (toPlace) board.setTileAt(row, firstEmpty, this.board.getTileAt(comparingRow, firstEmpty).oppositeColor());
                return new Index[]{ new Index(row, firstEmpty), new Index(comparingRow, firstEmpty) };
            }
        }
        return null;
    }
    /**
     * Check for two cols have two empty tiles in the same rows and rest of the tiles are the same
     * @param toPlace boolean to determine if the found index should be colored in the board
     * @return the involved indexes
     */
    private Index[] findUniqueCol(boolean toPlace) {
        for (int column = 0; column < this.board.getSize(); column++) {
            //We can only solve 2 empties
            if (this.board.countTilesInCol(column, EMPTY) != 2) continue;
            //Look for a similar column
            ComparingColumnLoop:
            for (int comparingColumn = 0; comparingColumn < this.board.getSize(); comparingColumn++) {
                if (column == comparingColumn) continue;
                if (this.board.countTilesInCol(comparingColumn, EMPTY) > 0) continue; //We can compare to a full column only
                int firstEmpty = -1;
                for (int row = 0; row < this.board.getSize(); row++) {
                    if (this.board.getTileAt(row, column) == EMPTY) {
                        if (firstEmpty == -1) firstEmpty = row; //Found the first empty tile in the column
                    } else {
                        if (this.board.getTileAt(row, column) != this.board.getTileAt(row, comparingColumn))
                            continue ComparingColumnLoop;
                    }
                }
                //We know firstEmpty will have the correct value now because there are 2 empties in the column
                if (toPlace) board.setTileAt(firstEmpty, column, this.board.getTileAt(firstEmpty, comparingColumn).oppositeColor());
                return new Index[]{ new Index(firstEmpty, column), new Index(firstEmpty, comparingColumn)};
            }
        }
        return null;
    }
    /**
     * Check for column with complete amount of tiles in same color
     * @param toPlace boolean to determine if the found index should be colored in the board
     * @return the involved index
     */
    private Index findTilesByColCount(boolean toPlace) {
        int color1Counter = 0, color2Counter = 0;
        for (int col = 0; col < board.getSize(); col++) {
            color1Counter = board.countTilesInCol(col, Tile.COLOR1);
            color2Counter = board.countTilesInCol(col, Tile.COLOR2);
            if (color1Counter != color2Counter) {
                if (color1Counter == board.getSize() / 2) {
                    // All color1 tiles are colored
                    for (int row = 0; row < board.getSize(); row++) {
                        Index index = new Index(row, col);
                        if (board.getTile(index) == EMPTY) {
                            if (toPlace)
                                board.setTile(index, Tile.COLOR2);
                            return index;
                        }
                    }
                } else if (color2Counter == board.getSize() / 2) {
                    // All color2 tiles are colored
                    for (int row = 0; row < board.getSize(); row++) {
                        Index index = new Index(row, col);
                        if (board.getTile(index) == EMPTY) {
                            if (toPlace)
                                board.setTile(index, Tile.COLOR1);
                            return index;
                        }
                    }
                }
            }
        }
        return null;
    }
    /**
     * Check for rows with complete amount of tiles in same color
     * @param toPlace boolean to determine if the found index should be colored in the board
     * @return the involved index
     */
    private Index findTilesByRowCount(boolean toPlace) {
        int color1Counter = 0, color2Counter = 0;
        for (int row = 0; row < board.getSize(); row++) {
            color1Counter = board.countTilesInRow(row, Tile.COLOR1);
            color2Counter = board.countTilesInRow(row, Tile.COLOR2);
            if (color1Counter != color2Counter) {
                if (color1Counter == board.getSize() / 2) {
                    // All color1 tiles are colored
                    for (int col = 0; col < board.getSize(); col++) {
                        Index index = new Index(row, col);
                        if (board.getTile(index) == EMPTY) {
                            if (toPlace)
                                board.setTile(index, Tile.COLOR2);
                            return index;
                        }
                    }
                } else if (color2Counter == board.getSize() / 2) {
                    // All color2 tiles are colored
                    for (int col = 0; col < board.getSize(); col++) {
                        Index index = new Index(row, col);
                        if (board.getTile(index) == EMPTY) {
                            if (toPlace)
                                board.setTile(index, Tile.COLOR1);
                            return index;
                        }
                    }
                }
            }
        }
        return null;
    }


}
