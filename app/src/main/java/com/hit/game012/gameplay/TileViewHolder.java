package com.hit.game012.gameplay;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.game012.R;
import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.game.Tile;

public class TileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Tile tile;
    private int boardSize;
    private TextView mTextView;

    public char getColor() {
        return tile.getSerialized();
    }

    public void setColor(Tile tile) {
        this.tile = tile;
        int trueColor;
        switch (tile){
            case COLOR1:
                trueColor = R.color.tileZero;
                break;
            case COLOR2:
                trueColor = R.color.tileOne;
                break;
            default:
                trueColor = R.color.tileEmpty;
        }
        mTextView.setBackgroundColor( itemView.getResources().
                getColor(trueColor, itemView.getContext().getTheme()));
    }


    public TileViewHolder(@NonNull View itemView, Tile tile, int boardSize) {
        super(itemView);
        this.tile = tile;
        mTextView = itemView.findViewById(R.id.tile);
        setColor(tile);
        this.boardSize = boardSize;
        int tileSize = (Resources.getSystem().getDisplayMetrics().widthPixels - 200)/ boardSize ;
        setTileSize(tileSize);
        itemView.setOnClickListener(this);
    }

    public void setTileSize(int size){
        mTextView.setWidth(size);
        mTextView.setHeight(size);
    }

    @Override
    public void onClick(View view) {
        int position = this.getAdapterPosition();
        Index index = new Index(position/boardSize, position%boardSize);
        Toast.makeText(view.getContext(), "index "+index+" clicked, color is "+tile.getSerialized(), Toast.LENGTH_LONG).show();
        setColor(tile.nextState());
    }
}
