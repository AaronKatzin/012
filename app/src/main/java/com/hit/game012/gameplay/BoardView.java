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
import androidx.recyclerview.widget.GridLayoutManager;

import com.hit.game012.R;
import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.game.Tile;
import com.hit.game012.gamelogic.solver.BoardSolver;
import com.hit.game012.gamelogic.solver.Hint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Class BoardView is the visual representation of the board.
 * It extends Fragment class in order to be reused in multiple activities.
 */
public class BoardView extends Fragment {
    private static Board board;
    private static Stack<Move> moves;
    private BoardSolver boardSolver;
    private GridView mGridView;
    private GridAdapter adapter;
    private Set<Index> highlightedIndexes;

    public BoardView(Board board) {
        BoardView.board = board;
        moves = new Stack<>();
        boardSolver = new BoardSolver(board);
        highlightedIndexes = new HashSet<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_board_fragment, container, false);
        mGridView = view.findViewById(R.id.board);
        initGrid(view.getContext(), board.getSize());
        return view;
    }

    /**
     * Init the grid and set adapter
     * @param context
     * @param size
     */
    private void initGrid(Context context, int size){
        mGridView.setNumColumns(size);
        adapter = new GridAdapter(context, board, getActivity());
        mGridView.setAdapter(adapter);

        // spacing between the tiles
        int spacing;
        switch (size){
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
    }

    public static Board getBoard() {
        return board;
    }

    /**
     * Function to request hint from solver and to highlight the involved tiles
     * @return id of the hint message
     */
    public int requestHint() {
        highlightedIndexes.clear();
        boardSolver.setBoard(board);
        Hint hint = boardSolver.requestHint();
        List<Index> involvedTiles = hint.getInvolvedTiles();
        if (involvedTiles != null) {
            highlightedIndexes.addAll(involvedTiles);
        }
        highlightHintTiles(involvedTiles);
        adapter.setInGameMessageChanged(true);
        return hint.getMessage();
    }

    private void highlightHintTiles(List<Index> highlighted) {
        if (highlighted != null) {
            adapter.setHighlightedTiles(highlighted);
//            mGridView.invalidateViews();
            adapter.notifyDataSetInvalidated();
        }
    }

    /**
     * Add move to move stack
     * @param move
     */
    public static void addToMoveStack(Move move) {
        long time = System.currentTimeMillis();
        if (!moves.empty()) {
            Move lastMove = moves.pop();
            long delay = 1000; // 1 Second
            // Check if last move was part of double click
            if (!lastMove.getIndex().equals(move.getIndex()) || (time - lastMove.getTime()) > delay) {
                moves.push(lastMove);
            }
        }
        moves.push(move);
    }

    /**
     * Function to undo the last move
     * @return false if no moves were made, true otherwise
     */
    public boolean undo(){
        // Moves stack empty means no moves was made
        if (moves.empty()){
            adapter.setInGameMessageChanged(true);
            return false;
        }
        char color;
        Move lastMove = moves.pop();
        if (moves.contains(lastMove)){
            color = moves.get(moves.indexOf(lastMove)).getColor();
        }
        else
            color = Tile.EMPTY.getSerialized();

        board.setTile(lastMove.getIndex(),Tile.deserialize(color));
//        mGridView.invalidateViews();
        adapter.notifyDataSetInvalidated();
        return true;
    }

}
