package com.hit.game012.gameplay;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class BoardView extends Fragment {
    private Board board;
    private Set<Index> highlightedIndexes;
    private Set<ClickListener> ClickListeners;
    private RecyclerView mRecyclerView;

    public BoardView(Board board) {
        this.board = board;
        highlightedIndexes = new HashSet<>();
        ClickListeners = new HashSet<>();
        int size = board.getSize();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_board, container, false);
        mRecyclerView = view.findViewById(R.id.board);
        setupAdapter();
        return view;
    }

    private void setupAdapter(){
        // get all the indexes
        List<Index> indexes = new ArrayList<>();
        for (int i=0; i<board.getSize(); i++)
            for (int j=0; j< board.getSize(); j++)
                indexes.add(new Index(i,j));

        // Build the adapter for the RecyclerView
        BoardViewAdapter adapter = new BoardViewAdapter(indexes, board);
        mRecyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), board.getSize()));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new TileViewDecorator());

    }
}
