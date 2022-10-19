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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Activity to choose the size of the game to play.
 * If in daily challenge mode, loads the boolean array from SharedPreferences to check if user already played today
 * And update the button's view with the check mark.
 * If current date is different than the one saved in SharedPreferences, reset the boolean array.
 */
public class ChooseBoardSize extends AppCompatActivity {
    private TextView header;
    private boolean isDailyGame;
    private Boolean[] dailyBoardPlayed;
    private String dailyBoardDay;
    private final int[] boardPlayed = {
            R.id.board_checked_0,
            R.id.board_checked_1,
            R.id.board_checked_2,
            R.id.board_checked_3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_board_size);
        TextView game4 = findViewById(R.id.game4);
        TextView game6 = findViewById(R.id.game6);
        TextView game8 = findViewById(R.id.game8);
        TextView game10 = findViewById(R.id.game10);

        game4.setBackground(getResources().getDrawable(Config.COLOR_TILE_ONE, this.getTheme()));
        game6.setBackground(getResources().getDrawable(Config.COLOR_TILE_ZERO, this.getTheme()));
        game8.setBackground(getResources().getDrawable(Config.COLOR_TILE_ONE, this.getTheme()));
        game10.setBackground(getResources().getDrawable(Config.COLOR_TILE_ZERO, this.getTheme()));


        dailyBoardPlayed = new Boolean[4];
        header = findViewById(R.id.choose_size_header);
        isDailyGame = (boolean) getIntent().getExtras().get("isDailyGame");
        if (isDailyGame) dailyGameInit();
        else header.setText(R.string.choose_size_free_play);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isDailyGame) {
            saveToMem();
        }
    }

    private void dailyGameInit() {
        header.setText(R.string.choose_size_daily_play);
        loadFromMem();

        System.out.println(Arrays.asList(dailyBoardPlayed));
        String today = String.valueOf(LocalDate.now());

        if (!today.equals(dailyBoardDay)) {
            dailyBoardDay = today;
            Arrays.fill(dailyBoardPlayed, false);
        }

        TextView todayLabel = findViewById(R.id.choose_size_today);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        todayLabel.setText(String.valueOf(formatter.format(LocalDate.now(ZoneId.systemDefault()))));
        updateCheckDailyGame();
    }

    public void game4(View view) {
        playGame(4);
    }

    public void game6(View view) {
        playGame(6);
    }

    public void game8(View view) {
        playGame(8);
    }

    public void game10(View view) {
        playGame(10);
    }

    private void playGame(int size) {
        if (isDailyGame && String.valueOf(LocalDate.now()).equals(dailyBoardDay)) {
            if (dailyBoardPlayed[(size / 2) - 2]) {
                Toast.makeText(this, "You can play the daily challenge only once!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                dailyBoardPlayed[(size / 2) - 2] = true;
            }
        }
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra("size", size);
        intent.putExtra("isDailyGame", isDailyGame);
        startActivity(intent);
        finish();

    }

    private void loadFromMem() {
        SharedPreferences sharedPref = this.getSharedPreferences("application", MODE_PRIVATE);
        for (int i = 0; i < 4; i++) {
            dailyBoardPlayed[i] = sharedPref.getBoolean("isPlayed" + i, false);
        }
        dailyBoardDay = sharedPref.getString("dailyBoardDay", String.valueOf(LocalDate.now()));
        System.out.println("Loaded from mem: " + Arrays.toString(dailyBoardPlayed));
        System.out.println("Loaded from mem: " + dailyBoardDay);
    }

    private void saveToMem() {

        SharedPreferences sharedPref = this.getSharedPreferences("application", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for (int i = 0; i < 4; i++) {
            editor.putBoolean("isPlayed" + i, dailyBoardPlayed[i]);
        }
        editor.putString("dailyBoardDay", dailyBoardDay);
        editor.apply();
        System.out.println("Saved to mem: " + Arrays.toString(dailyBoardPlayed));
    }

    public void backToMenu(View view) {
        finish();
    }

    public void updateCheckDailyGame() {
        for (int i = 0; i < dailyBoardPlayed.length; i++) {
            if (dailyBoardPlayed[i])
                findViewById(boardPlayed[i]).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (isDailyGame)
            updateCheckDailyGame();
        super.onWindowFocusChanged(hasFocus);

    }

}

