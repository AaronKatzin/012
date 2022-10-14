package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.hit.game012.ui.MyListAdapter;

import java.util.Random;

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

        MyListAdapter adapter=new MyListAdapter(this, maintitle, subtitle,imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    public void newGame(View view){
        Intent intent = new Intent(this, ChooseBoardSize.class);
        startActivity(intent);
    }

}