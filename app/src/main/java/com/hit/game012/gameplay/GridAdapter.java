package com.hit.game012.gameplay;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.RenderNode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GridAdapter extends BaseAdapter {
    private int tileSize;
    private Board board;
    private GridView mGridView;
    private Context context;
    private LayoutInflater inflater;
    private FragmentActivity activity;
    private List<View> locked;
    private List<Index> highlightedTiles;
    private ThreadPoolExecutor threadPoolExecutor;

    public GridAdapter(Context context, Board board, GridView grid, FragmentActivity activity) {
        this.context = context;
        this.board = board;
        mGridView = grid;
        tileSize = (Resources.getSystem().getDisplayMetrics().widthPixels - 100) / board.getSize();
        inflater = LayoutInflater.from(context.getApplicationContext());
        locked = new ArrayList<>();
        highlightedTiles = new ArrayList<>();
        this.activity = activity;
        threadPoolExecutor = new ThreadPoolExecutor(1, 2,
                1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
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
                    validateBoard(v);
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

    private void validateBoard(View view){
        int message;

        Boolean isSolved=false;
        if(board.isFull()){
            // save the time?

            //validate in new thread
            if(threadPoolExecutor.getActiveCount()==0) {
                // TODO : fix condition to create only one thread!

                System.out.println("start validate");
                try {
                    isSolved= threadPoolExecutor.submit(new Validator()).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(isSolved){
                    // TODO : stop timer
                    int[] winStringMessages={R.string.win_great,R.string.win_good_job,R.string.win_excellent,R.string.win_amazing};
                    Random r=new Random();
                    message=r.nextInt(winStringMessages.length);
                    ((BoardActivity) activity).setInGameMessage(winStringMessages[message],34);
                    popupAnim(view);
                }
//                    threadPoolExecutor.execute(new ShowPopUp(view));
                else{
                    int[] loseStringMessages={R.string.lose_next_time,R.string.lose_not_good,R.string.lose_practice};
                    Random r=new Random();
                    message=r.nextInt(loseStringMessages.length);
                    ((BoardActivity) activity).setInGameMessage(loseStringMessages[message],34);
                    popupAnim(view);
                }

            }

        }


    }
    public void popupAnim(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        final View layout=LayoutInflater.from(view.getContext()).inflate(R.layout.pop_up_win,null);
        alertDialogBuilder.setView(layout);
//        alertDialogBuilder.setMessage("No Internet Connection. Check Your Wifi Or enter code hereMobile Data.");
//        alertDialogBuilder.setTitle("Connection Failed");
//        alertDialogBuilder.setNegativeButton("ok", new DialogInterface.OnClickListener(){

//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
                // add these two lines, if you wish to close the app:
//                finishAffinity();
//                System.exit(0);
//            }
//        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
