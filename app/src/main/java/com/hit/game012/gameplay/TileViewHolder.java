package com.hit.game012.gameplay;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.game012.R;
import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.game.Tile;

public class TileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Tile tile;
    private final int boardSize;
    private int tileSize;
    private TextView tileView;
    private ImageView padlockView;
    private ImageView highlightView;
    private Index index;



    public TileViewHolder(@NonNull View itemView, Tile tile, int boardSize) {
        super(itemView);
        this.tile = tile;
        this.boardSize = boardSize;
        tileView = itemView.findViewById(R.id.tile);
        padlockView = itemView.findViewById(R.id.lock_icon);
        highlightView = itemView.findViewById(R.id.highlight);
        setColor(tile);
        setTileSize();
        itemView.setOnClickListener(this);
    }


    public Tile setColor(Tile tile) {
        this.tile = tile;

        Tile newTile;
        int tileDrawable;

        switch (tile) {
            case COLOR1:
                tileDrawable = R.drawable.tile_zero;
                newTile = Tile.COLOR1;
                break;
            case COLOR2:
                tileDrawable = R.drawable.tile_one;
                newTile = Tile.COLOR2;
                break;
            default:
                tileDrawable = R.drawable.tile_empty;
                newTile = Tile.EMPTY;
        }

        tileView.setBackground(itemView.getResources().getDrawable(tileDrawable, itemView.getContext().getTheme()));
        return newTile;
    }

    public void setTileSize() {
        tileSize = (Resources.getSystem().getDisplayMetrics().widthPixels - 100) / boardSize;
        tileView.setWidth(tileSize);
        tileView.setHeight(tileSize);
        highlightView.setMaxHeight(tileSize);
        highlightView.setMaxWidth(tileSize);
    }


    public void switchLock() {
        if (BoardView.getBoard().isLocked(index)) {
            if (padlockView.getVisibility() == VISIBLE) padlockView.setVisibility(INVISIBLE);
            else {
                padlockView.setVisibility(VISIBLE);
                tileView.startAnimation(AnimationUtils.loadAnimation(tileView.getContext(), R.anim.shake_anim));
            }
        }
    }

    public void showHighlight() {
        highlightView.setVisibility(VISIBLE);
    }

    public void resetHighlight() {
        highlightView.setVisibility(INVISIBLE);
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public char getColor() {
        return tile.getSerialized();
    }

    @Override
    public void onClick(View view) {

        if (!BoardView.getBoard().isLocked(index)) {
            Tile newTile = BoardView.onClick(view, index);
            setColor(newTile);
        } else {
            switchLock();
        }
    }
}
