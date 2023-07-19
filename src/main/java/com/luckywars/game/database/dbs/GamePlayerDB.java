package com.luckywars.game.database.dbs;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.object.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class GamePlayerDB {

    private final Connection connection;

    public GamePlayerDB(Connection connection){
        this.connection = connection;
    }

    public GamePlayer getGamePlayerStatsFromDB(String uuid){
        try{
            PreparedStatement ps = connection.prepareStatement("SELECT * from player_stats WHERE UUID=?");
            ps.setString(1, uuid);

            ResultSet results = ps.executeQuery();

            if (results.next()){

                GamePlayer gamePlayer = new GamePlayer(UUID.fromString(results.getString("UUID")));

                gamePlayer.setKills(results.getInt("KILLS"));
                gamePlayer.setLuckyBlocksOpened(results.getInt("LUCKYBLOCKS"));
                gamePlayer.setDeaths(results.getInt("DEATHS"));
                gamePlayer.setWins(results.getInt("WINS"));
                gamePlayer.setLosses(results.getInt("LOSSES"));

                ps.close();

                return gamePlayer;
            }

        } catch (SQLException e){
            Bukkit.getLogger().severe("There has been a problem with getting the player stats from DB!");
            e.printStackTrace();
        }
        return null;
    }

    public void createGamePlayerStats(GamePlayer gamePlayer){
        try{
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO player_stats (UUID, KILLS, LUCKYBLOCKS, DEATHS, WINS, LOSSES) VALUES (?, ?, ?, ?, ?, ?)");

            ps.setString(1, gamePlayer.getUUID().toString());
            ps.setInt(2, gamePlayer.getKills());
            ps.setInt(3, gamePlayer.getLuckyBlocksOpened());
            ps.setInt(4, gamePlayer.getDeaths());
            ps.setInt(5, gamePlayer.getWins());
            ps.setInt(6, gamePlayer.getLosses());

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            Bukkit.getLogger().severe("There has been a problem with creating player stats from DB!");
            e.printStackTrace();
        }
    }

    public void updateGamePlayerStats(GamePlayer gamePlayer){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE player_stats SET KILLS = ?, LUCKYBLOCKS = ?, DEATHS = ?, WINS = ?, LOSSES = ? WHERE UUID = ?");

            ps.setInt(1, gamePlayer.getKills());
            ps.setInt(2, gamePlayer.getLuckyBlocksOpened());
            ps.setInt(3, gamePlayer.getDeaths());
            ps.setInt(4, gamePlayer.getWins());
            ps.setInt(5, gamePlayer.getLosses());
            ps.setString(6, gamePlayer.getUUID().toString());

            ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("There has been a problem with updating player stats from DB!");
            e.printStackTrace();
        }
    }

    public void deleteGamePlayerStatsFromDB(GamePlayer gamePlayer){
        try{
            PreparedStatement ps = connection.prepareStatement("DELETE FROM player_stats WHERE UUID=?");

            ps.setString(1, gamePlayer.getUUID().toString());

            ps.executeUpdate();

            ps.close();

        } catch (SQLException e) {
            Bukkit.getLogger().severe("There has been a problem with deleting player stats from DB!");
            e.printStackTrace();
        }
    }
}