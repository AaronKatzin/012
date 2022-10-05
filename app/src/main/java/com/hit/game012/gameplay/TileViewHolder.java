package com.hit.game012.gameplay;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.game012.R;
import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.game.Tile;

public class TileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Tile tile;
    private final int boardSize;
    private TextView mTextView;
    private Index index;
    private boolean isHighlighted = false;
    private boolean isLocked = false;

    public TileViewHolder(@NonNull View itemView, Tile tile, int boardSize) {
        super(itemView);
        this.tile = tile;
        this.boardSize = boardSize;

        mTextView = itemView.findViewById(R.id.tile);
        setColor(tile);
        setTileSize();
        itemView.setOnClickListener(this); // ?
    }

    public Tile setColor(Tile tile) {
        this.tile = tile;

        Tile newTile;
        int tileDrawable;

        switch (tile){
            case COLOR1:
                tileDrawable=R.drawable.tile_zero;
                newTile = Tile.COLOR1;
                break;
            case COLOR2:
                tileDrawable=R.drawable.tile_one;
                newTile = Tile.COLOR2;
                break;
            default:
                tileDrawable=R.drawable.tile_empty;
                newTile = Tile.EMPTY;
        }
        mTextView.setBackground(itemView.getResources().getDrawable(tileDrawable,itemView.getContext().getTheme()));
        return newTile;
    }

    public void setTileSize(){
        int tileSize = (Resources.getSystem().getDisplayMetrics().widthPixels - 200)/ boardSize ;
        mTextView.setWidth(tileSize);
        mTextView.setHeight(tileSize);
    }

    public Index getIndex() {
        return index;
    }
    public void setIndex(Index index) {
        this.index = index;
    }
    public boolean isHighlighted() {
        return isHighlighted;
    }
    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }
    public boolean isLocked() {
        return isLocked;
    }
    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public Tile stepTile(){
        return setColor(tile.nextState());

    }

    public char getColor() {
        return tile.getSerialized();
    }

    @Override
    public void onClick(View view) {
        Tile newMove = stepTile();
        Move move = new Move(index, newMove.getSerialized());
        GamePlay.onTileClick(move);
    }
}
