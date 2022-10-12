package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Win extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        long gameTime = (long) getIntent().getExtras().get("gameTime");
        int hintCounter = (int) getIntent().getExtras().get("hintCounter");

        long score = gameTime + hintCounter;

        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText("gameTime: " + gameTime + "\n hintCounter: " + hintCounter+ "\n score: " + score);
    }

    public void newGame(View view){
        Intent intent = new Intent(this, ChooseBoardSize.class);
        startActivity(intent);
    }
}