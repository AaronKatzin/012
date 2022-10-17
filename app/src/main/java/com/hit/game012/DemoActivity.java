package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.hit.game012.demo.DemoBoardView;
import com.hit.game012.startupsequence.AnimatedTextView;

public class DemoActivity extends AppCompatActivity {
    private AnimatedTextView inGameMessageView;
    private DemoBoardView boardView;
    int[] demoMessagesResIDPerMove = {
            R.string.demo_msg_1,
            R.string.demo_msg_2,
            R.string.demo_msg_3,
            R.string.demo_msg_4,
            R.string.demo_msg_5,
            R.string.demo_msg_6,
            R.string.demo_msg_7,
            R.string.demo_msg_8,
            R.string.demo_msg_9,
            R.string.demo_msg_10
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        init();
        boardView = new DemoBoardView();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.demo_fragment_frame, boardView,boardView.getClass().getSimpleName())
                .addToBackStack(null).commit();
        setMessage(-1);


    }
    private void init(){
        inGameMessageView = findViewById(R.id.demo_in_game_message);
    }
    public void setMessage(int turn){
        if (turn< demoMessagesResIDPerMove.length){
            inGameMessageView.setText(demoMessagesResIDPerMove[turn+1]);
            inGameMessageView.setTextSize(30);
            inGameMessageView.initAnimation(1000,200);
        }
    }
    public void endDemoGame(){
        inGameMessageView.setText(R.string.demo_end_msg);
        inGameMessageView.setTextSize(30);
        inGameMessageView.initAnimation(1000,200);

    }
    public void backToMenu(View view){
        finish();
    }
}