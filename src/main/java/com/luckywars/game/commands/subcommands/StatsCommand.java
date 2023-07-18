package com.luckywars.game.commands.subcommands;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.commands.SubCommand;
import com.luckywars.game.object.GamePlayer;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class StatsCommand extends SubCommand {
    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String getDescription() {
        return "&aGets the stats of the player";
    }

    @Override
    public String getSyntax() {
        return "/lw stats <playerName>";
    }

    @Override
    public String getPermission() {
        return "luckywars.command.stats";
    }

    @Override
    public void perform(Player player, String[] args) {
        List<String> stats = new ArrayList<>();

        if (args.length == 1){
            GamePlayer gamePlayer = LuckyWars.getInstance().getGamePlayerDB().getGamePlayerStatsFromDB(player.getUniqueId().toString());

            if (gamePlayer == null){
                player.sendMessage(MessageUtils.color("&cYou aren't a gamePlayer!"));
                return;
            }

            stats.add("&cKills: " + gamePlayer.getKills());
            stats.add("&eLuckyBlocks: " + gamePlayer.getLuckyBlocksOpened());
            stats.add("&7Deaths: " + gamePlayer.getDeaths());
            stats.add("&0Losses: " + gamePlayer.getLosses());
            stats.add("&6Wins: " + gamePlayer.getWins());

            MessageUtils.sendMessageWithLines(player, stats);
        }else if (args.length == 2){
            if (Bukkit.getOfflinePlayer(args[1]) == null){
                player.sendMessage(MessageUtils.color("&cThat guy doesn't even exist!"));
                return;
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);

            GamePlayer targetPlayer = LuckyWars.getInstance().getGamePlayerDB().getGamePlayerStatsFromDB(offlinePlayer.getUniqueId().toString());

            if (targetPlayer == null){
                return;
            }

            stats.add("&cKills: " + targetPlayer.getKills());
            stats.add("&eLuckyBlocks: " + targetPlayer.getLuckyBlocksOpened());
            stats.add("&7Deaths: " + targetPlayer.getDeaths());
            stats.add("&0Losses: " + targetPlayer.getLosses());
            stats.add("&6Wins: " + targetPlayer.getWins());

            MessageUtils.sendMessageWithLines(player, stats);
        }else{
            player.sendMessage(MessageUtils.color("&c" + getSyntax()));
        }
    }
}
