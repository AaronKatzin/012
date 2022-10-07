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
import java.util.stream.IntStream;


public class BoardView extends Fragment {
    private static Board board;
    private BoardSolver boardSolver;
    private Set<ClickListener> ClickListeners;
    private static Timer timer;
    private static Stack<Move> moves;
    private Set<Index> highlightedIndexes;
    private RecyclerView mRecyclerView;
    private BoardViewAdapter adapter;

    private List<TileViewHolder> tiles;


    public static Board getBoard() {
        return board;
    }

    public BoardView(Board board) {
        this.board = board;
        boardSolver = new BoardSolver(board);
        timer = new Timer();

        highlightedIndexes = new HashSet<>();
        ClickListeners = new HashSet<>();
        moves = new Stack<>();

    }

    public void startGame() {
        timer.start();

    }

    public String requestHint() {
        boardSolver.setBoard(board);
        Hint hint = boardSolver.requestHint();
        List<Index> involvedTiles = hint.getInvolvedTiles();
        highlightedIndexes.clear();
        if (involvedTiles != null) {
            highlightedIndexes.addAll(involvedTiles);
            System.out.println("Highlighted indexes: " + highlightedIndexes);
            for (Index index : highlightedIndexes) {

            }
        }
        return hint.getMessage();
    }

    public void showAllLocks() {
        IntStream.range(0, board.getSize() * board.getSize()).forEach(i -> {
            Index index = board.getIndex(i);
            if (board.isLocked(index)) {
                TileViewHolder tileViewHolder = (TileViewHolder) mRecyclerView.findViewHolderForAdapterPosition(i);
                if (tileViewHolder != null) tileViewHolder.switchLock();
            }
        });
    }

    public static void addToMoveStack(Move move) {
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

        Tile newMove = board.stepTile(index);
        Move move = new Move(index, newMove.getSerialized());
        addToMoveStack(move);
        return newMove;
    }

}
