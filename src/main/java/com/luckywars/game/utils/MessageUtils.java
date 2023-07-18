package com.luckywars.game.utils;

import com.luckywars.game.object.GamePlayer;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MessageUtils {

    public static String color(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> color(List<String> strings){
        List<String> lines = new ArrayList<>();
        for (String str : strings){
            lines.add(color(str));
        }
        return lines;
    }

    public static String format(String message){
        return color("&7[&6LuckyWars&7]&r " + message);
    }

    public static TextComponent button(String text, boolean bold, net.md_5.bungee.api.ChatColor color, String command, String hoverMsg){
        TextComponent component = new TextComponent(text);
        component.setBold(bold);
        component.setColor(color);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(hoverMsg).color(net.md_5.bungee.api.ChatColor.GRAY).italic(true).create()));

        return component;
    }

    public static void broadcast(String message, Player... players){
        for (Player player : players) {
            player.sendMessage(color(message));
        }
    }

    public static void broadcast(String message, Map<UUID, Integer> players){
        for(UUID uuid : players.keySet()){
            if (uuid != null && Bukkit.getPlayer(uuid) != null){
                Bukkit.getPlayer(uuid).sendMessage(color(message));
            }
        }
    }

    public static void broadcast(String message, List<GamePlayer> gamePlayers){
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gamePlayer.getOfflinePlayer().getPlayer() != null){
                gamePlayer.getOfflinePlayer().getPlayer().sendMessage(color(message));
            }
        }
    }

    public static void broadcast(String message){
        Bukkit.broadcastMessage(color(message));
    }

    public static void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut, Player... players){
        if(title != null && subTitle != null){
            PacketPlayOutTitle titlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a(getJsonMessage(title)));
            PacketPlayOutTitle subTitlePacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a(getJsonMessage(title)));
            PacketPlayOutTitle timingsPacket = new PacketPlayOutTitle(fadeIn, stay, fadeOut);

            for (Player player : players) {
                PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                connection.sendPacket(timingsPacket);
                connection.sendPacket(titlePacket);
                connection.sendPacket(subTitlePacket);
            }
        }
    }

    public static void sendActionbar(String message, Player... players){
        if(message != null){
            PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(getJsonMessage(message)), (byte) 2);

            for (Player player : players) {
                PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                connection.sendPacket(packet);
            }
        }
    }

    private static String getJsonMessage(String message){
        return "{\"text\":\"" + color(message) + "\"}";
    }

    public static void sendMessageWithLines(Player player, String message){
        if (player != null){
            player.sendMessage(color("&6&m-------------------------"));
            player.sendMessage(color(message));
            player.sendMessage(color("&6&m-------------------------"));
        }
    }

    public static void sendMessageWithLines(Player player, List<String> messages){
        if (player != null){
            player.sendMessage(color("&6&m-------------------------"));
            for (String message : messages){
                player.sendMessage(color(message));
            }
            player.sendMessage(color("&6&m-------------------------"));
        }
    }
}