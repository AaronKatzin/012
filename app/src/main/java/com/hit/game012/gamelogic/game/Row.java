package com.hit.game012.gamelogic.game;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

public class Row {
    private int size;
    private Cell[] content;

    public Row(int size) {
        this.size = size;
        this.content = new Cell[size];
        IntStream.range(0,size).forEach(i -> content[i] = new Cell());
    }

    public Tile getTile(int index){
        return content[index].state;
    }

    public void setLocked(int index, boolean lock){
        content[index].isLocked = lock;
    }

    public Tile setTile(int index, Tile newState){
        Tile oldState = getTile(index);
        content[index].state = newState;
        return oldState;
    }
    public Tile stepTile(int index){
        return content[index].state = content[index].state.nextState();
    }

    public boolean isLocked(int col){
        return content[col].isLocked();
    }

    /**
     * Helper function to get the row represented by string
     * @return string of chars represented the tiles colors.
     */
    public String serialize(){
        String serialized = "";
        for (Cell c:content){
            serialized += c.state.getSerialized();
        }
        return serialized;
    }

    public Row copy() {
        Row clone = new Row(size);
        IntStream.range(0, size).forEach(index -> {
            clone.content[index].state = content[index].state;
            clone.content[index].isLocked = content[index].isLocked;
        });
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Row row = (Row) o;
        return size == row.size && Arrays.equals(content, row.content);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(content);
        return result;
    }

    @Override
    public String toString() {
        return Arrays.toString(content);
    }
}
