package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        int hintCounter,boardSize,score, highScore;

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2,
                1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();


        // calculate current score
        gameTime = (long) getIntent().getExtras().get("gameTime");
        hintCounter = (int) getIntent().getExtras().get("hintCounter");
        boardSize = (int) getIntent().getExtras().get("boardSize");


        score = (int)gameTime + hintCounter;

        // get previous personal high score and submit current
        try {
            myHighScoreM = threadPoolExecutor.submit(new GetPersonalHighScoreThreaded(userID, boardSize, score)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(myHighScoreM != null && myHighScoreM.containsKey("score")){
            highScore = myHighScoreM.get("score");
        } else {

            highScore = 0;
        }

        // set score text
        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText(getResources().getString(R.string.score) + score + "\n" + getResources().getString(R.string.high_score) + highScore);


        // get score board
        try {
            highScoreTreeMap = threadPoolExecutor.submit(new GetHighScoreListThreaded(userID)).get();
            System.out.println("highScoreTreeMap: ");

            System.out.println(highScoreTreeMap);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MyListAdapter adapter=new MyListAdapter(this, highScoreTreeMap,imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    public void newGame(View view){
        Intent intent = new Intent(this, ChooseBoardSize.class);
        startActivity(intent);
    }


}