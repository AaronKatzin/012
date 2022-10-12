package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HowToPlayReminder extends AppCompatActivity {

    FloatingActionButton exitHowToPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play_reminder);
        exitHowToPlay = findViewById(R.id.exitHowToPlay);

        exitHowToPlay.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }


}