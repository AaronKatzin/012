package com.hit.game012.demo;

import android.app.Activity;

import com.hit.game012.DemoActivity;
import com.hit.game012.gamelogic.game.Index;
import com.hit.game012.gamelogic.game.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class to validate a turn in the demo board.
 * Checks if all the participating tiles in the turn are in their final state.
 * If true:
 * - Increments the turn and start {@code nextTurn()} function in the adapter.
 * - Sleeps for DEMO_VALIDATOR_DELAY after last validation before starting the next activity.
 */
public class DemoValidator implements Runnable {
    private DemoGridAdapter adapter;
    private Activity activity;
    private List<Tile> turnTileState;
    private int DEMO_VALIDATOR_DELAY = 1500;


    public DemoValidator(Activity activity, DemoGridAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
    }


    @Override
    public void run() {
        boolean isValid = true;
        int turn = DemoGridAdapter.turn;
        // Get the final tiles color for this turn
        turnTileState = Arrays.asList(DemoGridAdapter.tilesInTurn.get(turn));

        // Get the indexes participation in this turn
        List<Index> indexList = Arrays.asList(DemoGridAdapter.indexesInTurn.get(turn));

        for (int i = 0; i < indexList.size(); i++) {
            // For each index in the turn we will check if the color is the final color
            Tile currentTile = DemoBoardView.board.getTile(indexList.get(i));
            if (!currentTile.equals(turnTileState.get(i))) {
                isValid = false;
            }
        }
        if (isValid) {
            DemoGridAdapter.turn += 1;
            adapter.nextTurn();
            if (DemoGridAdapter.turn == 10){
                try {
                    Thread.sleep(DEMO_VALIDATOR_DELAY);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ((DemoActivity) activity).endDemoGame();
            }

        }
    }
}
