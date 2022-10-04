package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        playGame(4);
    }
    public void game6 (View view){
        playGame(6);
    }
    private void playGame(int size){
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra("size", size);
        startActivity(intent);
    }
}