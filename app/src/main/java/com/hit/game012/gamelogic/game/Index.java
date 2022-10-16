package com.hit.game012.gamelogic.game;

import java.util.Objects;

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
    public Integer getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "Index{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Index index = (Index) o;
        return row.equals(index.row) && col.equals(index.col);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
