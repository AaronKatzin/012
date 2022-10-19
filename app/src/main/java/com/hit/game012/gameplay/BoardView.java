package com.hit.game012.gameplay;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hit.game012.R;
import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.game.Tile;
import com.hit.game012.gamelogic.solver.BoardSolver;
import com.hit.game012.gamelogic.solver.Hint;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * BoardView controls the view of the board.
 * Holds a GridView object to represent the board instance graphically.
 * The class maintains a Move Stack to support undo.
 */
public class BoardView extends Fragment {
    private static Board board;
    private BoardSolver boardSolver;
    private static Stack<Move> moves;
    private Set<Index> highlightedIndexes;
    private GridView mGridView;
    private GridAdapter adapter;


    public GridView getmGridView() {
        return mGridView;
    }

    public static Board getBoard() {
        return board;
    }

    public BoardView(Board board) {
        this.board = board;
        boardSolver = new BoardSolver(board);
        highlightedIndexes = new HashSet<>();
        moves = new Stack<>();

    }

    /**
     * Request hint from Solver, highlights the involved tiles and shows the hint message.
     *
     * @return resID of the current hint
     */
    public int requestHint() {
        boardSolver.setBoard(board);
        Hint hint = boardSolver.requestHint();
        List<Index> involvedTiles = hint.getInvolvedTiles();
        highlightedIndexes.clear();

        if (involvedTiles != null) {
            highlightedIndexes.addAll(involvedTiles);
        }
        highlightHintTiles(involvedTiles);
        adapter.setInGameMessageChanged(true);
        return hint.getMessage();
    }

    /**
     * Function to highlight all tiles in list
     *
     * @param highlighted list of Tiles to be highlighted.
     */
    private void highlightHintTiles(List<Index> highlighted) {
        if (highlighted != null) {
            adapter.setHighlightedTiles(highlighted);
            mGridView.invalidateViews();
        }
    }

    /**
     * Adds a Move to the stack.<br>
     * Each move contains a timestamp in order to handle quick tile switches (second color),
     * If two moves made in the DELAY timeframe, removes the first move from stack.
     *
     * @param move most recent move
     */
    public static void addToMoveStack(Move move) {
        long time = System.currentTimeMillis();
        if (!moves.empty()) {
            Move lastMove = moves.pop();
            long DELAY = 1000; // 1 Second
            if (!lastMove.getIndex().equals(move.getIndex()) || (time - lastMove.getTime()) > DELAY) {
                moves.push(lastMove);
            }
        }
        moves.push(move);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_board_fragment, container, false);
        mGridView = view.findViewById(R.id.board_gridview);
        initGrid(view.getContext(), board.getSize());
        return view;
    }

    /**
     * Initialize the grid and set the adapter
     *
     * @param context of the activity
     * @param size board size
     */
    private void initGrid(Context context, int size) {
        adapter = new GridAdapter(context, board, getActivity());
        mGridView.setAdapter(adapter);

        int spacing;
        switch (size) {
            case 4:
                spacing = 7;
                break;
            case 6:
                spacing = 6;
                break;
            default:
                spacing = 5;
        }

        mGridView.setVerticalSpacing(spacing);
        mGridView.setHorizontalSpacing(spacing);
        mGridView.setNumColumns(size);
    }

    /**
     * Restore the last move in the stack.
     *
     * @return true if undo succeeded, false otherwise.
     */
    public boolean undo() {
        if (moves.empty()) {
            // No moves made
            adapter.setInGameMessageChanged(true);
            return false;
        }

        char color;
        Move lastMove = moves.pop();
        if (moves.contains(lastMove)) {
            // The tile was pressed before - return the color to last color
            color = moves.get(moves.indexOf(lastMove)).getColor();
        } else
            // The tile was not pressed before - empty the tile
            color = Tile.EMPTY.getSerial();

        board.setTile(lastMove.getIndex(), Tile.deserialize(color));
        adapter.notifyDataSetChanged();
        return true;
    }

    public static Stack<Move> getMoves() {
        return moves;
    }

}
