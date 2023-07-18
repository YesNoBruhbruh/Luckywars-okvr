package com.luckywars.game.game.state;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public abstract class GameStateImpl {

    protected Game game;
    private GameStateListener listener;

    public void pre(Game game){
        this.game = game;
        GameStateListener listener1 = getListener(game);
        if(listener1 != null){
            Bukkit.getPluginManager().registerEvents(listener1, LuckyWars.getInstance());
            this.listener = listener1;
        }
        this.onEnable();
    }

    public void post(Game game){
        this.game = game;
        if(listener != null){
            HandlerList.unregisterAll(listener);
        }
        this.onDisable();
    }

    abstract public void onEnable();
    abstract public void onDisable();
    abstract public GameStateListener getListener(Game game);

}
