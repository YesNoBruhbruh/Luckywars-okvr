package com.luckywars.game.game.state.impl;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.game.Game;
import com.luckywars.game.game.state.GameState;
import com.luckywars.game.game.state.GameStateImpl;
import com.luckywars.game.game.state.GameStateListener;
import com.luckywars.game.game.state.impl.listeners.PreGameListener;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class StartingGameState extends GameStateImpl {

    private int secondsLeft = 10;
    private BukkitTask task;

    @Override
    public void onEnable() {
        task = Bukkit.getScheduler().runTaskTimer(LuckyWars.getInstance(), () -> {
            if (game.getPlayers().size() < game.getMode().getMinPlayers()){
                task.cancel();
                game.setGameState(GameState.WAITING);
            }
            if(secondsLeft == 0){
                game.setGameState(GameState.ACTIVE);
            }
            secondsLeft--;
        }, 0, 20);
    }

    @Override
    public void onDisable() {
        if(task != null){
            task.cancel();
            task = null;
        }
    }

    @Override
    public GameStateListener getListener(Game game) {
        return new PreGameListener(game);
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }
}