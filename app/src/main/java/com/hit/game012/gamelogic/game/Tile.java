package com.hit.game012.gamelogic.game;

public enum Tile {
    EMPTY('e'),
    COLOR1('a'),
    COLOR2('b');
    private char serial;

    Tile(char serial) {
        this.serial = serial;
    }

    /**
     * @return the opposite color of the tile.
     * empty cells opposite is null.
     */
    public Tile oppositeColor() {
        switch (this) {
            case COLOR1:
                return COLOR2;
            case COLOR2:
                return COLOR1;
            default:
                return null;
        }
    }

    /**
     * @return the next state of the tile after being clicked.
     */
    public Tile nextState() {
        switch (this) {
            case EMPTY:
                return COLOR1;
            case COLOR1:
                return COLOR2;
            default:
                return EMPTY;
        }
    }

    public char getSerial() {
        return serial;
    }

    /**
     * @param serialize - char representing a tile color
     * @return new Tile from char
     */
    public static Tile deserialize(char serialize) {
        switch (serialize) {
            case 'e':
                return Tile.EMPTY;
            case 'a':
                return Tile.COLOR1;
            case 'b':
                return Tile.COLOR2;
            default:
                return null;
        }
    }
}
