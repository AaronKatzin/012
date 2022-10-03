package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.generator.BoardGenerator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, ChooseBoardSize.class);
        startActivity(intent);
    }
}