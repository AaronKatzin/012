package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.hit.game012.gameplay.GetHighScoreListThreaded;
import com.hit.game012.gameplay.GetPersonalHighScoreThreaded;
import com.hit.game012.ui.MyListAdapter;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Activity representing the scoreboard screen
 * <p>
 * in case of Daily Game, shows user's current score, user's high score, and multiplayer scoreboard
 * which is fetched from the server
 * <p>
 * In case of a free game,  shows user's current score and user's high score
 */
public class ScoreBoard extends AppCompatActivity {

    // class attributes
    Map<String, String> highScoreTreeMap = new TreeMap<>();
    Map<String, Integer> myHighScoreM;
    ListView list;
    boolean isDailyGame = false;
    LottieAnimationView awardAnimation, barChartAnimation, fireworksAnimationView;
    TextView highScoreBeatText;

    final int defaultImgid = com.facebook.login.R.drawable.com_facebook_profile_picture_blank_square;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        // local variables
        long gameTime;
        int hintCounter, boardSize, score, highScore = 0;
        boolean win = (boolean) getIntent().getExtras().get("win");

        // variables from intent
        isDailyGame = (boolean) getIntent().getExtras().get("isDailyGame");
        SharedPreferences sharedPref = this.getSharedPreferences("application", MODE_PRIVATE);

        // views
        awardAnimation = findViewById(R.id.awardAnimationView);
        barChartAnimation = findViewById(R.id.barChartAnimation);
        highScoreBeatText = findViewById(R.id.highScoreBeatText);
        fireworksAnimationView = findViewById(R.id.fireworksAnimationView);

        // threading init
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2,
                1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

        // get firebase info
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid() + "," + mAuth.getCurrentUser().getDisplayName();

        // calculate current score
        gameTime = ((int) getIntent().getExtras().get("gameTime"));
        hintCounter = (int) getIntent().getExtras().get("hintCounter");
        boardSize = (int) getIntent().getExtras().get("boardSize");


        if (win) {
            fireworksAnimationView.setVisibility(View.VISIBLE);
            try {
                // calculate score
                score = (int) Math.min((1000 / (gameTime + (hintCounter * 5))) * Math.pow(2, boardSize), Integer.MAX_VALUE);
                System.out.println("Score calculated: " + score);
            } catch (ArithmeticException e) {
                // in case of division by 0
                System.out.println("ArithmeticException! \ngameTime: " + gameTime + "\nhintCounter: " + hintCounter + "\nboardSize: " + boardSize);
                score = 0;
            }
        } else {
            score = 0;
            System.out.println("Default score: " + score);
        }

        // get previous personal high score and submit current score to server for daily games
        if (isDailyGame) {
            try {
                myHighScoreM = threadPoolExecutor.submit(new GetPersonalHighScoreThreaded(userID, boardSize, score)).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            if (myHighScoreM != null && myHighScoreM.containsKey("score")) {
                highScore = myHighScoreM.get("score");
            }
        } else {
            highScore = sharedPref.getInt("personalHighScore", 0);
            System.out.println("got highscore from personal prefs: " + highScore);
            barChartAnimation.setVisibility(View.VISIBLE);
        }

        // set score text
        if (win) { // only show current score if you just played a game and won
            TextView scoreText = findViewById(R.id.scoreText);
            scoreText.setText(String.format(getResources().getString(R.string.score), score));
            scoreText.setVisibility(View.VISIBLE);
        }
        // always show your high score
        TextView scoreText = findViewById(R.id.personalHighScoreText);
        scoreText.setText(String.format(getResources().getString(R.string.high_score), highScore));

        // get score board for daily games
        if (isDailyGame) {
            try {
                highScoreTreeMap = threadPoolExecutor.submit(new GetHighScoreListThreaded(userID)).get();
                System.out.println("highScoreTreeMap: ");
                System.out.println(highScoreTreeMap);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            // fill scrolling list with scores
            MyListAdapter adapter = new MyListAdapter(this, highScoreTreeMap, defaultImgid);
            list = findViewById(R.id.list);

            // list title
            TextView textView = new TextView(this);
            textView.setText(this.getResources().getString(R.string.high_score_list_title));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(30);

            list.addHeaderView(textView);
            list.setAdapter(adapter);


            // make score board visible
            findViewById(R.id.multiplayerHighScoreTable).setVisibility(View.VISIBLE);

        } else {
            // store new personal high score in shared preferences memory
            if (score >= highScore) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("personalHighScore", score);
                editor.apply();

                System.out.println("saved highscore to personal prefs: " + score);


            }
        }

        // celebration for beating high score
        if (score >= highScore && win) {
            awardAnimation.setVisibility(View.VISIBLE);
            highScoreBeatText.setVisibility(View.VISIBLE);
        }

        onCreateAnimations();
    }

    /**
     * listeners for animation states
     */
    private void onCreateAnimations() {
        awardAnimation.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                awardAnimation.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * Finish current screen and start ChooseBoardSize activity
     *
     * @param view current view
     */
    public void newGame(View view) {
        Intent intent = new Intent(this, ChooseBoardSize.class);
        intent.putExtra("isDailyGame", isDailyGame);
        startActivity(intent);
        finish();
    }


}