package com.luckywars.game.party;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.game.Game;
import com.luckywars.game.utils.MessageUtils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Party {

    private final UUID partyId;

    private UUID leader;

    private final Map<UUID, PartyRank> players;

    private final List<UUID> onlinePlayers;
    private final List<UUID> offlinePlayers;

    private final Map<UUID, BukkitTask> removeTasks;
    private final Map<UUID, BukkitTask> inviteTasks;

    public Party(UUID partyId, Player owner){
        this.partyId = partyId;
        this.players = new HashMap<>();
        this.onlinePlayers = new ArrayList<>();
        this.offlinePlayers = new ArrayList<>();
        this.removeTasks = new HashMap<>();
        this.inviteTasks = new HashMap<>();

        this.leader = owner.getUniqueId();
        this.addPlayer(owner, PartyRank.LEADER);
        MessageUtils.sendMessageWithLines(owner, "&aYou created a party");
    }

    public void addPlayer(Player player){
        if(inviteTasks.containsKey(player.getUniqueId())){
            inviteTasks.get(player.getUniqueId()).cancel();
            inviteTasks.remove(player.getUniqueId());
            LuckyWars.getInstance().getGamePlayer(player.getUniqueId()).setPendingInvite(null);
        }
        this.addPlayer(player, PartyRank.MEMBER);
        this.broadcast("&a" + player.getDisplayName() + " has joined the party!");
    }

    public void addPlayer(Player player, PartyRank rank){
        this.players.put(player.getUniqueId(), rank);
        this.onlinePlayers.add(player.getUniqueId());
        LuckyWars.getInstance().getGamePlayer(player.getUniqueId()).setPartyId(partyId);
    }

    public void removePlayer(Player player, boolean quit){
        if(quit){
            if (player != null){
                this.broadcast(player.getDisplayName() + " has disconnected. They will be removed in 1 minute");
                this.removeTasks.put(player.getUniqueId(), Bukkit.getScheduler().runTaskLater(LuckyWars.getInstance(), () -> kick(player, false), 20*60));
            }
            return;
        }
        this.broadcast("&c" + player.getDisplayName() + " has left the party!");
        this.players.remove(player.getUniqueId());
        this.onlinePlayers.remove(player.getUniqueId());
        this.offlinePlayers.remove(player.getUniqueId());
        LuckyWars.getInstance().getGamePlayer(player.getUniqueId()).setPartyId(null);
    }

    public void joinGame(Game game){
        if(offlinePlayers.size() != 0){
            MessageUtils.sendMessageWithLines(Bukkit.getPlayer(leader), "Not every part member is online");
            return;
        }
        onlinePlayers.forEach(uuidd -> {
            game.addPlayer(Bukkit.getPlayer(uuidd));
        });
    }

    public void invite(Player player, Player who){
        MessageUtils.sendMessageWithLines(player, who.getDisplayName() + " has sent you a party invite. use: /party accept, to join it!");
        LuckyWars.getInstance().getGamePlayer(player.getUniqueId()).setPendingInvite(partyId);
        this.inviteTasks.put(player.getUniqueId(), Bukkit.getScheduler().runTaskLater(LuckyWars.getInstance(), () -> {
            this.inviteTasks.remove(player.getUniqueId());
            LuckyWars.getInstance().getGamePlayer(player.getUniqueId()).setPendingInvite(null);
            this.broadcast("The party invite for " + player.getDisplayName() + " has expired");
        }, 1200));
        this.broadcast("&e" + who.getDisplayName() + " has invited " + player.getDisplayName() + " to the party. They have 60 seconds to accept.");
    }

    public void rejoin(Player player){
        if(this.removeTasks.containsKey(player.getUniqueId())){
            this.removeTasks.get(player.getUniqueId());
            this.removeTasks.remove(player.getUniqueId());
        }
    }

    public void transfer(Player newOwner){
        this.players.put(leader, PartyRank.MODERATOR);
        this.players.put(newOwner.getUniqueId(), PartyRank.LEADER);
        this.leader = newOwner.getUniqueId();
        this.broadcast("The party has been transferred to " + newOwner.getDisplayName());
    }

    public void promote(Player player){
        PartyRank rank = players.get(player.getUniqueId());
        if(rank.equals(PartyRank.MEMBER)){
            players.put(player.getUniqueId(), PartyRank.MODERATOR);
            this.broadcast(player.getDisplayName() + " has been promoted to party moderator");
        }
    }

    public void demote(Player player){
        PartyRank rank = players.get(player.getUniqueId());
        if(rank.equals(PartyRank.MODERATOR)){
            players.put(player.getUniqueId(), PartyRank.MEMBER);
            this.broadcast(player.getDisplayName() + " has been demoted to party member");
        }
    }

    public void kick(Player player, boolean offline){
        MessageUtils.sendMessageWithLines(player, "You were kicked out of the party");
        this.players.remove(player.getUniqueId());
        this.onlinePlayers.remove(player.getUniqueId());
        this.offlinePlayers.remove(player.getUniqueId());
        if(!offline){
            this.broadcast(player.getDisplayName() + " was kicked out of the party");
        }
        LuckyWars.getInstance().getGamePlayer(player.getUniqueId()).setPartyId(null);
    }

    public void kickOffline(){
        offlinePlayers.forEach(uuid -> {
            this.kick(Bukkit.getPlayer(uuid), true);
        });
        this.broadcast("All offline players are kicked from the party");
    }

    public void disband(){
        this.broadcast("The party has been disbanded");
        for(UUID uuid : players.keySet()){
            LuckyWars.getInstance().getGamePlayer(uuid).setPartyId(null);
        }
        this.players.clear();
        this.onlinePlayers.clear();
        this.offlinePlayers.clear();
    }

    public void broadcast(String message){
        onlinePlayers.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            MessageUtils.sendMessageWithLines(player, message);
        });
    }

    public boolean hasRank(Player player, PartyRank rank){
        return players.get(player.getUniqueId()).equals(rank);
    }

    public UUID getPartyId() {
        return partyId;
    }

    public UUID getLeader() {
        return leader;
    }

    public Map<UUID, PartyRank> getPlayers() {
        return players;
    }

    public List<UUID> getOnlinePlayers() {
        return onlinePlayers;
    }

    public List<UUID> getOfflinePlayers() {
        return offlinePlayers;
    }
}