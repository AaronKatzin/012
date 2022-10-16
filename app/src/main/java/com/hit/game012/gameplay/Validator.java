package com.hit.game012.gameplay;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.fragment.app.FragmentActivity;

import com.hit.game012.BoardActivity;
import com.hit.game012.ChooseBoardSize;
import com.hit.game012.R;
import com.hit.game012.Win;
import com.hit.game012.gamelogic.checker.BoardChecker;
import com.hit.game012.gamelogic.game.Board;

import java.util.Random;
import java.util.concurrent.Callable;

public class Validator implements Runnable {
    private final long VALIDATOR_DELAY = 1000;
    private BoardChecker checker;
    private FragmentActivity activity;
    private Random r = new Random();
    private View view;

    public Validator(FragmentActivity activity, View view) {
        this.activity = activity;
        this.view = view;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(VALIDATOR_DELAY); //1 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Board board = BoardView.getBoard();
        synchronized (board) {
            checker = new BoardChecker(board);
        }

        if (checker.isSolvedBoolean()) //win
        {
            System.out.println("win");
            ((BoardActivity) activity).setInGameMessage(getWinMessage(), 40);
            ((BoardActivity) activity).setEndGameGif(true);

        } else {
            System.out.println("lose");
            ((BoardActivity) activity).setInGameMessage(getLoseMessage(), 40);
            ((BoardActivity) activity).setEndGameGif(false);
//            return false;

        }

    }

    private int getWinMessage() {
        int[] winStringMessages = {R.string.win_great, R.string.win_good_job, R.string.win_excellent, R.string.win_amazing};
        int messageId = r.nextInt(winStringMessages.length);
        return winStringMessages[messageId];
    }
    private int getLoseMessage() {
        int[] loseStringMessages = {R.string.lose_next_time, R.string.lose_not_good, R.string.lose_practice};
        int messageId = r.nextInt(loseStringMessages.length);
        return loseStringMessages[messageId];
    }


}
