package com.hit.game012.gameplay;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hit.game012.R;
import com.hit.game012.gamelogic.checker.BoardChecker;

import java.util.concurrent.Callable;

public class Validator implements Callable<Boolean> {
    private final long VALIDATOR_DELAY   = 700;
    private BoardChecker checker;
//    private Context context;
//    private View view;

//    public Validator( View view) {
//        this.context=view.getContext();
//        this.view=view;
//    }

    @Override
    public Boolean call() {
        try {
            Thread.sleep(VALIDATOR_DELAY); //1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        checker=new BoardChecker(BoardView.getBoard());
        System.out.println(checker.isSolvedBoolean());
        if(checker.isSolvedBoolean()) //win
        {
            System.out.println("win");
            return true;
            //start win animation
            //move to next page
        }
        else{
            System.out.println("lose");
            return false;
            //start lose animation
            //move to next page
        }

    }

}
