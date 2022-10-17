package com.hit.game012.demo;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.view.ContextThemeWrapper;

import com.hit.game012.Config;
import com.hit.game012.R;
import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.game.Tile;
import com.hit.game012.gameplay.Validator;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DemoGridAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ThreadPoolExecutor threadPoolExecutor;
    private DemoValidator validator;

    public static int turn;
    public static List<Index[]> indexesInTurn = Arrays.asList(
            new Index[][]{
                    {new Index(0, 0)},
                    {new Index(1, 0)},
                    {new Index(0, 2)},
                    {new Index(1, 1)},
                    {new Index(2, 1), new Index(2, 2)},
                    {new Index(1, 0), new Index(1, 1), new Index(1, 2), new Index(1, 3)},
                    {new Index(0, 1), new Index(1, 1), new Index(2, 1), new Index(3, 1)},
                    {new Index(3, 2)},
                    {new Index(2, 0), new Index(2, 1), new Index(2, 2), new Index(2, 3),
                            new Index(3, 0), new Index(3, 1), new Index(3, 2), new Index(3, 3)},
                    {new Index(0, 3)}
            }
    );
    public static List<Tile[]> tilesInTurn = Arrays.asList(
            new Tile[][]{
                    {Tile.COLOR1},
                    {Tile.COLOR2},
                    {Tile.COLOR2},
                    {Tile.COLOR1},
                    {Tile.COLOR2, Tile.COLOR1},
                    {Tile.COLOR2, Tile.COLOR1, Tile.COLOR2, Tile.COLOR1},
                    {Tile.COLOR1, Tile.COLOR1, Tile.COLOR2, Tile.COLOR2},
                    {Tile.COLOR1},
                    {Tile.COLOR2, Tile.COLOR2, Tile.COLOR1, Tile.COLOR1
                            , Tile.COLOR1, Tile.COLOR2, Tile.COLOR1, Tile.COLOR2},
                    {Tile.COLOR2}
            }
    );

    public DemoGridAdapter(Activity activity, Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context.getApplicationContext());
        turn = 0;
        threadPoolExecutor = new ThreadPoolExecutor(1, 2,
                1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        validator = new DemoValidator(activity, this);

    }

    @Override
    public int getCount() {
        return DemoBoardView.board.getSize() * DemoBoardView.board.getSize();
    }

    @Override
    public Object getItem(int position) {
        return DemoBoardView.board.getTile(DemoBoardView.board.getIndex(position));
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ContextThemeWrapper ctx = new ContextThemeWrapper(context, R.style.Theme_012);
        view = inflater.from(ctx).inflate(R.layout.tile_holder, null);

        Index index = DemoBoardView.board.getIndex(position);
        TextView tileView = view.findViewById(R.id.tile);
        ImageView highlightView = view.findViewById(R.id.highlight);

        setColor(view, tileView, position);
        setTileSize(tileView, highlightView);
        if (turn < indexesInTurn.size()) {
            List<Index> turnIndexesList = Arrays.asList(indexesInTurn.get(turn));
            if (turnIndexesList.contains(index)) {
                List<Tile> turnTilesList = Arrays.asList(tilesInTurn.get(turn));
                // Current index is participating in the current turn

                // Highlight the tile
                highlightView.setVisibility(View.VISIBLE);

                // Handle onClick listener
                if (turnTilesList.get(turnIndexesList.indexOf(index)).equals(DemoBoardView.board.getTile(index))) {
                    // Tile already have its final color in the grid
                    view.setOnClickListener(null);
                } else {
                    // User can click the tile until reaching its final state
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DemoBoardView.board.stepTile(index);
                            notifyDataSetInvalidated();
                            validateTurn();
                        }
                    });
                }
            }
        } else {
            validateTurn();
        }

        return view;

    }

    private void setColor(View view, TextView tileView, int position) {
        Tile tile = (Tile) getItem(position);
        int tileDrawable;

        switch (tile) {
            case COLOR1:
                tileDrawable = Config.COLOR_TILE_ZERO;
                break;
            case COLOR2:
                tileDrawable = Config.COLOR_TILE_ONE;
                break;
            default:
                tileDrawable = R.drawable.tile_empty;

        }
        tileView.setBackground(view.getResources().getDrawable(tileDrawable, view.getContext().getTheme()));

    }

    private void setTileSize(TextView tileView, ImageView highlightView) {
        int tileSize = (Resources.getSystem().getDisplayMetrics().widthPixels - 100) / 4;
        tileView.setWidth(tileSize);
        tileView.setHeight(tileSize);
        highlightView.setMaxHeight(tileSize);
        highlightView.setMaxWidth(tileSize);
    }

    private void validateTurn() {
        threadPoolExecutor.submit(validator);
    }

    public void nextTurn() {
        if (turn <= indexesInTurn.size())
            notifyDataSetInvalidated();
        else
            endDemo();
    }

    private void endDemo() {

    }
}