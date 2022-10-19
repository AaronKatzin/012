package com.hit.game012.demo;

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

/**
 * Class to show the demo board in How To Play activity.
 * The demo board is a special case of the regular board
 */
public class DemoBoardView extends Fragment {
    public static Board board;
    private GridView mGridView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.demo_board_view_fragment, container, false);
        mGridView = view.findViewById(R.id.demo_board_gridview);
        initBoard();
        initGrid(view.getContext());
        return view;
    }

    /**
     * Creates the demo board scenario
     */
    private void initBoard() {
        board = new Board(4);

        // Set first indexes position
        board.setTile(new Index(0, 1), Tile.COLOR1);
        board.setTile(new Index(1, 2), Tile.COLOR2);
        board.setTile(new Index(3, 3), Tile.COLOR2);
    }
    private void initGrid(Context context){
        mGridView.setNumColumns(4);
        mGridView.setVerticalSpacing(7);
        mGridView.setHorizontalSpacing(7);

        DemoGridAdapter adapter = new DemoGridAdapter(getActivity() ,context);
        mGridView.setAdapter(adapter);
    }

}
