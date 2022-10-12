package com.hit.game012;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;


//import com.bumptech.glide.Glide;
import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gameplay.BoardView;
import com.hit.game012.gameplay.GetBoardThreaded;
import com.hit.game012.startupsequence.AnimatedTextView;


import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;


public class BoardActivity extends AppCompatActivity {
    private Board board;
    private int boardSize;
    private boolean isDailyGame;
    private AnimatedTextView inGameMessageView;
    private GifImageView endGameGif;
    private Chronometer inGameTimerChronometer;
    private BoardView boardView;
    private int hintCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        init();
        getBoard();
        resetInGameMessage(boardSize);
        // show board fragment
        boardView = new BoardView(board);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, boardView, boardView.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();

//        boardView.startGame();
        inGameTimerChronometer.start();
    }
    private void init(){
        inGameMessageView = findViewById(R.id.in_game_message);
        inGameTimerChronometer = findViewById(R.id.timer_text);
        endGameGif = findViewById(R.id.end_game_gif);

//        endGameGif.setImageResource(R.drawable.lose1);

        boardSize = (int) getIntent().getExtras().get("size");
        isDailyGame = (boolean) getIntent().getExtras().get("isDailyGame");

    }
    private void getBoard(){
        // Get board threaded
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2,
                1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        try {
            board = threadPoolExecutor.submit(new GetBoardThreaded(boardSize, isDailyGame)).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void backToMenu(View view){
        finish();
    }
    public void getHint(View view){
        hintCounter++;
        setInGameMessage(boardView.requestHint(),28);
    }

    public void undo(View view){
        if(!boardView.undo()){
            setInGameMessage(R.string.undo_stack_emp,28);
        }
    }

    public void resetInGameMessage(int boardSize){
        inGameMessageView.setTextSize(40);
        inGameMessageView.setText(boardSize+" X "+ boardSize);
    }

    public void setInGameMessage(int resID, int size){
        String message = getResources().getString(resID);
        inGameMessageView.setTextSize(size);
        inGameMessageView.setText(message);
        inGameMessageView.initAnimation(800,100);
    }
    public long getGameTime(){
        inGameTimerChronometer.stop();
        return inGameTimerChronometer.getBase();
    }
    public void setEndGameGif(boolean validatorResult){
        int resourceID;
        Random r = new Random();
        if (validatorResult){
            // Win
            int[] winGifOptions = {R.drawable.win1, R.drawable.win2, R.drawable.win3, R.drawable.win4};
            resourceID = winGifOptions[r.nextInt(winGifOptions.length)];
        }
        else{
            // Lose
            int[] loseGifOptions = {R.drawable.lose1, R.drawable.lose2, R.drawable.lose3};
            resourceID = loseGifOptions[r.nextInt(loseGifOptions.length)];
        }
        endGameGif.setImageResource(resourceID);

        goToWinScreen();


//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom_animation);
//        endGameGif.startAnimation(animation);

    }

    public void goToWinScreen(){
        Intent intent = new Intent(this, Win.class);
        intent.putExtra("gameTime", getGameTime());
        intent.putExtra("hintCounter", hintCounter);
        startActivity(intent);
    }

}