package com.hit.game012;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Chronometer;

import com.google.firebase.auth.FirebaseAuth;
import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.game.Tile;
import com.hit.game012.gameplay.BoardView;
import com.hit.game012.gameplay.GetBoardThreaded;
import com.hit.game012.gameplay.Move;
import com.hit.game012.startupsequence.AnimatedTextView;

import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;

/**
 * Game activity.
 * Receives board asynchronously either locally or from the server.
 * Starts BoardView fragment to load the board.
 */
public class BoardActivity extends AppCompatActivity {
    private Board board;
    private int boardSize;
    private boolean isDailyGame;
    private AnimatedTextView inGameMessageView;
    private GifImageView endGameGif;
    private Chronometer inGameTimerChronometer;
    private BoardView boardView;
    private int gameTime;
    private int hintCounter = 0;
    private boolean win = false;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.clicksounds);
        init();
        getBoard();
        resetInGameMessage(boardSize);

        // show board fragment
        boardView = new BoardView(board);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, boardView, boardView.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();

        inGameTimerChronometer.start();
    }

    private void init() {
        inGameMessageView = findViewById(R.id.in_game_message);
        endGameGif = findViewById(R.id.end_game_gif);
        inGameTimerChronometer = findViewById(R.id.timer_text);
        inGameTimerChronometer.setVisibility((Config.inGameTimerEnabled) ? View.VISIBLE : View.INVISIBLE);
        boardSize = (int) getIntent().getExtras().get("size");
        isDailyGame = (boolean) getIntent().getExtras().get("isDailyGame");
    }

    private void getBoard() {
        // Get board threaded
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2,
                1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        try {
            board = threadPoolExecutor.submit(new GetBoardThreaded(boardSize, isDailyGame, userID)).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void backToMenu(View view) {
        finish();
    }

    public void getHint(View view) {
        hintCounter++;
        setInGameMessage(boardView.requestHint(), 28);
    }

    public void undo(View view) {
        if (!boardView.undo()) {
            setInGameMessage(R.string.undo_stack_emp, 28);
        }
    }
    public void changeSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    public void howToPlay(View view) {
        Intent intent = new Intent(this, HowToPlayReminder.class);
        startActivity(intent);
    }


    public void resetInGameMessage(int boardSize) {
        inGameMessageView.setTextSize(40);
        inGameMessageView.setText(boardSize + " X " + boardSize);
    }

    public void setInGameMessage(int resID, int size) {
        String message = getResources().getString(resID);
        inGameMessageView.setTextSize(size);
        inGameMessageView.setText(message);
        inGameMessageView.initAnimation(800, 100);
    }

    public void stopGameTime() {
        inGameTimerChronometer.stop();
        setGameTime((int)(SystemClock.elapsedRealtime() - inGameTimerChronometer.getBase()) / 1000);
    }

    public void setEndGameGif(boolean validatorResult) {
        int resourceID;
        Random r = new Random();
        if (validatorResult) {
            // Win
            int[] winGifOptions = {R.drawable.win1, R.drawable.win2, R.drawable.win3, R.drawable.win6, R.drawable.win10, R.drawable.win12,};
            resourceID = winGifOptions[r.nextInt(winGifOptions.length)];
            win = true;
        } else {
            // Lose
            int[] loseGifOptions = {R.drawable.lose11, R.drawable.lose2, R.drawable.lose3, R.drawable.lose6, R.drawable.lose8};
            resourceID = loseGifOptions[r.nextInt(loseGifOptions.length)];
            win = false;
        }
        endGameGif.setImageResource(resourceID);

    }

    public void goToPostGameActivity(View view){
        Stack<Move> moves = BoardView.getMoves();
        Move lastMove = moves.peek();
        long delay = 1000; // 1 Second
        if ((System.currentTimeMillis() - lastMove.getTime()) > delay) {
            Intent intent;
            if (win) {
                intent = new Intent(this, ScoreBoard.class);
                intent.putExtra("gameTime", getGameTime());
                intent.putExtra("hintCounter", hintCounter);
                intent.putExtra("boardSize", boardSize);
                intent.putExtra("win", win);


            } else {
                intent = new Intent(this, ChooseBoardSize.class);
            }
            intent.putExtra("isDailyGame", isDailyGame);
            startActivity(intent);
            finish();
        } else {
            // change color of last tile
            Index index = lastMove.getIndex();
            Tile newMove = board.stepTile(index);
            Move move = new Move(index, newMove.getSerial());
            BoardView.addToMoveStack(move);
            boardView.getmGridView().invalidateViews();
        }
    }

    private void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public int getGameTime() {
        return gameTime;
    }
}