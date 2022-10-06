package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.generator.BoardGenerator;
import com.hit.game012.startupsequence.AnimatedImageView;
import com.hit.game012.startupsequence.AnimatedTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnimatedImageView left = findViewById(R.id.left_tile);
        AnimatedImageView right = findViewById(R.id.right_tile);
        AnimatedTextView gameName = findViewById(R.id.app_name_startup);

        left.initAnimation(1500,1000);
        right.initAnimation(1500, 2000);
        gameName.initAnimation(2000, 3000);


    }
    public void startGame(View view){
        Intent intent = new Intent(this, ChooseBoardSize.class);
        startActivity(intent);
    }
}