package com.luckywars.game.game.state.impl;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.game.Game;
import com.luckywars.game.game.state.GameState;
import com.luckywars.game.game.state.GameStateImpl;
import com.luckywars.game.game.state.GameStateListener;
import com.luckywars.game.luckyblock.LuckyBlock;
import com.luckywars.game.object.Pos;
import com.luckywars.game.utils.LocationUtils;
import org.bukkit.Bukkit;

public class LoadingGameState extends GameStateImpl {

    @Override
    public void onEnable() {

        Bukkit.getScheduler().runTaskLater(LuckyWars.getInstance(), () -> {
            int id = 1;
            for(Pos pos : game.getData().getLuckyBlocks()){
                new LuckyBlock(LocationUtils.posToLocation(pos, game.getWorld()), id);
                id++;
            }
        }, 20);

        game.setGameState(GameState.WAITING);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public GameStateListener getListener(Game game) {
        return null;
    }

}
