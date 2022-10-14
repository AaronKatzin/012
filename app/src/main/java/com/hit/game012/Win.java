package com.hit.game012;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.hit.game012.ui.MyListAdapter;

import java.util.Random;

public class Win extends AppCompatActivity {
    ListView list;

    String[] maintitle ={
            "Title 1","Title 2",
            "Title 3","Title 4",
            "Title 5",
            "Title 1","Title 2",
            "Title 3","Title 4",
            "Title 5",
    };

    String[] subtitle ={
            "Sub Title 1","Sub Title 2",
            "Sub Title 3","Sub Title 4",
            "Sub Title 5",
            "Sub Title 1","Sub Title 2",
            "Sub Title 3","Sub Title 4",
            "Sub Title 5",
    };

    Integer[] imgid={
            R.drawable.padlock,R.drawable.padlock,
            R.drawable.padlock,R.drawable.padlock,
            R.drawable.padlock,
            R.drawable.padlock,R.drawable.padlock,
            R.drawable.padlock,R.drawable.padlock,
            R.drawable.padlock,
    };

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

        MyListAdapter adapter=new MyListAdapter(this, maintitle, subtitle,imgid);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    public void newGame(View view){
        Intent intent = new Intent(this, ChooseBoardSize.class);
        startActivity(intent);
    }

}