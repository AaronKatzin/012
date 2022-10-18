package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

/**
 * Activity representing the How To Play screen
 */
public class HowToPlayReminder extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play_reminder);
    }

    /**
     * Finishes current activity
     * @param view current view
     */
    public void backToMenu(View view){
        finish();
    }


}