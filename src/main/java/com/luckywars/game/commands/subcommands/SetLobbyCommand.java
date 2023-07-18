package com.luckywars.game.commands.subcommands;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.commands.SubCommand;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetLobbyCommand extends SubCommand {

    @Override
    public String getName() {
        return "setLobby";
    }

    @Override
    public String getDescription() {
        return (LuckyWars.getInstance().getLobbyLocation() != null) ? "&aLobby is already set!": "&cLobby is not yet set!";
    }

    @Override
    public String getSyntax() {
        return "/luckywars setLobby";
    }

    @Override
    public String getPermission() {
        return "luckywars.command.setlobby";
    }

    @Override
    public void perform(Player player, String[] args) {
        Location location = player.getLocation();

        LuckyWars.getInstance().getConfig().set("lobby-location.world", location.getWorld().getName());
        LuckyWars.getInstance().getConfig().set("lobby-location.x", location.getX());
        LuckyWars.getInstance().getConfig().set("lobby-location.y", location.getY());
        LuckyWars.getInstance().getConfig().set("lobby-location.z", location.getZ());
        LuckyWars.getInstance().getConfig().set("lobby-location.yaw", location.getYaw());
        LuckyWars.getInstance().getConfig().set("lobby-location.pitch", location.getPitch());

        LuckyWars.getInstance().saveConfig();
        LuckyWars.getInstance().reloadConfig();

        player.sendMessage(MessageUtils.color("&aSuccessfully set lobby location!"));
    }
}