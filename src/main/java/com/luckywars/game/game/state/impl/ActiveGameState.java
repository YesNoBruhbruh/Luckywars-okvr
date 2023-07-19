package com.luckywars.game.game.state.impl;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.game.Game;
import com.luckywars.game.game.state.GameStateImpl;
import com.luckywars.game.game.state.GameStateListener;
import com.luckywars.game.game.state.impl.listeners.ActiveListener;
import com.luckywars.game.object.Pos;
import com.luckywars.game.region.Cuboid;
import com.luckywars.game.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;

public class ActiveGameState extends GameStateImpl {
    private Map<Integer, Set<UUID>> aliveTeams;

    private int playersAlive;

    @Override
    public void onEnable() {
        this.aliveTeams = new HashMap<>();
        this.playersAlive = game.getPlayers().size();
        for(int i = 1; i <= (game.getMode().getMaxPlayers() / game.getMode().getPlayersPerTeam()); i++){
            if(game.getTeams().get(i) != null && game.getTeams().get(i).size() != 0){
                this.aliveTeams.put(i, new HashSet<>());
                this.aliveTeams.put(i, game.getTeams().get(i));

                if (LuckyWars.getInstance().isDebugMessages()){
                    System.out.println("an alive team was created " + i);
                }
            }
        }

        for (Cuboid cuboid : game.getData().getCages()) {
            for (Pos pos : LocationUtils.posFromTwoPoints(cuboid.getPos1(), cuboid.getPos2())) {
                new Location(game.getWorld(), pos.getX(), pos.getY(), pos.getZ()).getBlock().setType(Material.AIR);
            }
        }

        for (UUID uuid : game.getPlayers().keySet()){
            if (Bukkit.getPlayer(uuid) != null){
                Bukkit.getPlayer(uuid).setGameMode(GameMode.SURVIVAL);
            }
        }
    }

    public void addDeath(){
        this.playersAlive--;
    }

    @Override
    public void onDisable() {

    }

    @Override
    public GameStateListener getListener(Game game) {
        return new ActiveListener(game, this);
    }

    public Map<Integer, Set<UUID>> getAliveTeams() {
        return aliveTeams;
    }

    public int getPlayersAlive() {
        return playersAlive;
    }
}