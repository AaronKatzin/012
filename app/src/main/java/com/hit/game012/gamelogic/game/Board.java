package com.hit.game012.gamelogic.game;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Board {
    private Row[] board;
    private Integer size;

    public Board(int size) {
        this.size = size;
        this.board = new Row[size];
        IntStream.range(0, size).forEach(i -> board[i] = new Row(size));
    }

    public Board(@NotNull Board board) {
        this.size = board.size;
        this.board = board.board.clone();
    }


    public Integer getSize() {
        return size;
    }

    private void checkIndexInRange(@NotNull Index index) {
        int row = index.getRow();
        int col = index.getCol();
        if (row < 0)
            throw new IllegalArgumentException("row must be >= 0 but was " + row);
        if (row >= size)
            throw new IllegalArgumentException("row must be < board size (" + size + ") but was " + row);
        if (col < 0)
            throw new IllegalArgumentException("column must be >= 0 but was " + col);
        if (col >= size)
            throw new IllegalArgumentException("column must be < board size (" + size + ") but was " + col);
    }

    public Tile getTile(@NotNull Index index) {
        checkIndexInRange(index);
        return board[index.getRow()].getTile(index.getCol());
    }

    public Tile setTile(@NotNull Index index, Tile newState) {
        checkIndexInRange(index);
        return board[index.getRow()].setTile(index.getCol(), newState);
    }

    public Tile stepTile(@NotNull Index index) {
        checkIndexInRange(index);
        return board[index.getRow()].stepTile(index.getCol());
    }

    public int countTilesInRow(int row, Tile tile) {
        if (row < 0) throw new IllegalArgumentException("row must be >= 0 but was " + row);
        if (row >= size) throw new IllegalArgumentException("row must be < board size (" + size + ") but was " + row);
        return IntStream.range(0, size).reduce(0, (sum, i) -> (board[row].getTile(i) == tile ? sum + 1 : sum));
    }

    public int countTilesInCol(int col, Tile tile) {
        if (col < 0) throw new IllegalArgumentException("column must be >= 0 but was " + col);
        if (col >= size)
            throw new IllegalArgumentException("column must be < board size (" + size + ") but was " + col);
        return IntStream.range(0, size).reduce(0, (sum, i) -> (board[i].getTile(col) == tile ? sum + 1 : sum));
    }

    public boolean isFull() {
        return !IntStream.range(0,size).anyMatch(i -> countTilesInRow(i, Tile.EMPTY) > 0);
    }

    public int percentageSolved() {
        int countEmpty = IntStream.range(0, size).reduce(0, (numEmpty, rowIndex) -> numEmpty + countTilesInRow(rowIndex, Tile.EMPTY));
        int countFull = (size * size) - countEmpty;
        float percentFull = countFull / ((float) (size * size));
        return Math.round(100 * percentFull);
    }

    public void setLocked(Index index, boolean locked){
        checkIndexInRange(index);
        board[index.getRow()].setLocked(index.getCol(), locked);
    }
    public void setAllLocks(boolean locked){
        Arrays.stream(board).forEach(row -> row.setAllLocks(locked));
    }
    public static Board boardFromString(int size, String serializedBoard) {
        Board board = new Board(size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                char ch = serializedBoard.charAt(0);
                board.setTile(new Index(i,j), Tile.deserialize(ch));
                serializedBoard = serializedBoard.substring(1);
            }
        }
        return board;
    }
    public String stringFromBoard(){
        String serialized = "";
        for (Row row : board)
            serialized += row.serialize();
        return serialized;
    }
    public Board copy() {
        Board clone = new Board(size);
        IntStream.range(0, size).forEach(index -> clone.board[index] = board[index].copy());
        return clone;
    }
    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();
        for (Row r : board) {
            text.append(r).append('\n');
        }
        return text.toString();
    }


    public Tile getTileAt(int row, int col) {
        return getTile(new Index(row, col));
    }

    public Tile setTileAt(int row, int col, Tile oppositeColor) {
        return setTile(new Index(row, col), oppositeColor);
    }

    public boolean isLocked(Index index){
        checkIndexInRange(index);
        return board[index.getRow()].isLocked(index.getCol());
    }
}


