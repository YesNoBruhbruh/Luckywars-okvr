package com.luckywars.game.commands.subcommands;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.commands.SubCommand;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.entity.Player;

public class ToggleBuildCommand extends SubCommand {
    @Override
    public String getName() {
        return "build";
    }

    @Override
    public String getDescription() {
        return "&aIf you toggle this on you will be able to break blocks!";
    }

    @Override
    public String getSyntax() {
        return "/luckywars build";
    }

    @Override
    public String getPermission() {
        return "luckywars.command.build";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission(getPermission())){
            player.sendMessage(MessageUtils.color("&cYou do not have the necessary permission to use this command!"));
            return;
        }

        if (LuckyWars.getInstance().getInBuildSession().contains(player.getUniqueId())){
            //this means that they are in build session already and want to toggle it off.
            LuckyWars.getInstance().getInBuildSession().remove(player.getUniqueId());
            player.sendMessage(MessageUtils.color("&7You have toggled your build off!"));
        }else{
            LuckyWars.getInstance().getInBuildSession().add(player.getUniqueId());
            player.sendMessage(MessageUtils.color("&aYou can now break and place blocks!"));
        }
    }
}
