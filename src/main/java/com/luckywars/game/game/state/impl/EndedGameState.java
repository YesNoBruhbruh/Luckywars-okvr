package com.luckywars.game.game.state.impl;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.game.Game;
import com.luckywars.game.game.state.GameStateImpl;
import com.luckywars.game.game.state.GameStateListener;
import com.luckywars.game.game.state.impl.listeners.PreGameListener;
import com.luckywars.game.object.GamePlayer;
import com.luckywars.game.utils.MessageUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class EndedGameState extends GameStateImpl {
    //TODO MAKE A WAY TO STORE KILLS AND DEATHS WITHIN THE GAME ITSELF.
    //TODO THEN GRAB IT THAT WAY.
    //TODO BECAUSE THE MOST KILLS LEADERBOARD DOESN'T WORK THE WAY IT WORKS RIGHT NOW.

    @Override
    public void onEnable() {
        // Win effects
        //TODO do cosmetics
        //TODO after some seconds or something remove them from all the lists they were added and teleport them back to lobby
        //TODO check in onEnable if lobby location is not set and if it isn't then force them to set it >:)

        // this checks for all the players and updates their stats.
        for (UUID playerUUID : game.getPlayers().keySet()){
            GamePlayer gamePlayer = LuckyWars.getInstance().getGamePlayer(playerUUID);

            LuckyWars.getInstance().getGamePlayerDB().updateGamePlayerStats(gamePlayer);

            if (gamePlayer.getOfflinePlayer().isOnline()){
                gamePlayer.getOfflinePlayer().getPlayer().sendMessage(MessageUtils.color("&aUpdating game stats..."));

                gamePlayer.getOfflinePlayer().getPlayer().sendMessage(MessageUtils.color("&bWould you like to see your stats?"));
                gamePlayer.getOfflinePlayer().getPlayer().spigot().sendMessage(MessageUtils.button(
                        "CLICK HERE",
                        true,
                        ChatColor.GOLD,
                        "/lw stats",
                        "Click here to see your stats!"
                ));
            }
        }

        Bukkit.getScheduler().runTaskLater(LuckyWars.getInstance(), () -> {
            for (Player player : game.getWorld().getPlayers()){
                if (player != null){

                    if (game.getPlayers().containsKey(player.getUniqueId())){
                        game.removePlayer(player);
                    } else {
                        player.sendMessage(MessageUtils.color("&cHow did you get here?"));
                    }
                }
            }
        }, 20*5);
    }

    @Override
    public void onDisable() {
        game.reset();

        if (LuckyWars.getInstance().isDebugMessages()){
            System.out.println("onDisable actually works even though I've never used it");
        }
    }

    @Override
    public GameStateListener getListener(Game game) {
        return new PreGameListener(game);
    }
}