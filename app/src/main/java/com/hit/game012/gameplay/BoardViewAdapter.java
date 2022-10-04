package com.hit.game012.gameplay;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.game012.R;
import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.game.Index;

import java.util.List;

public class BoardViewAdapter extends RecyclerView.Adapter<TileViewHolder> {
    private List<Index> indexes;
    private Board board;

    public BoardViewAdapter(List<Index> indexes, Board board) {
        this.indexes = indexes;
        this.board = board;
    }

    @NonNull
    @Override
    public TileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_holder, parent, false);
        return new TileViewHolder(layout, 'e');
    }

    @Override
    public void onBindViewHolder(@NonNull TileViewHolder holder, int position) {
        Index index = indexes.get(position);
        holder.setColor(board.getTile(index).getSerialized());

    }

    @Override
    public int getItemCount() {
        return indexes.size();
    }
}
