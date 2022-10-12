package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Win extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
    }

    public void newGame(View view){
        Intent intent = new Intent(this, ChooseBoardSize.class);
        intent.putExtra("isDailyGame", false);
        startActivity(intent);
    }
}