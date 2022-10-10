package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;


public class ChooseBoardSize extends AppCompatActivity {
    CheckBox mCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_board_size);
        mCheckBox = findViewById(R.id.fromServer);

    }
    public void game4 (View view){playGame(4, mCheckBox.isChecked());}
    public void game6 (View view){
        playGame(6, mCheckBox.isChecked());
    }
    public void game8 (View view){
        playGame(8, mCheckBox.isChecked());
    }
    public void game10 (View view){
        playGame(10, mCheckBox.isChecked());
    }

    private void playGame(int size, boolean fromServer){
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra("size", size);
        intent.putExtra("fromServer", fromServer);
        startActivity(intent);
    }
}