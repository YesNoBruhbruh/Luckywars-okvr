package com.luckywars.game.commands;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.commands.subcommands.*;
import com.luckywars.game.commands.subcommands.GameListCommand;
import com.luckywars.game.utils.MessageUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandManager implements CommandExecutor {

    private final ArrayList<SubCommand> suggestAdminSubCommands = new ArrayList<>();
    private final ArrayList<SubCommand> instantAdminSubCommands = new ArrayList<>();
    private final ArrayList<SubCommand> normalSubCommands = new ArrayList<>();

    public CommandManager(){
        suggestAdminSubCommands.add(new GameSetupCommand());
        suggestAdminSubCommands.add(new GameDeleteCommand());

        instantAdminSubCommands.add(new GameListCommand());
        instantAdminSubCommands.add(new ReloadCommand());
        instantAdminSubCommands.add(new SetLobbyCommand());
        instantAdminSubCommands.add(new ToggleBuildCommand());
        instantAdminSubCommands.add(new GiveLocWandCommand());

        normalSubCommands.add(new GameJoinCommand());
        normalSubCommands.add(new PartyCommand());
        normalSubCommands.add(new StatsCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("luckywars.command.admin")){
            if (!player.hasPermission("luckywars.command.member")){
                player.sendMessage(MessageUtils.format("&cYou don't have permission for anything!"));
                return true;
            }

            if (args.length == 0){
                player.sendMessage(MessageUtils.color("&b&lLuckywars v1.0 - Member Commands"));
                player.sendMessage("");
                for (SubCommand normalSubCommand : normalSubCommands) {
                    player.sendMessage(MessageUtils.color("&e+ &7" + normalSubCommand.getSyntax() + " - " + normalSubCommand.getDescription()));
                }
            }else{
                for (int i = 0; i < normalSubCommands.size(); i++){
                    if (args[0].equalsIgnoreCase(getNormalSubCommands().get(i).getName()) && player.hasPermission(getNormalSubCommands().get(i).getPermission())){
                        getNormalSubCommands().get(i).perform(player, args);
                    }
                }
            }

        }else{
            if (args.length == 0){
                player.sendMessage(MessageUtils.color("&b&lLuckywars v1.0 - Admin Commands"));
                player.sendMessage("");
                for (int i = 0; i < getSuggestAdminSubCommands().size(); i++){
                    sendMessage(player, ClickEvent.Action.SUGGEST_COMMAND, getSuggestAdminSubCommands().get(i));
                }
                for (int i = 0; i < getInstantAdminSubCommands().size(); i++){
                    sendMessage(player, ClickEvent.Action.RUN_COMMAND, getInstantAdminSubCommands().get(i));
                }
                for (int i = 0; i < getNormalSubCommands().size(); i++){
                    sendMessage(player, ClickEvent.Action.SUGGEST_COMMAND, getNormalSubCommands().get(i));
                }
            }else{
                for (int i = 0; i < getSuggestAdminSubCommands().size(); i++){
                    if (args[0].equalsIgnoreCase(getSuggestAdminSubCommands().get(i).getName()) && player.hasPermission(getSuggestAdminSubCommands().get(i).getPermission())){
                        getSuggestAdminSubCommands().get(i).perform(player, args);
                    }
                }
                for (int i = 0; i < getInstantAdminSubCommands().size(); i++){
                    if (args[0].equalsIgnoreCase(getInstantAdminSubCommands().get(i).getName()) && player.hasPermission(getInstantAdminSubCommands().get(i).getPermission())){
                        getInstantAdminSubCommands().get(i).perform(player, args);
                    }
                }
                for (int i = 0; i < getNormalSubCommands().size(); i++){
                    if (args[0].equalsIgnoreCase(getNormalSubCommands().get(i).getName()) && player.hasPermission(getNormalSubCommands().get(i).getPermission())){
                        getNormalSubCommands().get(i).perform(player, args);
                    }
                }
            }
        }
        return true;
    }

    private void sendMessage(Player player, ClickEvent.Action action, SubCommand command) {
        TextComponent message = new TextComponent(MessageUtils.color("&e+ &7" + command.getSyntax() + " - " + command.getDescription()));
        message.setClickEvent(new ClickEvent(action, command.getSyntax()));
        player.spigot().sendMessage(message);
    }

    public ArrayList<SubCommand> getSuggestAdminSubCommands() {
        return suggestAdminSubCommands;
    }

    public ArrayList<SubCommand> getInstantAdminSubCommands() {
        return instantAdminSubCommands;
    }

    public ArrayList<SubCommand> getNormalSubCommands() {
        return normalSubCommands;
    }
}