package com.hit.game012.gameplay;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.game012.R;

public class TileViewHolder extends RecyclerView.ViewHolder {
    private char color;

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
        TextView mTextView = itemView.findViewById(R.id.tile);
        int trueColor;
        switch (color){
            case 'a':
                trueColor = R.color.tileZero;
                break;
            case 'b':
                trueColor = R.color.tileOne;
                break;
            default:
                trueColor = R.color.tileEmpty;
        }
        mTextView.setBackgroundColor( itemView.getResources().
                getColor(trueColor, itemView.getContext().getTheme()));
    }


    public TileViewHolder(@NonNull View itemView, char color) {
        super(itemView);
        setColor(color);
    }
}
