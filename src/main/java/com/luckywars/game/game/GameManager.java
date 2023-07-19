package com.luckywars.game.game;

import com.luckywars.game.LuckyWars;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameManager {

    private final Map<UUID, Game> games;

    public GameManager(){
        games = new HashMap<>();
    }

    public void createGame(GameMode mode, String map){
        UUID uuid = UUID.randomUUID();
        this.games.put(uuid, new Game(uuid, mode, map));
        LuckyWars.getInstance().getLogger().info("Created game for map: " + map);
    }

    public void deleteGame(UUID uuid){
        this.games.get(uuid).delete();
        this.games.remove(uuid);
    }

    public Game getGame(UUID uuid){
        return games.get(uuid);
    }

    public Game getGame(String game){
        for (Game game1 : games.values()){
            if (game1.getMap().equalsIgnoreCase(game)){
                return game1;
            }
        }
        return null;
    }

    public Map<UUID, Game> getGames(){
        return games;
    }
}