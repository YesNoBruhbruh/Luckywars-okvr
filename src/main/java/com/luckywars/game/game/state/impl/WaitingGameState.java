package com.luckywars.game.game.state.impl;

import com.luckywars.game.game.Game;
import com.luckywars.game.game.state.GameStateImpl;
import com.luckywars.game.game.state.GameStateListener;
import com.luckywars.game.game.state.impl.listeners.PreGameListener;

public class WaitingGameState extends GameStateImpl {

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public GameStateListener getListener(Game game) {
        return new PreGameListener(game);
    }

}
