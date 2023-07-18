package com.luckywars.game.game.state.impl;

import com.luckywars.game.game.Game;
import com.luckywars.game.game.state.GameStateImpl;
import com.luckywars.game.game.state.GameStateListener;

public class NoneGameState extends GameStateImpl {

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public GameStateListener getListener(Game game) {
        return null;
    }

}
