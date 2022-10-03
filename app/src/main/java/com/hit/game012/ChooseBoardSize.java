package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.hit.game012.gameplay.GamePlay;

public class ChooseBoardSize extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_board_size);
    }
    public void game4 (View view){
        System.out.println("Game 4*4");
        GamePlay game = new GamePlay();
        game.startGame(4);
    }
    public void game6 (View view){
        System.out.println("Game 6*6");
        GamePlay game = new GamePlay();
        game.startGame(6);
    }
}