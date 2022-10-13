package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

public class Win extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        long gameTime;
        int hintCounter;

        if(false) {
            gameTime = (long) getIntent().getExtras().get("gameTime");
            hintCounter = (int) getIntent().getExtras().get("hintCounter");
        }
        else{
            gameTime = 100;
            hintCounter = 1;
        }

        long score = gameTime + hintCounter;

        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText("gameTime: " + gameTime + "\n hintCounter: " + hintCounter+ "\n score: " + score);

        createTable(5,2);
    }

    public void newGame(View view){
        Intent intent = new Intent(this, ChooseBoardSize.class);
        startActivity(intent);
    }

    private void createTable(int rows, int columns){

        ScrollView sv = findViewById(R.id.multiplayerHighScoreTable); // TODO type

        //ListView DynamicListView = new ListView(this);
        ListView DynamicListView = findViewById(R.id.DynamicListView);
//        DynamicListView.LayoutParams(new ViewGroup.LayoutParams(100,100));

        final String[] DynamicListElements = new String[] {
                "Android",
                "PHP",
                "Android Studio",
                "PhpMyAdmin"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (Win.this, android.R.layout.simple_list_item_1, DynamicListElements);

        DynamicListView.setAdapter(adapter);

//        DynamicListView.setLayoutParams(new ListView.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, 300));
//        ListView.LayoutParams params= (ListView.LayoutParams) DynamicListView.getLayoutParams();
//        params.width=300;
//        params.height=600;
//        DynamicListView.setLayoutParams(params);
        DynamicListView.setHeaderDividersEnabled(true);
        DynamicListView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        //sv.addView(DynamicListView);

//        linearLayout.removeAllViews();
//        this.setContentView(linearLayout, new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

//        TableLayout table = new TableLayout(this);
//        table.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        table.setShrinkAllColumns(true);
//        table.setStretchAllColumns(true);
        
//        Random random = new Random();
//
//        for (int i=0; i < rows; i++) {
//            TableRow row = new TableRow(Win.this);
//            for (int j=0; j < columns; j++) {
//                int value = random.nextInt(100) + 1;
//                TextView tv = new TextView(Win.this);
//                tv.setText(String.valueOf(value));
//                row.addView(tv);
//            }
//            table.addView(row);
//        }
//
//        ScrollView tableScroller = findViewById(R.id.multiplayerHighScoreTable);
//        tableScroller.addView(table);
    }
}