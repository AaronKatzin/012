package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;


public class ChooseBoardSize extends AppCompatActivity {
    private TextView header;
    private boolean isDailyGame;
    private boolean[] dailyBoardPlayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_board_size);

        header = findViewById(R.id.choose_size_header);
        isDailyGame = (boolean) getIntent().getExtras().get("isDailyGame");
        if (isDailyGame) header.setText(R.string.choose_size_daily_play);
        else header.setText(R.string.choose_size_free_play);

    }

    public void game4 (View view){playGame(4);}
    public void game6 (View view){
        playGame(6);
    }
    public void game8 (View view){ playGame(8);  }
    public void game10 (View view){
        playGame(10);
    }



    private void playGame(int size){
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra("size", size);
        intent.putExtra("isDailyGame", isDailyGame);
        startActivity(intent);
    }
    private void loadFromMem(){

    }
    private void saveToMem(){

    }
    public void backToMenu(View view){
        finish();
    }
}