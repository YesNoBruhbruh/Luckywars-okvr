package com.luckywars.game.commands.subcommands;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.commands.SubCommand;
import com.luckywars.game.game.Game;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.entity.Player;

public class GameDeleteCommand extends SubCommand {
    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "&cDeletes the game you selected";
    }

    @Override
    public String getSyntax() {
        return "/luckywars delete <game>";
    }

    @Override
    public String getPermission() {
        return "luckywars.command.delete";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length < 2){
            player.sendMessage(MessageUtils.color("&cCorrect usage is " + getSyntax() + "&c!"));
            return;
        }

        if (LuckyWars.getInstance().getGameManager().getGames().size() == 0){
            player.sendMessage(MessageUtils.color("&cNo games setup!"));
            return;
        }

        Game gameToDelete = LuckyWars.getInstance().getGameManager().getGame(args[1]);

        if (gameToDelete == null){
            player.sendMessage(MessageUtils.color("&cThe game you are trying to delete does not exist smh!@!!"));
            return;
        }

        LuckyWars.getInstance().getGameManager().deleteGame(gameToDelete.getUuid());
        player.sendMessage(MessageUtils.color("&aGame deleted!"));
    }
}