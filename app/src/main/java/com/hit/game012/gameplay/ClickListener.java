package com.hit.game012.gameplay;

import com.hit.game012.gamelogic.game.Index;

@FunctionalInterface
public interface ClickListener {
    void onTileClick(Index index);
}
