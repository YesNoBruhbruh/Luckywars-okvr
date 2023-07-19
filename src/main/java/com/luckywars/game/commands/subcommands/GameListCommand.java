package com.luckywars.game.commands.subcommands;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.commands.SubCommand;
import com.luckywars.game.game.Game;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.entity.Player;

public class GameListCommand extends SubCommand {
    @Override
    public String getName() {
        return "gamelist";
    }

    @Override
    public String getDescription() {
        return "&aSends the player the list of setup games!";
    }

    @Override
    public String getSyntax() {
        return "/luckywars gamelist";
    }

    @Override
    public String getPermission() {
        return "luckywars.command.gamelist";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (LuckyWars.getInstance().getGameManager().getGames().size() == 0){
            player.sendMessage(MessageUtils.color("&cNo games setup!"));
            return;
        }

        for (Game game : LuckyWars.getInstance().getGameManager().getGames().values()){
            player.sendMessage(MessageUtils.color("&a" + game.getMap()));
        }
    }
}
