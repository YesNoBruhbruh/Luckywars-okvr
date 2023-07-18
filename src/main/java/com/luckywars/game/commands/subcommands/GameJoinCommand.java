package com.luckywars.game.commands.subcommands;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.commands.SubCommand;
import com.luckywars.game.game.Game;
import com.luckywars.game.game.GameMode;
import com.luckywars.game.object.GamePlayer;
import com.luckywars.game.party.Party;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class GameJoinCommand extends SubCommand {
    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "&aYou will join the game you want to join?";
    }

    @Override
    public String getSyntax() {
        return "/luckywars join <game>";
    }

    @Override
    public String getPermission() {
        return "luckywars.command.join";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length < 2){
            player.sendMessage(MessageUtils.color("&cCorrect usage is " + getSyntax() + "&c!"));
            return;
        }

//        Game game = LuckyWars.getInstance().getGameManager().getGame(args[1]);
//
//        if (game == null){
//            player.sendMessage(MessageUtils.color("&cThis game doesn't exist!"));
//            return;
//        }
//
//        if (game.getState() == GameState.ACTIVE){
//            player.sendMessage(MessageUtils.color("&cThis game is currently being used!"));
//            return;
//        }

        Game game = new Game(UUID.randomUUID(), GameMode.DUOS, "candylane");
        LuckyWars.getInstance().getGameManager().getGames().put(game.getUuid(), game);

        GamePlayer gamePlayer = LuckyWars.getInstance().getGamePlayer(player.getUniqueId());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (gamePlayer.isInParty()){
                    Party party = LuckyWars.getInstance().getPartyManager().getParty(gamePlayer.getPartyId());

                    if (game.getMode() == GameMode.DUELS || game.getMode() == GameMode.SOLO){
                        party.joinGame(game);
                    }else{
                        game.addParty(party);
                    }

                    player.sendMessage(MessageUtils.color("&aAdding your party to the game..."));
                }else{
                    game.addPlayer(player);
                }
            }
        }.runTaskLater(LuckyWars.getInstance(), 20*3);
    }
}