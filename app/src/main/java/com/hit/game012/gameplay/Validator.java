package com.hit.game012.gameplay;


import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.hit.game012.BoardActivity;
import com.hit.game012.R;
import com.hit.game012.gamelogic.checker.BoardChecker;
import com.hit.game012.gamelogic.game.Board;

import java.util.Random;

/**
 * Class to validate the board after its full.
 * The class uses BoardChecker isSolved function.
 * Implements Runnable to validate the board after a VALIDATOR_DELAY sleep.
 * The sleep allows the user to complete the move before validator starts.
 */
public class Validator implements Runnable {
    private final long VALIDATOR_DELAY = 1000; // 1 second
    private BoardChecker checker;
    private FragmentActivity activity;
    private Random r = new Random();

    public Validator(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void run() {
        // Sleep
        try {
            Thread.sleep(VALIDATOR_DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Feed current board to checker
        Board board = BoardView.getBoard();
        checker = new BoardChecker(board);

        if (checker.isSolvedBoolean()) {
            // Win
            ((BoardActivity) activity).setInGameMessage(getWinMessage(), 40);
            ((BoardActivity) activity).setEndGameGif(true);

        } else {
            // Lose
            ((BoardActivity) activity).setInGameMessage(getLoseMessage(), 40);
            ((BoardActivity) activity).setEndGameGif(false);
        }

    }

    /**
     * Randomly chooses a message to display after a win.
     * @return the resID of the message
     */
    private int getWinMessage() {
        int[] winStringMessages = {R.string.win_great, R.string.win_good_job, R.string.win_excellent, R.string.win_amazing};
        int messageId = r.nextInt(winStringMessages.length);
        return winStringMessages[messageId];
    }

    /**
     * Randomly chooses a message to display after a lose.
     * @return the resID of the message
     */
    private int getLoseMessage() {
        int[] loseStringMessages = {R.string.lose_next_time, R.string.lose_not_good, R.string.lose_practice};
        int messageId = r.nextInt(loseStringMessages.length);
        return loseStringMessages[messageId];
    }


}
