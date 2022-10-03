package com.hit.game012.gameplay;

import androidx.fragment.app.Fragment;

import com.hit.game012.gamelogic.game.Board;
import com.hit.game012.gamelogic.game.Index;

import java.util.HashSet;
import java.util.Set;
import javax.swing.*;

public class BoardView extends Fragment {
    private Board board;
    private Set<Index> highlightedIndexes;
    private Set<ClickListener> ClickListeners;

    public BoardView(Board board) {
        this.board = board;
        highlightedIndexes = new HashSet<>();
        ClickListeners = new HashSet<>();
        int size = board.getSize();

    }
}
