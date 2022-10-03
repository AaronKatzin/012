package com.hit.game012.gamelogic.game;

public class Index {
    private Integer row;
    private Integer col;

    public Index(Integer row, Integer col) {
        if (row<0) throw new IllegalArgumentException("row must be > 0, but was " + row);
        if (col<0) throw new IllegalArgumentException("col must be > 0, but was " + col);
        this.row = row;
        this.col = col;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    @Override
    public String toString() {
        return "Index{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    public Integer getCol() {
        return col;
    }

    public void setCol(Integer col) {
        this.col = col;
    }
}
