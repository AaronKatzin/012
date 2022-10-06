package com.hit.game012;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.solver.Hint;
import com.hit.game012.gameplay.BoardView;
import com.hit.game012.gameplay.GamePlay;

import java.util.List;

public class BoardActivity extends AppCompatActivity {

    private GamePlay gamePlay;
    private static TextView inGameMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        int boardSize = (int) getIntent().getExtras().get("size");

        // show message
        inGameMessage = findViewById(R.id.in_game_message);
        resetInGameMessage(boardSize);

        gamePlay = new GamePlay();
        gamePlay.startGame(boardSize);

        // show board fragment
        Fragment boardFragment = new BoardView(gamePlay.getBoard());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, boardFragment, boardFragment.getClass().getSimpleName())
                .addToBackStack(null)
                .commit();



    }
    public void backToMenu(View view){
        finish();
    }
    public void getHint(View view){
        Hint hint = gamePlay.requestHint();
        String message = hint.getMessage();
        List<Index> involvedTiles = hint.getInvolvedTiles();
        if (!involvedTiles.isEmpty()){
            //highlight the involved tiles
        }
        inGameMessage.setTextSize(30);
        inGameMessage.setText(message);
    }
    public void undo(View view){

    }
    public void resetInGameMessage(int boardSize){
        inGameMessage.setTextSize(40);
        inGameMessage.setText(boardSize+" X "+ boardSize);
    }
}