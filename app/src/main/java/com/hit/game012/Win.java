package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.hit.game012.gameplay.GetHighScoreThreaded;
import com.hit.game012.ui.MyListAdapter;

import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Win extends AppCompatActivity {
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
            com.facebook.login.R.drawable.com_facebook_profile_picture_blank_square,com.facebook.login.R.drawable.com_facebook_profile_picture_blank_square,
            com.facebook.login.R.drawable.com_facebook_profile_picture_blank_square,com.facebook.login.R.drawable.com_facebook_profile_picture_blank_square,
            com.facebook.login.R.drawable.com_facebook_profile_picture_blank_square,
            com.facebook.login.R.drawable.com_facebook_profile_picture_blank_square,com.facebook.login.R.drawable.com_facebook_profile_picture_blank_square,
            com.facebook.login.R.drawable.com_facebook_profile_picture_blank_square,com.facebook.login.R.drawable.com_facebook_profile_picture_blank_square,
            com.facebook.login.R.drawable.com_facebook_profile_picture_blank_square,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        long gameTime;
        int hintCounter;

        if(false) {
            gameTime = (long) getIntent().getExtras().get("gameTime");
            hintCounter = (int) getIntent().getExtras().get("hintCounter");
        }
        else{
            gameTime = 100;
            hintCounter = 1;
        }

        long score = gameTime + hintCounter;

        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText("gameTime: " + gameTime + "\n hintCounter: " + hintCounter+ "\n score: " + score);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 2,
                1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        TreeMap<String, String> highScoreTreeMap;
        try {
            highScoreTreeMap= threadPoolExecutor.submit(new GetHighScoreThreaded(userID)).get();
            System.out.println("highScoreTreeMap: ");

            System.out.println(highScoreTreeMap);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MyListAdapter adapter=new MyListAdapter(this, maintitle, subtitle,imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    public void newGame(View view){
        Intent intent = new Intent(this, ChooseBoardSize.class);
        startActivity(intent);
    }


}