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
import com.hit.game012.gamelogic.game.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BoardViewAdapter extends RecyclerView.Adapter<TileViewHolder> {
    private List<Index> indexes;
//    private Board board;
//    private  List<TileViewHolder> tiles;

    public BoardViewAdapter(List<Index> indexes, Board board) {
        this.indexes = indexes;
//        this.board = board;
//        tiles = new ArrayList<>();
    }

//    public List<TileViewHolder> getTiles() {
//        return tiles;
//    }

    @NonNull
    @Override
    public TileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.tile_holder, parent, false);
        TileViewHolder newTile = new TileViewHolder(layout, Tile.EMPTY, BoardView.getBoard().getSize());
        return newTile;
    }

//    @Override
//    public void onViewAttachedToWindow(@NonNull TileViewHolder holder) {
//        super.onViewAttachedToWindow(holder);
////        tiles.add(holder);
//    }

    @Override
    public void onBindViewHolder(@NonNull TileViewHolder holder, int position) {
        Index index = indexes.get(position);
        holder.setColor(BoardView.getBoard().getTile(index));
        holder.setIndex(index);
//        holder.setLocked(BoardView.getBoard().isLocked(index));
//        BoardView.setClickListener(holder.itemView, index);

    }

    @Override
    public int getItemCount() {
        return indexes.size();
    }

//    public static void highlightIndexes(List<Index> highlighted){
//        for (Index index : highlighted){
//
//        }
//    }
}
