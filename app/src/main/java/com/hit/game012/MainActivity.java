package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.hit.game012.startupsequence.AnimatedImageView;
import com.hit.game012.startupsequence.AnimatedTextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        AnimatedImageView left = findViewById(R.id.left_tile);
        AnimatedImageView right = findViewById(R.id.right_tile);
        AnimatedTextView gameName = findViewById(R.id.app_name_startup);

        left.initStartupAnimation(1500,1000);
        right.initStartupAnimation(1500, 2000);
        gameName.initAnimation(2000, 3000);


    }
    private void init(){
        SharedPreferences sharedPref = this.getSharedPreferences("application", MODE_PRIVATE);
        Config.setGridThemeID(sharedPref.getInt("colorTheme", R.id.settings_color_theme_1));
        setTheme(Config.gridThemeID);
    }
    public void startGame(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}