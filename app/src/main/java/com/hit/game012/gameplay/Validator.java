package com.hit.game012.gameplay;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.fragment.app.FragmentActivity;

import com.hit.game012.BoardActivity;
import com.hit.game012.R;
import com.hit.game012.gamelogic.checker.BoardChecker;
import com.hit.game012.gamelogic.game.Board;

import java.util.Random;
import java.util.concurrent.Callable;

public class Validator implements Callable<Boolean> {
    private final long VALIDATOR_DELAY = 700;
    private BoardChecker checker;
    private FragmentActivity activity;

    public Validator(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public Boolean call() {

        try {
            Thread.sleep(VALIDATOR_DELAY); //1 second

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Board board = BoardView.getBoard();
        synchronized (board){
            checker = new BoardChecker(board);
            System.out.println(board);

        }
        if (checker.isSolvedBoolean()) //win
        {
            System.out.println("win");
            int[] winStringMessages={R.string.win_great,R.string.win_good_job,R.string.win_excellent,R.string.win_amazing};
            Random r=new Random();
            int message=r.nextInt(winStringMessages.length);
            ((BoardActivity) activity).setInGameMessage(winStringMessages[message],40);
            return true;

        } else {
            System.out.println("lose");
            int[] loseStringMessages={R.string.lose_next_time,R.string.lose_not_good,R.string.lose_practice};
            Random r=new Random();
            int message=r.nextInt(loseStringMessages.length);
            ((BoardActivity) activity).setInGameMessage(loseStringMessages[message],40);
            return false;

        }

    }

}
