package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    private TextView btnHowToPlay;
    private TextView btnFreeGame;
    private TextView btnDailyGame;
    private TextView btnOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btnHowToPlay = findViewById(R.id.menu_btn_how_to_play);
        btnFreeGame = findViewById(R.id.menu_btn_free_game);
        btnDailyGame = findViewById(R.id.menu_btn_daily_game);
        btnOptions = findViewById(R.id.menu_btn_options);

    }

    public void howToPlay(View view){

    }

    public void freeGame(View view){
        chooseBoardSize(false);
    }

    public void dailyGame(View view){
        chooseBoardSize(true);
    }


    public void options(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void chooseBoardSize(boolean isDailyGame){
        Intent intent = new Intent(this, ChooseBoardSize.class);
        intent.putExtra("isDailyGame", isDailyGame);
        startActivity(intent);
    }

}