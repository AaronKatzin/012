package com.hit.game012.gamelogic.checker;

import static com.hit.game012.gamelogic.checker.CheckResult.State.*;
import static com.hit.game012.gamelogic.game.Tile.*;
import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.game.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Random;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class Rules {
    /**
     * Random to give the checker a random checking start point.
     */
    private static Random r = new Random();
    public static final Rule NO_EMPTY_TILES = board -> {
        List<Index> emptyTiles = new ArrayList<>();
        IntStream.range(0, board.getSize()).forEach(row ->
                IntStream.range(0, board.getSize()).forEach(col -> {
                    Index index = new Index(row, col);
                    if (board.getTile(index) == Tile.EMPTY)
                        emptyTiles.add(index);
                })
        );
        if (emptyTiles.isEmpty())
            return new CheckResult(SATISFIED, "No empty tiles", null);
        else
            return new CheckResult(VIOLATES, "Board contains empty tiles", emptyTiles);
    };
    public static final Rule EQUAL_TILE_COUNT_ROW = board -> {
        boolean[] problemRows = new boolean[board.getSize()];
        IntStream.range(0, board.getSize()).forEach(row -> problemRows[row] =
                board.countTilesInRow(row, COLOR1) > board.getSize() / 2
                        || board.countTilesInRow(row, COLOR2) > board.getSize() / 2);
        final int selection = r.nextInt(board.getSize());
        final OptionalInt problemRow = IntStream.range(selection, selection + board.getSize())
                .filter(i -> problemRows[i % board.getSize()])
                .findFirst();
        if (!problemRow.isPresent())
            return new CheckResult(SATISFIED, "Rows have successful or potentially successful sums", null);

        List<Index> problemIndex = new ArrayList<>();
        int row = problemRow.getAsInt() % board.getSize();
        IntStream.range(0, board.getSize())
                .forEach(column -> problemIndex.add(new Index(row, column)));
        return new CheckResult(VIOLATES, "Row " + (row + 1) + ": Incorrect tile sum", problemIndex);
    };
    public static final Rule EQUAL_TILE_COUNT_COL = board -> {
        boolean[] problemCols = new boolean[board.getSize()];
        IntStream.range(0, board.getSize()).forEach(col -> problemCols[col] =
                board.countTilesInCol(col, COLOR1) > board.getSize() / 2
                        || board.countTilesInCol(col, COLOR2) > board.getSize() / 2);
        final int selection = r.nextInt(board.getSize());
        final OptionalInt problemCol = IntStream.range(selection, selection + board.getSize())
                .filter(i -> problemCols[i % board.getSize()])
                .findFirst();
        if (!problemCol.isPresent())
            return new CheckResult(SATISFIED, "Cols have successful or potentially successful sums", null);

        List<Index> problemIndex = new ArrayList<>();
        int col = problemCol.getAsInt() % board.getSize();
        IntStream.range(0, board.getSize())
                .forEach(column -> problemIndex.add(new Index(col, column)));
        return new CheckResult(VIOLATES, "Col " + (col + 1) + ": Incorrect tile sum", problemIndex);
    };
    public static final Rule MAX_2_CONSECUTIVE_ROW = board -> {
        int start = r.nextInt(board.getSize());
        Optional<List<Index>> problemRow = IntStream.range(start, board.getSize() + start)
                .mapToObj((IntFunction<List<Index>>) row -> {
                    row = row % board.getSize();
                    List<Index> consecutiveColor1 = new ArrayList<>();
                    List<Index> consecutiveColor2 = new ArrayList<>();
                    for (int col = 0; col < board.getSize(); col++) {
                        Index index = new Index(row, col);
                        Tile tile = board.getTile(index);
                        switch (tile) {
                            case EMPTY:
                                if (consecutiveColor1.size() > 2) return consecutiveColor1;
                                else if (consecutiveColor2.size() > 2) return consecutiveColor2;
                                consecutiveColor1.clear();
                                consecutiveColor2.clear();
                            case COLOR1:
                                if (consecutiveColor2.size() > 2) return consecutiveColor2;
                                consecutiveColor1.add(index);
                                consecutiveColor2.clear();
                            case COLOR2:
                                if (consecutiveColor1.size() > 2) return consecutiveColor1;
                                consecutiveColor2.add(index);
                                consecutiveColor1.clear();
                        }
                    }
                    return new ArrayList<>();
                })
                .filter(list -> list.isEmpty())
                .findFirst();
        if (!problemRow.isPresent())
            return new CheckResult(SATISFIED, "No row contains more than 2 consecutive tiles", null);
        Index badTile = problemRow.get().stream().findAny().get();
        Tile tile = board.getTile(badTile);
        return new CheckResult(VIOLATES, "Row " + badTile.getRow() + ": Too many consecutive "
                + tile.name().toLowerCase() + "tiles.", problemRow.get());
    };
    public static final Rule MAX_2_CONSECUTIVE_COL = board -> {
        int start = r.nextInt(board.getSize());
        Optional<List<Index>> problemCol = IntStream.range(start, board.getSize() + start)
                .mapToObj((IntFunction<List<Index>>) col -> {
                    col = col % board.getSize();
                    List<Index> consecutiveColor1 = new ArrayList<>();
                    List<Index> consecutiveColor2 = new ArrayList<>();
                    for (int row = 0; row < board.getSize(); row++) {
                        Index index = new Index(row, col);
                        Tile tile = board.getTile(index);
                        switch (tile) {
                            case EMPTY:
                                if (consecutiveColor1.size() > 2) return consecutiveColor1;
                                else if (consecutiveColor2.size() > 2) return consecutiveColor2;
                                consecutiveColor1.clear();
                                consecutiveColor2.clear();
                            case COLOR1:
                                if (consecutiveColor2.size() > 2) return consecutiveColor2;
                                consecutiveColor1.add(index);
                                consecutiveColor2.clear();
                            case COLOR2:
                                if (consecutiveColor1.size() > 2) return consecutiveColor1;
                                consecutiveColor2.add(index);
                                consecutiveColor1.clear();
                        }
                    }
                    return new ArrayList<>();
                })
                .filter(list -> list.isEmpty())
                .findFirst();
        if (!problemCol.isPresent())
            return new CheckResult(SATISFIED, "No col contains more than 2 consecutive tiles", null);
        System.out.println(Arrays.toString(problemCol.get().toArray()));
        Index badTile = problemCol.get().stream().findAny().get();
        Tile tile = board.getTile(badTile);
        return new CheckResult(VIOLATES, "Col " + badTile.getRow() + ": Too many consecutive "
                + tile.name().toLowerCase() + "tiles.", problemCol.get());
    };
    public static final Rule NO_IDENTICAL_ROWS = board -> {
        int startingRow = r.nextInt(board.getSize());
        OptionalLong rowPair = IntStream.range(startingRow, startingRow + board.getSize())
                .map(i -> i % board.getSize())
                .mapToLong((row) -> {
                    int startingCompRow = r.nextInt(board.getSize());
                    OptionalInt matchingRow = IntStream.range(startingCompRow, startingCompRow + board.getSize())
                            .map(i -> i % board.getSize())
                            .filter(compRow ->
                                    IntStream.range(0, board.getSize()).allMatch(column -> row != compRow
                                            && board.getTile(new Index(row, column)) != EMPTY
                                            && board.getTile(new Index(row, column)) == board.getTile(new Index(compRow, column)))
                            ).findFirst();
                    if (matchingRow.isPresent() && matchingRow.getAsInt() != row)
                        return ((long) row) << 32 | matchingRow.getAsInt();
                    else return Long.MIN_VALUE;
                }).filter(pair -> pair != Long.MIN_VALUE).findFirst();
        if (!rowPair.isPresent())
            return new CheckResult(SATISFIED, "No identical rows", null);

        int row1 = (int) (rowPair.getAsLong() >> 32);
        int row2 = (int) (rowPair.getAsLong() & 0xFFFF);
        List<Index> index = new ArrayList<>();
        IntStream.range(0, board.getSize()).forEach(column -> {
            index.add(new Index(row1, column));
            index.add(new Index(row2, column));
        });
        return new CheckResult(VIOLATES, String.format("Row %d & Row %d: Identical", row1+1, row2+1), index);
    };
    public static final Rule NO_IDENTICAL_COLS = board -> {
        int startingColumn = r.nextInt(board.getSize());
        OptionalLong columnPair = IntStream.range(startingColumn, startingColumn + board.getSize())
                .map(i -> i % board.getSize())
                .mapToLong((column) -> {
                    int startingCompColumn = r.nextInt(board.getSize());
                    OptionalInt matchingColumn = IntStream.range(startingCompColumn, startingCompColumn + board.getSize())
                            .map(i -> i%board.getSize())
                            .filter(compColumn ->
                                    IntStream.range(0, board.getSize()).allMatch(row -> column != compColumn
                                            && board.getTile(new Index(row, column)) != EMPTY
                                            && board.getTile(new Index(row, column)) == board.getTile(new Index(row, compColumn)))
                            ).findFirst();
                    if (matchingColumn.isPresent() && matchingColumn.getAsInt() != column)
                        return ((long) column)<<32 | matchingColumn.getAsInt();
                    else return Long.MIN_VALUE;
                }).filter(pair -> pair != Long.MIN_VALUE).findFirst();
        if (!columnPair.isPresent()) return new CheckResult(SATISFIED, "No identical columns", null);
        int column1 = (int) (columnPair.getAsLong() >> 32);
        int column2 = (int) (columnPair.getAsLong() & 0xFFFF);
        List<Index> index = new ArrayList<>();
        IntStream.range(0, board.getSize()).forEach(row -> {
            index.add(new Index(row, column1));
            index.add(new Index(row, column2));
        });
        return new CheckResult(VIOLATES, String.format("Column %d & Column %d: Identical", column1+1, column2+1), index);
    };


}
