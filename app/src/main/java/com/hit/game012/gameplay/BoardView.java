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


public class BoardView extends Fragment {
    private static Board board;
    private BoardSolver boardSolver;
    private static Timer timer;
    private static Stack<Move> moves;
    private Set<Index> highlightedIndexes;
    private GridView mGridView;
    private GridAdapter adapter;
    List<View> allTileView;


    public static Board getBoard() {
        return board;
    }

    public BoardView(Board board) {
        this.board = board;

        boardSolver = new BoardSolver(board);
        timer = new Timer();

        highlightedIndexes = new HashSet<>();
        moves = new Stack<>();

    }

    public void startGame() {
        timer.start();
        

    }

    public int requestHint() {
        boardSolver.setBoard(board);
        Hint hint = boardSolver.requestHint();
        List<Index> involvedTiles = hint.getInvolvedTiles();
        highlightedIndexes.clear();
        if (involvedTiles != null) {
            highlightedIndexes.addAll(involvedTiles);
            System.out.println("Highlighted indexes: " + highlightedIndexes);
        }
        highlightHintTiles(involvedTiles);
        return hint.getMessage();
    }

    private void highlightHintTiles(List<Index> highlighted) {
        if (highlighted != null) {
            adapter.setHighlightedTiles(highlighted);
            mGridView.invalidateViews();
        }
    }


    public static void addToMoveStack(Move move) {
        long time = System.currentTimeMillis();
        if (!moves.empty()) {
            Move lastMove = moves.pop();
            long delay = 1000; // 1 Second
            if (!lastMove.getIndex().equals(move.getIndex()) || (time - lastMove.getTime()) > delay) {
                moves.push(lastMove);
            }
        }
        moves.push(move);
    }

    public long getGameTime() {
        return timer.getTotalTime();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_board_fragment, container, false);
        mGridView = view.findViewById(R.id.board);
        initGrid(view.getContext(), board.getSize());
//        allTileView = adapter.allTileViews;
//        setupAdapter();

        return view;
    }
    private void initGrid(Context context, int size){
        mGridView.setNumColumns(size);
        adapter = new GridAdapter(context, board, mGridView, getActivity());
        mGridView.setAdapter(adapter);

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
//
//    public static Tile onClick(View view, Index index) {
//        Tile newMove = board.stepTile(index);
//        Move move = new Move(index, newMove.getSerialized());
//        addToMoveStack(move);
//        return newMove;
//    }


    public boolean undo(){
        if (moves.empty()){
            return false;
        }
        char color;
        Move lastMove = moves.pop();
        if (moves.contains(lastMove)){
            color = moves.get(moves.indexOf(lastMove)).getColor();
        }
        else
            color = Tile.EMPTY.getSerialized();
//        int lastMovePos=lastMove.getIndex().getRow()* board.getSize()+lastMove.getIndex().getCol();
//        TileViewHolder tileView = (TileViewHolder) mGridView.findViewHolderForAdapterPosition(lastMovePos);
        board.setTile(lastMove.getIndex(),Tile.deserialize(color));
//        tileView.setColor(Tile.deserialize(color));
        mGridView.invalidateViews();
        return true;
    }

}
