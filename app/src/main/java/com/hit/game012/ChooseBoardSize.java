package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.Arrays;


public class ChooseBoardSize extends AppCompatActivity {
    private TextView header;
    private boolean isDailyGame;
    private Boolean[] dailyBoardPlayed;
    private String dailyBoardDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_board_size);
        dailyBoardPlayed = new Boolean[4];
        header = findViewById(R.id.choose_size_header);
        isDailyGame = (boolean) getIntent().getExtras().get("isDailyGame");
        if (isDailyGame) {
            header.setText(R.string.choose_size_daily_play);
            loadFromMem();
        }
        else header.setText(R.string.choose_size_free_play);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isDailyGame) {
            saveToMem();
        }
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
        if (isDailyGame && String.valueOf(LocalDate.now()).equals(dailyBoardDay)){
            if(dailyBoardPlayed[(size/2)-2]){
                Toast.makeText(this, "You can play the daily challenge only once!", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                dailyBoardPlayed[(size/2)-2] = true;
            }
        }

        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra("size", size);
        intent.putExtra("isDailyGame", isDailyGame);
        startActivity(intent);
    }
    private void loadFromMem(){
        SharedPreferences sharedPref = this.getSharedPreferences("application", MODE_PRIVATE);
        for (int i=0; i<4;i++){
            dailyBoardPlayed[i] = sharedPref.getBoolean("isPlayed" + i, false);
        }
        dailyBoardDay = sharedPref.getString("dailyBoardDay", String.valueOf(LocalDate.now()));
        System.out.println("Loaded from mem: " + Arrays.toString(dailyBoardPlayed));
    }
    private void saveToMem(){

        SharedPreferences sharedPref = this.getSharedPreferences("application", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (int i=0; i<4;i++){
            editor.putBoolean("isPlayed" + i, dailyBoardPlayed[i]);
        }
        editor.putString("dailyBoardDay", dailyBoardDay);
        editor.apply();
        System.out.println("Saved to mem: " + Arrays.toString(dailyBoardPlayed));
    }
    public void backToMenu(View view){
        finish();
    }
}