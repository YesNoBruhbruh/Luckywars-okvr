package com.luckywars.game.utils;

import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimePropertyMap;

import com.luckywars.game.LuckyWars;

import com.luckywars.game.game.Game;
import com.luckywars.game.game.state.GameState;

import org.bukkit.Bukkit;

import java.io.IOException;

public class WorldUtils {

    private static final SlimeLoader loader = LuckyWars.getInstance().getSlimePlugin().getLoader("mysql");
    private static int id = 0;

    public static void loadGameWorld(Game game){
        Bukkit.getScheduler().runTaskAsynchronously(LuckyWars.getInstance(), () -> {
            try {
                SlimeWorld world = LuckyWars.getInstance().getSlimePlugin().loadWorld(loader, game.getMap(), true, new SlimePropertyMap()).clone(game.getMap() + "-" + id);


                LuckyWars.getInstance().getSlimePlugin().generateWorld(world);

                id++;

                Bukkit.getScheduler().runTaskLater(LuckyWars.getInstance(), () -> {
                    game.setWorld(Bukkit.getWorld(world.getName()));
                    game.setGameState(GameState.LOADING);
                }, 40);
            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException |
                     WorldInUseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void loadWorld(String name){
        Bukkit.getScheduler().runTaskAsynchronously(LuckyWars.getInstance(), () -> {
            try {
                SlimeWorld world = LuckyWars.getInstance().getSlimePlugin().loadWorld(loader, name, true, new SlimePropertyMap());

                LuckyWars.getInstance().getSlimePlugin().generateWorld(world);

            } catch (UnknownWorldException | IOException | CorruptedWorldException | NewerFormatException |
                     WorldInUseException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void unloadWorld(String name){
        try {
            Bukkit.unloadWorld(name, false);
        } catch (Exception ignored){}
    }

    public static boolean worldExists(String name){
        try {
            return !loader.worldExists(name);
        } catch (IOException e) {
            return true;
        }
    }

}
