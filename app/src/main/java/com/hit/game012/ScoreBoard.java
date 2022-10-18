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

public class ScoreBoard extends AppCompatActivity {

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
        long gameTime;
        int hintCounter, boardSize, score, highScore = 0;
        isDailyGame = (boolean) getIntent().getExtras().get("isDailyGame");
        SharedPreferences sharedPref = this.getSharedPreferences("application", MODE_PRIVATE);
        boolean win = (boolean) getIntent().getExtras().get("win");
        awardAnimation = findViewById(R.id.awardAnimationView);
        barChartAnimation = findViewById(R.id.barChartAnimation);
        highScoreBeatText = findViewById(R.id.highScoreBeatText);
        fireworksAnimationView = findViewById(R.id.fireworksAnimationView);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2,
                1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid() + "," + mAuth.getCurrentUser().getDisplayName();

        // calculate current score
        gameTime = ((int) getIntent().getExtras().get("gameTime"));
        hintCounter = (int) getIntent().getExtras().get("hintCounter");
        boardSize = (int) getIntent().getExtras().get("boardSize");


        if (win) {
            fireworksAnimationView.setVisibility(View.VISIBLE);
            try {
                score = (int) Math.min((1000 / (gameTime + (hintCounter * 5))) * Math.pow(2,(boardSize)), Integer.MAX_VALUE);
                System.out.println("Score calculated: " + score);
            } catch (ArithmeticException e) {
                System.out.println("ArithmeticException! \ngameTime: " + gameTime + "\nhintCounter: " + hintCounter + "\nboardSize: " + boardSize);
                score = 0;
            }
        } else {
            score = 0;
            System.out.println("Default score: " + score);
        }

        // get previous personal high score and submit current
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

        // get score board
        if (isDailyGame) {
            try {
                highScoreTreeMap = threadPoolExecutor.submit(new GetHighScoreListThreaded(userID)).get();
                System.out.println("highScoreTreeMap: ");
                System.out.println(highScoreTreeMap);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

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
            // store new personal high score
            if (score >= highScore) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("personalHighScore", score);
                editor.apply();

                System.out.println("saved highscore to personal prefs: " + score);


            }
        }

        if (score >= highScore && win) {
            awardAnimation.setVisibility(View.VISIBLE);
            highScoreBeatText.setVisibility(View.VISIBLE);
        }

        onCreateAnimations();
    }

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

    public void newGame(View view) {
        Intent intent = new Intent(this, ChooseBoardSize.class);
        intent.putExtra("isDailyGame", isDailyGame);
        startActivity(intent);
        finish();
    }


}