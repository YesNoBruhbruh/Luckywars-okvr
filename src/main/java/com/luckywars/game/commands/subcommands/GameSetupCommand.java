package com.luckywars.game.commands.subcommands;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.commands.SubCommand;
import com.luckywars.game.game.Game;
import com.luckywars.game.game.GameMode;
import com.luckywars.game.utils.MessageUtils;
import com.luckywars.game.utils.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class GameSetupCommand extends SubCommand {

    @Override
    public String getName() {
        return "setup";
    }

    @Override
    public String getDescription() {
        return "&aSends you to your desired map of setupping? (idk if I spelled that right)";
    }

    @Override
    public String getSyntax() {
        return "/luckywars setup <map> <gameMode>";
    }

    @Override
    public String getPermission() {
        return "luckywars.command.setup";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length < 3){
            player.sendMessage(MessageUtils.color("&cCorrect usage is " + getSyntax() + "&c!"));
            return;
        }

        if (LuckyWars.getInstance().getWizardManager().hasSession(player.getUniqueId())){
            player.sendMessage(MessageUtils.color("&cYou already have a running session!"));
            return;
        }

        World world = Bukkit.getWorld(args[1]);

        if (world == null){
            player.sendMessage(MessageUtils.color("&cThat world does not exist!"));
            return;
        }

        if (WorldUtils.worldExists(args[1])){
            player.sendMessage(MessageUtils.color("&cThe world &e" + args[1] + " &cdoes not exist in the database!"));
            return;
        }

        Game gameToSetup = LuckyWars.getInstance().getGameManager().getGame(args[1]);

        if (gameToSetup != null){
            player.sendMessage(MessageUtils.color("&cThis world already exists in the database!"));
            return;
        }

        GameMode gameMode = GameMode.valueOf(args[2].toUpperCase());

        if (gameMode == null){
            player.sendMessage(MessageUtils.color("&cThat gameMode doesn't exist! if you think this is an error, contact developers!"));
            return;
        }

        LuckyWars.getInstance().getWizardManager().createSession(player, args[1], gameMode);
    }
}