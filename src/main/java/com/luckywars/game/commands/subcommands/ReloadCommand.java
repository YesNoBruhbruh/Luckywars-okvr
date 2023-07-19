package com.luckywars.game.commands.subcommands;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.commands.SubCommand;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.entity.Player;

public class ReloadCommand extends SubCommand {

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "&cReloads the config file, this may cause issues!";
    }

    @Override
    public String getSyntax() {
        return "/luckywars reload";
    }

    @Override
    public String getPermission() {
        return "luckywars.command.reload";
    }

    @Override
    public void perform(Player player, String[] args) {
        player.sendMessage(MessageUtils.color("&eReloading config.yml..."));
        LuckyWars.getInstance().reloadConfig();
        player.sendMessage(MessageUtils.color("&aConfig.yml has been reloaded! Restart server if you experience any issues!"));
    }
}