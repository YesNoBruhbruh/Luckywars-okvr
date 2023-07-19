package com.luckywars.game;

import com.grinderwolf.swm.api.SlimePlugin;

import com.luckywars.game.commands.CommandManager;
import com.luckywars.game.database.*;
import com.luckywars.game.database.dbs.GamePlayerDB;
import com.luckywars.game.database.dbs.MapDB;
import com.luckywars.game.game.GameManager;
import com.luckywars.game.listeners.GeneralListener;
import com.luckywars.game.luckyblock.LuckyBlockManager;
import com.luckywars.game.object.GamePlayer;
import com.luckywars.game.party.PartyManager;
import com.luckywars.game.region.SelectionManager;
import com.luckywars.game.setupwizard.WizardListeners;
import com.luckywars.game.setupwizard.WizardManager;
import com.luckywars.game.region.CuboidSelector;
import com.luckywars.game.utils.PreLoadedItemLists;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.*;

public class LuckyWars extends JavaPlugin {

    private static LuckyWars instance;
    private GameManager gameManager;
    private PartyManager partyManager;

    private WizardManager wizardManager;

    private LuckyBlockManager luckyBlockManager;
    private SelectionManager selectionManager;

    private final SlimePlugin slimePlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");

    private DatabaseConnection connection;

    private MapDB mapDB;
    private GamePlayerDB gamePlayerDB;

    private final List<GamePlayer> gamePlayers = new ArrayList<>();

    private final List<UUID> inBuildSession = new ArrayList<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        if(this.slimePlugin == null){
            this.getLogger().severe("SlimeWorldManager is not installed. Shutting down server...");
            Bukkit.shutdown();
        }

        connectDatabases();
        registerClasses();
        registerListeners();
        registerCommands();
        loadGames();
    }

    @Override
    public void onDisable() {
        connection.disconnect();

        instance = null;
    }

    private void connectDatabases(){
        this.connection = new DatabaseConnection(getConfig().getString("mysql.host"), getConfig().getInt("mysql.port"), getConfig().getString("mysql.database"), getConfig().getString("mysql.username"), getConfig().getString("mysql.password"), getConfig().getBoolean("mysql.useSSL"), new IConnectedCallback() {

            @Override
            public void onConnected(Connection connection) {
                new TableBuilder("player_stats")
                        .addField("UUID", TableBuilder.DataType.VARCHAR, 100)
                        .addField("KILLS", TableBuilder.DataType.INT, 100)
                        .addField("LUCKYBLOCKS", TableBuilder.DataType.INT, 100)
                        .addField("DEATHS", TableBuilder.DataType.INT, 100)
                        .addField("WINS", TableBuilder.DataType.INT, 100)
                        .addField("LOSSES", TableBuilder.DataType.INT, 100)
                        .setPrimaryKey("UUID")
                        .execute(connection);
                new TableBuilder("mapdata")
                        .addField("NAME", TableBuilder.DataType.VARCHAR, 100)
                        .addField("AUTHORS", TableBuilder.DataType.VARCHAR, 1000)
                        .addField("LASTEDIT", TableBuilder.DataType.VARCHAR, 10000)
                        .addField("LUCKYBLOCKS", TableBuilder.DataType.VARCHAR, 1000)
                        .addField("SPAWNPOINTS", TableBuilder.DataType.VARCHAR, 1000)
                        .addField("CAGES", TableBuilder.DataType.VARCHAR, 1000)
                        .addField("SPECTATORSPAWN", TableBuilder.DataType.VARCHAR, 100)
//                        .addField("DATA", TableBuilder.DataType.VARCHAR, 10000)
                        .setPrimaryKey("NAME")
                        .execute(connection);

                mapDB = new MapDB(connection);
                gamePlayerDB = new GamePlayerDB(connection);
            }

            @Override
            public void onDisconnect() {
            }
        });
    }

    private void loadGames(){
        //TODO currently hardcoded. Make sure to make a for loop and loop through map data and make games out of all the map names!
//        gameManager.createGame(GameMode.DUELS, "candylane");
    }

    private void registerClasses() {
        gameManager = new GameManager();
        partyManager = new PartyManager();
        wizardManager = new WizardManager();
        luckyBlockManager = new LuckyBlockManager();
        selectionManager = new SelectionManager();

        Bukkit.getScheduler().runTaskAsynchronously(this, PreLoadedItemLists::new);
    }

    private void registerListeners(){
        PluginManager pm = Bukkit.getPluginManager();
//        pm.registerEvents(new LuckyBlockListener(), this);
        pm.registerEvents(new WizardListeners(), this);
        pm.registerEvents(new GeneralListener(), this);
        pm.registerEvents(new CuboidSelector(), this);
    }

    private void registerCommands(){
        getCommand("luckywars").setExecutor(new CommandManager());
    }

    public GamePlayer getGamePlayer(UUID uuid) {
        for (GamePlayer gamePlayer : gamePlayers) {
            if (gamePlayer != null && gamePlayer.getOfflinePlayer() != null){
                if (gamePlayer.getOfflinePlayer().equals(Bukkit.getPlayer(uuid))) return gamePlayer;
            }
        }

        return newGamePlayer(uuid);
    }

    private GamePlayer newGamePlayer(UUID uuid) {
        try{
            GamePlayer gamePlayer = new GamePlayer(uuid);

            if (gamePlayerDB.getGamePlayerStatsFromDB(gamePlayer.getUUID().toString()) == null){
                gamePlayerDB.createGamePlayerStats(gamePlayer);
                if (isDebugMessages()){
                    System.out.println("created game stats for " + gamePlayer.getOfflinePlayer().getName());
                }
            }else{
                GamePlayer databaseGamePlayer = gamePlayerDB.getGamePlayerStatsFromDB(gamePlayer.getUUID().toString());

                int kills = databaseGamePlayer.getKills();
                int luckyBlocks = databaseGamePlayer.getLuckyBlocksOpened();
                int deaths = databaseGamePlayer.getDeaths();
                int wins = databaseGamePlayer.getWins();
                int losses = databaseGamePlayer.getLosses();

                gamePlayer.setKills(kills);
                gamePlayer.setLuckyBlocksOpened(luckyBlocks);
                gamePlayer.setDeaths(deaths);
                gamePlayer.setWins(wins);
                gamePlayer.setLosses(losses);
            }

            gamePlayers.add(gamePlayer);
            return gamePlayer;
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public static LuckyWars getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

    public WizardManager getWizardManager() {
        return wizardManager;
    }

    public LuckyBlockManager getLuckyBlockManager() {
        return luckyBlockManager;
    }

    public SelectionManager getSelectionManager() {
        return selectionManager;
    }

    public SlimePlugin getSlimePlugin() {
        return slimePlugin;
    }

    public MapDB getMapDB() {
        return mapDB;
    }

    public GamePlayerDB getGamePlayerDB(){
        return gamePlayerDB;
    }

    public List<UUID> getInBuildSession() {
        return inBuildSession;
    }

    public Location getLobbyLocation(){
        if (LuckyWars.getInstance().getConfig().getString("lobby-location.world") == null){
            return new Location(Bukkit.getWorld("world"), 0, 64, 0);
        }else{
            return new Location(
                    Bukkit.getWorld(LuckyWars.getInstance().getConfig().getString("lobby-location.world")),
                    LuckyWars.getInstance().getConfig().getDouble("lobby-location.x"),
                    LuckyWars.getInstance().getConfig().getDouble("lobby-location.y"),
                    LuckyWars.getInstance().getConfig().getDouble("lobby-location.z"),
                    (float)LuckyWars.getInstance().getConfig().getDouble("lobby-location.yaw"),
                    (float) LuckyWars.getInstance().getConfig().getDouble("lobby-location.pitch"));
        }
    }

    public boolean isDebugMessages(){
        return this.getConfig().getBoolean("debug-messages");
    }

    public boolean isSendJoinCommand(){
        return this.getConfig().getBoolean("join-command");
    }
}