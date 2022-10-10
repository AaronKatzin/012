package com.hit.game012.gameplay;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.hit.game012.BoardActivity;
import com.hit.game012.R;
import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.game.Tile;
import com.hit.game012.startupsequence.AnimatedImageView;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {
    int tileSize;
    Board board;
    GridView mGridView;
    Context context;
    LayoutInflater inflater;
    FragmentActivity activity;
    List<View> locked;
    List<Index> highlightedTiles;

    public GridAdapter(Context context, Board board, GridView grid, FragmentActivity activity) {
        this.context = context;
        this.board = board;
        mGridView = grid;
        tileSize = (Resources.getSystem().getDisplayMetrics().widthPixels - 100) / board.getSize();
        inflater = LayoutInflater.from(context.getApplicationContext());
        locked = new ArrayList<>();
        highlightedTiles = new ArrayList<>();
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return board.getSize() * board.getSize();
    }

    @Override
    public Object getItem(int position) {
        return board.getTile(board.getIndex(position));
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setHighlightedTiles(List<Index> highlightedTiles) {
        this.highlightedTiles = highlightedTiles;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ContextThemeWrapper ctx = new ContextThemeWrapper(context, R.style.Theme_012);
        view = inflater.from(ctx).inflate(R.layout.tile_holder, null);

        Index index = board.getIndex(position);
        TextView tileView = view.findViewById(R.id.tile);
        ImageView highlightView = view.findViewById(R.id.highlight);

        setColor(view, tileView, position);
        setTileSize(tileView, highlightView);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BoardActivity) activity).resetInGameMessage(board.getSize());
                highlightedTiles.clear();
                if (!board.isLocked(index)) {
                    Tile newMove = board.stepTile(index);
                    Move move = new Move(index, newMove.getSerialized());
                    BoardView.addToMoveStack(move);
                    mGridView.invalidateViews();
                } else {
                    for (View lockedView : locked) {
                        AnimatedImageView padlock = lockedView.findViewById(R.id.lock_icon);
                        padlock.initPadlockAnimation();
                        lockedView.startAnimation(AnimationUtils.loadAnimation(lockedView.getContext(), R.anim.shake_anim));
                        padlock.setVisibility(INVISIBLE);
                    }
                }
//
            }
        });
        if(highlightedTiles.contains(index)){
            highlightView.setVisibility(VISIBLE);
        }

        if (board.isLocked(board.getIndex(position)))
            locked.add(view);
        return view;
    }

    private void setTileSize(TextView tileView, ImageView highlightView) {
        tileView.setWidth(tileSize);
        tileView.setHeight(tileSize);
        highlightView.setMaxHeight(tileSize);
        highlightView.setMaxWidth(tileSize);
    }

    private void setColor(View view, TextView tileView, int position) {
        Tile tile = (Tile) getItem(position);
        int tileDrawable;

        switch (tile) {
            case COLOR1:
                tileDrawable = R.drawable.tile_zero;
                break;
            case COLOR2:
                tileDrawable = R.drawable.tile_one;
                break;
            default:
                tileDrawable = R.drawable.tile_empty;
        }
        tileView.setBackground(view.getResources().getDrawable(tileDrawable, view.getContext().getTheme()));

    }
}