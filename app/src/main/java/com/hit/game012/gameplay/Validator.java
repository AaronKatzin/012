package com.hit.game012.gameplay;

import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.hit.game012.BoardActivity;
import com.hit.game012.R;
import com.hit.game012.gamelogic.checker.BoardChecker;
import com.hit.game012.gamelogic.game.Board;

import java.util.Random;

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

        if (checker.isSolved()) //win
        {
            System.out.println("win");
            ((BoardActivity) activity).setInGameMessage(getWinMessage(), 40);
            ((BoardActivity) activity).setEndGameGif(true);
        }
        else {
            System.out.println("lose");
            ((BoardActivity) activity).setInGameMessage(getLoseMessage(), 40);
            ((BoardActivity) activity).setEndGameGif(false);
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
