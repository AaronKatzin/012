package com.hit.game012.gamelogic.checker;

import static com.hit.game012.gamelogic.game.Tile.*;
import com.hit.game012.gamelogic.game.Index;

import java.util.stream.IntStream;

/**
 * A set of the rules returning a boolean result
 * Used for the BoardGenerator.
 */
public class Rules {
    /**
     * Verifies that no row or column contain 3 consecutive color.
     */
    public static final Rule NO_3_CONSECUTIVE = board ->
            //Check all the rows and cols for 3 consecutive tiles
            IntStream.range(0, board.getSize()).allMatch(index -> {
                int consColor1row = 0;
                int consColor2row = 0;
                int consColor1col = 0;
                int consColor2col = 0;
                for (int i = 0; i < board.getSize(); i++) {
                    // Check rows
                    switch (board.getTile(new Index(index, i))) {
                        case EMPTY:
                            consColor2row = 0;
                            consColor1row = 0;
                            break;
                        case COLOR1:
                            consColor2row++;
                            consColor1row = 0;
                            break;
                        case COLOR2:
                            consColor2row = 0;
                            consColor1row++;
                            break;
                    }
                    // Check cols
                    switch (board.getTile(new Index(i, index))) {
                        case EMPTY:
                            consColor2col = 0;
                            consColor1col = 0;
                            break;
                        case COLOR1:
                            consColor2col++;
                            consColor1col = 0;
                            break;
                        case COLOR2:
                            consColor2col = 0;
                            consColor1col++;
                            break;
                    }
                    if (consColor1col == 3 || consColor1row == 3 || consColor2col == 3 || consColor2row == 3)
                        return false;
                }
                return true;
            });

    /**
     * Verify that the number of colored tiles are equal to each other and to half of the board size
     */
    public static final Rule EQUAL_BLUE_AND_RED = board ->
            IntStream.range(0, board.getSize()).allMatch(index ->
                    board.countTilesInRow(index, COLOR1) <= board.getSize() / 2
                            && board.countTilesInRow(index, COLOR2) <= board.getSize() / 2
                            && board.countTilesInCol(index, COLOR1) <= board.getSize() / 2
                            && board.countTilesInCol(index, COLOR2) <= board.getSize() / 2
            );

    /**
     * Verify that no two rows or columns are the same.
     */
    public static final Rule NO_IDENTICAL_ROWS_OR_COLUMNS = board -> IntStream.range(0, board.getSize())
            .map(row -> IntStream.range(0, board.getSize()).reduce(0, (serRow, col) -> {
        if (board.getTile(new Index(row, col)) == COLOR1) return serRow | (1 << col);
        else return serRow;
    }))
            .distinct().count() == board.getSize() && IntStream.range(0, board.getSize()).map(col -> IntStream.range(0, board.getSize()).reduce(0, (serCol, row) -> {
        if (board.getTile(new Index(row, col)) == COLOR1) return serCol | (1 << col);
        else return serCol;
    })).distinct().count() == board.getSize();
}
