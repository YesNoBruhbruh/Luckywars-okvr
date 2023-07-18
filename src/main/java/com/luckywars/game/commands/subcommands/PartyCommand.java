package com.luckywars.game.commands.subcommands;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.commands.SubCommand;
import com.luckywars.game.object.GamePlayer;
import com.luckywars.game.party.Party;
import com.luckywars.game.party.PartyRank;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PartyCommand extends SubCommand {

    @Override
    public String getName() {
        return "party";
    }

    @Override
    public String getDescription() {
        return "&aParty!!";
    }

    @Override
    public String getSyntax() {
        //                 args[0]                               args[1]                                 args[2]
        return "/luckywars party <accept|create|leave|promote|demote|kick|transfer|kickoffline|disband> [player]";
    }

    @Override
    public String getPermission() {
        return "luckywars.command.party";
    }

    @Override
    public void perform(Player player, String[] args) {
        GamePlayer gamePlayer = LuckyWars.getInstance().getGamePlayer(player.getUniqueId());

        Party party = LuckyWars.getInstance().getPartyManager().getParty(gamePlayer.getPartyId());
        // let's also make sure they aren't in a game!
        if (gamePlayer.isInGame()){
            player.sendMessage(MessageUtils.color("&cYou aren't allowed to party things while in a game!"));
            return;
        }

        if (args.length == 2){
            // commands for when they only put args[0]
            switch (args[1]){
                case "create":
                    if (!gamePlayer.isInParty()){
                        LuckyWars.getInstance().getPartyManager().createParty(player);
                    }else{
                        player.sendMessage(MessageUtils.color("&cYou are already in a party!"));
                    }
                    break;
                case "accept":
                    if (!gamePlayer.isInParty()){
                        if (gamePlayer.getPendingInvite() != null){
                            LuckyWars.getInstance().getPartyManager().getParty(gamePlayer.getPendingInvite()).addPlayer(player);
                            player.sendMessage(MessageUtils.color("&aAdded you to the party!"));
                        }else{
                            player.sendMessage(MessageUtils.color("&cNo one has invited you?"));
                        }
                    }else{
                        player.sendMessage(MessageUtils.color("&cLeave your current party first!"));
                    }
                    break;
                case "leave":
                    if (gamePlayer.isInParty()){
                        if (isLeader(player)){
                            LuckyWars.getInstance().getPartyManager().getParty(gamePlayer.getPartyId()).removePlayer(player, false);
                            LuckyWars.getInstance().getPartyManager().getParty(gamePlayer.getPartyId()).broadcast("&eParty leader has left! Party will disband now!");
                            LuckyWars.getInstance().getPartyManager().getParty(gamePlayer.getPartyId()).disband();
                        }else{
                            LuckyWars.getInstance().getPartyManager().getParty(gamePlayer.getPartyId()).removePlayer(player, false);
                        }
                    }else{
                        player.sendMessage(MessageUtils.color("&cYou are not in a party!"));
                    }
                    break;
                case "kickoffline":
                    if (gamePlayer.isInParty()){
                        if(isLeader(player)){
                            party.kickOffline();
                        }else{
                            player.sendMessage(MessageUtils.color("&cYou are not the party leader, therefore you cannot use this command!"));
                        }
                    }else{
                        player.sendMessage(MessageUtils.color("&cYou are not in a party!"));
                    }
                    break;
                case "disband":
                    if (gamePlayer.isInParty()){
                        if (isLeader(player)){
                            party.disband();
                        }else{
                            player.sendMessage(MessageUtils.color("&cYou are not the party leader, therefore you cannot use this command!"));
                        }
                    }else{
                        player.sendMessage(MessageUtils.color("&cYou are not in a party!"));
                    }
                    break;
                default:
                    sendWrongUsage(player);
            }
        } else if (args.length == 3){

            // let's make sure the player they selected actually exists!
            Player target = Bukkit.getPlayer(args[2]);
            if (target == null){
                player.sendMessage(MessageUtils.color("&cThat player is offline!"));
                return;
            }
            GamePlayer targetGamePlayer = LuckyWars.getInstance().getGamePlayer(target.getUniqueId());
            // lastly, let's make sure the person they select isn't in a game already!
            if (targetGamePlayer.isInGame()){
                player.sendMessage(MessageUtils.color("&cThis player is already in a game!"));
                return;
            }

            if (targetGamePlayer.equals(gamePlayer)){
                player.sendMessage(MessageUtils.color("&cTHATS THE SAME FUCKING PLAYER YOU DUMBFUCK"));
                return;
            }

            switch (args[1]){
                case "invite":
                    if (gamePlayer.isInParty()){
                        if (targetGamePlayer.isInParty()){
                            player.sendMessage(MessageUtils.color("&cThat player is already in a party!"));
                        }else{
                            if (targetGamePlayer.isInGame()){
                                player.sendMessage(MessageUtils.color("&cThat player is already in a game!"));
                            }else{
                                if (targetGamePlayer.getPendingInvite() == null){
                                    party.invite(target, player);
                                }else{
                                    player.sendMessage(MessageUtils.color("&cThis player already has a pending invite!"));
                                }
                            }
                        }
                    }else{
                        player.sendMessage(MessageUtils.color("&cYou aren't even in a party smh!"));
                    }
                    break;
                case "promote":
                    if (gamePlayer.isInParty()){
                        if(isLeader(player)){
                            party.promote(target);
                        }else{
                            player.sendMessage(MessageUtils.color("&cYou are not the party leader, therefore you cannot use this command!"));
                        }
                    }else{
                        player.sendMessage(MessageUtils.color("&cYou aren't even in a party smh!"));
                    }
                    break;
                case "demote":
                    if (gamePlayer.isInParty()){
                        if (isLeader(player)){
                            party.demote(target);
                        }else{
                            player.sendMessage(MessageUtils.color("&cYou are not the party leader, therefore you cannot use this command!"));
                        }
                    }else{
                        player.sendMessage(MessageUtils.color("&cYou aren't even in a party smh!"));
                    }
                    break;
                case "kick":
                    if (gamePlayer.isInParty()){
                        if (isMod(player)){
                            party.kick(player, false);
                        }else{
                            player.sendMessage(MessageUtils.color("&cYou are not the party leader nor the moderator, therefore you cannot use this command!"));
                        }
                    }else{
                        player.sendMessage(MessageUtils.color("&cYou aren't even in a party smh!"));
                    }
                    break;
                case "transfer":
                    if (gamePlayer.isInParty()){
                        if (isLeader(player)){
                            party.transfer(target);
                        }else{
                            player.sendMessage(MessageUtils.color("&cYou are not the party leader, therefore you cannot use this command!"));
                        }
                    }else{
                        player.sendMessage(MessageUtils.color("&cYou aren't even in a party smh!"));
                    }
                    break;
                default:
                    sendWrongUsage(player);
            }
        }else{
            sendWrongUsage(player);
        }
    }

    public static void sendWrongUsage(Player player){
        //                                           args.length == 0                          args.length == 1                  args.length == 2
        player.sendMessage(MessageUtils.format("&cWrong usage: /party <accept|create|leave|promote|demote|kick|transfer|kickoffline|disband> [player]"));
    }

    public boolean isMod(Player player){
        GamePlayer gamePlayer = LuckyWars.getInstance().getGamePlayer(player.getUniqueId());
        Party party = LuckyWars.getInstance().getPartyManager().getParty(gamePlayer.getPartyId());
        return party.hasRank(player, PartyRank.MODERATOR) || party.hasRank(player, PartyRank.LEADER);
    }

    public boolean isLeader(Player player){
        GamePlayer gamePlayer = LuckyWars.getInstance().getGamePlayer(player.getUniqueId());
        Party party = LuckyWars.getInstance().getPartyManager().getParty(gamePlayer.getPartyId());
        return party.getLeader().equals(player.getUniqueId());
    }
}