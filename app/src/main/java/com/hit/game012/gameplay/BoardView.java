package com.hit.game012.gameplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private RecyclerView mRecyclerView;
    private BoardViewAdapter adapter;
//    private static Set<TileViewHolder> lockedTiles;
//    private Set<View.OnClickListener> listeners;

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
            // There is a tile to highlight
            for (int i = 0; i < board.getSize() * board.getSize(); i++) {
                TileViewHolder tileView = (TileViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);
                Index index = tileView.getIndex();
                if (highlighted.contains(index)) {
                    tileView.showHighlight();
                } else {
                    tileView.resetHighlight();
                }
            }
        } else {
            // No tiles to highlight - reset all highlights
            for (int i = 0; i < board.getSize() * board.getSize(); i++) {
                TileViewHolder tileView = (TileViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);
                tileView.resetHighlight();

            }
        }
    }

    public void showAllLocks() {
        for (int i=0; i<board.getSize()* board.getSize();i++){
            TileViewHolder tileView = (TileViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);
            if(board.isLocked(tileView.getIndex())){
                tileView.switchLock();
            }
        }

    }

    public static void addMoveToStack(Move move) {
        long time = System.currentTimeMillis();
        if (!moves.empty()) {
            Move lastMove = moves.pop();
            long delay = 1000; // 1 Second
            if (lastMove.getIndex() != move.getIndex() || time - lastMove.getTime() > delay) {
                moves.push(lastMove);
            }
        }
        moves.push(move);
//        System.out.println(moves);
    }

    public long getGameTime() {
        return timer.getTotalTime();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_board_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.board);
        setupAdapter();
        return view;
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        listeners=adapter.getListeners();
//    }

    private void setupAdapter() {
        // get all the indexes
        List<Index> indexes = new ArrayList<>();
        for (int i = 0; i < board.getSize(); i++)
            for (int j = 0; j < board.getSize(); j++)
                indexes.add(new Index(i, j));

        // Build the adapter for the RecyclerView
        adapter = new BoardViewAdapter(indexes, board);
        mRecyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), board.getSize()));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new TileViewDecorator());
    }

    public static Tile onClick(View view, Index index) {
        if(!getBoard().isLocked(index)){
            Tile newMove = board.stepTile(index);
            Move move = new Move(index, newMove.getSerialized());
            addMoveToStack(move);
            return newMove;
        }
        else{
//            showAllLocks();
            return null;
        }


    }

    public boolean undo(){
        if (moves.empty()){
            return false;
        }
        char color;
        Move lastMove=moves.pop();
        if (moves.contains(lastMove)){
            color=moves.get(moves.indexOf(lastMove)).getColor();
        }
        else
            color= Tile.EMPTY.getSerialized();
        int lastMovePos=lastMove.getIndex().getRow()* board.getSize()+lastMove.getIndex().getCol();
        TileViewHolder tileView = (TileViewHolder) mRecyclerView.findViewHolderForAdapterPosition(lastMovePos);
        board.setTile(lastMove.getIndex(),Tile.deserialize(color));
        tileView.setColor(Tile.deserialize(color));
        return true;
    }

//    public Set<TileViewHolder> getLockedTiles(){
//        Set<Index> lockedIndexes= board.getLocked();
//        Set<TileViewHolder> lockedTiles= new HashSet<>();
//        int pos;
//       mRecyclerView.setAdapter(adapter);
////        for (int i = 0; i < adapter.getItemCount(); i++) {
////            TileViewHolder holder = (TileViewHolder) mRecyclerView.findViewHolderForItemId(adapter.getItemId(i));
////            lockedTiles.add(holder);
////        }
////        for (Index index: lockedIndexes){
////            pos=index.getRow()* board.getSize()+index.getCol();
////            TileViewHolder tileView = (TileViewHolder) mRecyclerView.findViewHolderForAdapterPosition(pos);
////            lockedTiles.add(tileView);
////        }
//        return lockedTiles;
//    }
}



