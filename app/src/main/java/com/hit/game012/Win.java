package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

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

public class Win extends AppCompatActivity {

    TreeMap<String, String> highScoreTreeMap = new TreeMap<>();
    Map<String, Integer> myHighScoreM;
    ListView list;
    boolean isDailyGame = false;

    String[] maintitle ={
            "User Name","User Name",
            "User Name","User Name",
            "User Name",
            "User Name","User Name",
            "User Name","User Name",
            "User Name",
    };

    String[] subtitle ={
            "Score","Score",
            "Score","Score",
            "Score",
            "Score","Score",
            "Score","Score",
            "Score",
    };


    Integer[] imgid={
            com.facebook.login.R.drawable.com_facebook_profile_picture_blank_square
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        long gameTime;
        int hintCounter,boardSize,score, highScore = 0;
        isDailyGame = (boolean) getIntent().getExtras().get("isDailyGame");
        SharedPreferences sharedPref = this.getSharedPreferences("application", MODE_PRIVATE);
        boolean win = (boolean) getIntent().getExtras().get("win");

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2,
                1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        // calculate current score
        gameTime = (long) getIntent().getExtras().get("gameTime");
        hintCounter = (int) getIntent().getExtras().get("hintCounter");
        boardSize = (int) getIntent().getExtras().get("boardSize");


        if(win){
            try {
                score = (int) Math.min((( ((gameTime-hintCounter*5)/60)/(99+((gameTime-hintCounter*5)/60)) ))  * 2^(boardSize), Integer.MAX_VALUE);
                System.out.println("Score calculated: " + score);
            } catch (ArithmeticException e){
                System.out.println("ArithmeticException! \ngameTime: " + gameTime + "\nhintCounter: " + hintCounter + "\nboardSize: " +  boardSize);
                score = 0;
            }
        } else {
            score = 0;
            System.out.println("Default score: " + score);
        }

        // get previous personal high score and submit current
        if(isDailyGame){
            try {
                myHighScoreM = threadPoolExecutor.submit(new GetPersonalHighScoreThreaded(userID, boardSize, score)).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(myHighScoreM != null && myHighScoreM.containsKey("score")){
                highScore = myHighScoreM.get("score");
            }
        } else {
            highScore = sharedPref.getInt("personalHighScore", 0);
            System.out.println("got highscore from personal prefs: " + highScore);
        }

        // set score text
        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText(getResources().getString(R.string.score) + score + "\n" + getResources().getString(R.string.high_score) + highScore);


        // get score board
        if(isDailyGame) {
            try {
                highScoreTreeMap = threadPoolExecutor.submit(new GetHighScoreListThreaded(userID)).get();
                System.out.println("highScoreTreeMap: ");

                System.out.println(highScoreTreeMap);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            MyListAdapter adapter = new MyListAdapter(this, highScoreTreeMap, imgid);
            list = (ListView) findViewById(R.id.list);
            list.setAdapter(adapter);
        } else {
            // store new personal high score
            if(score >= highScore){
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("personalHighScore", score);
                editor.apply();

                System.out.println("saved highscore to personal prefs: " + score);
                // todo fireworks GIF since you beat your highscore?
            }
        }
    }

    public void newGame(View view){
        Intent intent = new Intent(this, ChooseBoardSize.class);
        intent.putExtra("isDailyGame", isDailyGame);
        startActivity(intent);
    }


}