package com.luckywars.game.object;

import com.luckywars.game.LuckyWars;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import java.util.UUID;

public class GamePlayer {

    private final UUID uuid;
    private UUID gameId;

    private UUID partyId;
    private UUID pendingInvite;

    private int kills = 0;
    private int deaths = 0;
    private int wins = 0;
    private int losses = 0;
    private int luckyBlocksOpened = 0;
    public GamePlayer(UUID uuid){
        this.uuid = uuid;
    }

    public boolean isInGame(){
        return gameId != null;
    }

    public boolean isInParty(){
        return partyId != null;
    }

    public OfflinePlayer getOfflinePlayer(){
        return Bukkit.getOfflinePlayer(uuid);
    }

    public void addKill(){
        this.kills++;
    }

    public void addDeath(){
        this.deaths++;
    }

    public void addWin(){
        this.wins++;
    }

    public void addLoss(){
        this.losses++;
    }

    public void addLuckyBlock(){
        this.luckyBlocksOpened++;
    }

    public UUID getUUID(){
        return uuid;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public UUID getPartyId() {
        return partyId;
    }

    public void setPartyId(UUID partyId) {
        if (LuckyWars.getInstance().isDebugMessages()){
            System.out.println("someone's partyID got changed");
            if (partyId == null){
                System.out.println("someone's partyID is null");
            }
        }
        this.partyId = partyId;
    }

    public UUID getPendingInvite() {
        return pendingInvite;
    }

    public void setPendingInvite(UUID pendingInvite) {
        this.pendingInvite = pendingInvite;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getKills() {
        return kills;
    }

    public int getWins(){
        return wins;
    }

    public int getDeaths(){
        return deaths;
    }

    public int getLosses(){
        return losses;
    }

    public int getLuckyBlocksOpened() {
        return luckyBlocksOpened;
    }

    public void setLuckyBlocksOpened(int luckyBlocksOpened) {
        this.luckyBlocksOpened = luckyBlocksOpened;
    }
}