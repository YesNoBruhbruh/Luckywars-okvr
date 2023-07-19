package com.luckywars.game.game;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.game.state.GameState;
import com.luckywars.game.game.state.GameStateImpl;
import com.luckywars.game.game.state.impl.*;
import com.luckywars.game.object.GamePlayer;
import com.luckywars.game.object.MapData;
import com.luckywars.game.object.Pos;
import com.luckywars.game.party.Party;
import com.luckywars.game.utils.LocationUtils;
import com.luckywars.game.utils.MessageUtils;
import com.luckywars.game.utils.WorldUtils;

import fr.mrmicky.fastboard.FastBoard;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Game {

    private final UUID uuid;
    private final GameMode mode;

    private final String map;

    private final Map<UUID, Integer> players;
    private final Map<Integer, Set<UUID>> teams;
    private final Set<UUID> spectators;

    private final Map<UUID, FastBoard> scoreboards;

    private final MapData data;

    private World world;

    private GameState state = GameState.NONE;
    private final Map<GameState, GameStateImpl> states;
    private final List<BukkitTask> tasks;
    private int winnerTeam = 0;
    private final HashMap<UUID, Integer> totalKillsPerPlayer;

    /**
     * Create a Game and loads it.
     *
     * @param uuid - game uuid
     * @param mode - game GameMode
     * @param map - game world
     **/
    public Game(UUID uuid, GameMode mode, String map){
        this.uuid = uuid;
        this.mode = mode;
        this.map = map;
        this.players = new HashMap<>();
        this.teams = new HashMap<>();
        this.spectators = new HashSet<>();
        this.scoreboards = new HashMap<>();
        this.states = new HashMap<>();
        this.tasks = new ArrayList<>();
        this.totalKillsPerPlayer = new HashMap<>();

        this.states.put(GameState.NONE, new NoneGameState());
        this.states.put(GameState.LOADING, new LoadingGameState());
        this.states.put(GameState.WAITING, new WaitingGameState());
        this.states.put(GameState.STARTING, new StartingGameState());
        this.states.put(GameState.ACTIVE, new ActiveGameState());
        this.states.put(GameState.ENDED, new EndedGameState());

        this.tasks.add(Bukkit.getScheduler().runTaskTimer(LuckyWars.getInstance(), () -> {
            for (FastBoard board : this.scoreboards.values()){
                for (UUID playerUUID : this.scoreboards.keySet()){
                    updateBoard(board, Bukkit.getPlayer(playerUUID));
                }
            }
        },0, 10));

        this.data = LuckyWars.getInstance().getMapDB().loadMapData(map);

        WorldUtils.loadGameWorld(this);

    }
    /**
     * adds player to Game
     *
     * @param player - player who will join the game
     * */
    public void addPlayer(Player player){
        if(state == GameState.LOADING){
            player.sendMessage(MessageUtils.format("That game is still loading"));
            Bukkit.getScheduler().runTaskLater(LuckyWars.getInstance(), () -> this.addPlayer(player), 20);
            return;
        }
        if(players.size() == mode.getMaxPlayers()){
            this.addSpectator(player);
            return;
        }

        player.getInventory().clear();
        player.setGameMode(org.bukkit.GameMode.ADVENTURE);

        GamePlayer gamePlayer = LuckyWars.getInstance().getGamePlayer(player.getUniqueId());
        gamePlayer.setGameId(uuid);

        MessageUtils.broadcast("&a" + player.getDisplayName() + " has joined the match", players);

        if (LuckyWars.getInstance().isDebugMessages()){
            System.out.println("max players for " + mode + " is " + mode.getMaxPlayers());
            System.out.println("players per team for " + mode + " is " + mode.getPlayersPerTeam());
        }

        for(int i = 1; i <= (mode.getMaxPlayers() / mode.getPlayersPerTeam()); i++){
            if(teams.containsKey(i)){
                if(teams.get(i).size() != mode.getPlayersPerTeam()){
                    teams.get(i).add(player.getUniqueId());
                    players.put(player.getUniqueId(), i);

                    if (LuckyWars.getInstance().isDebugMessages()){
                        System.out.println();
                        System.out.println("already found an existing team");
                        System.out.println("player " + player.getName());
                        System.out.println("team set is " + i);
                        System.out.println("team size is " + teams.get(i).size());
                    }
                    break;
                }
            } else {
                teams.put(i, new HashSet<>());
                teams.get(i).add(player.getUniqueId());
                players.put(player.getUniqueId(), i);
                if (LuckyWars.getInstance().isDebugMessages()){
                    System.out.println();
                    System.out.println("created a team");
                    System.out.println("player " + player.getName());
                    System.out.println("team set is " + i);
                    System.out.println("team size is " + teams.get(i).size());
                }
                break;
            }
        }

        // teleports the player to their assigned location.
        this.assignSpawnLocation(player);

        FastBoard fastBoard = new FastBoard(player);
        fastBoard.updateTitle(MessageUtils.color("&e&lLuckyWars"));

        scoreboards.put(player.getUniqueId(), fastBoard);

        if(players.size() == mode.getMinPlayers()){
            this.setGameState(GameState.STARTING);
        }
    }

    /**
     * adds party to a Game
     *
     * @param party - party to join the game
     * */
    public void addParty(Party party){
        if((players.size() + party.getOnlinePlayers().size()) >= mode.getMaxPlayers()){
            MessageUtils.sendMessageWithLines(Bukkit.getPlayer(party.getLeader()), "Party is to big for this game or game is full.");
            return;
        }

        //TODO one way to fix this is to just use party.joinGame() method,
        //TODO or actually figure out a way to keep them in a same team.

        party.joinGame(this);
    }

    /**
     * removes a player from the game
     *
     * @param player - player who will be removed from game
     * */
    public void removePlayer(Player player){
        int team = this.players.get(player.getUniqueId());
        this.players.remove(player.getUniqueId());
        this.teams.get(team).remove(player.getUniqueId());
        this.spectators.remove(player.getUniqueId());

        GamePlayer gamePlayer = LuckyWars.getInstance().getGamePlayer(player.getUniqueId());
        gamePlayer.setGameId(null);

        MessageUtils.broadcast("&c" + player.getDisplayName() + " has left the match", players);

        if(state == GameState.ACTIVE){
            ActiveGameState gameState = (ActiveGameState) states.get(state);
            gameState.getAliveTeams().get(team).remove(player.getUniqueId());
            if(gameState.getAliveTeams().size() == 1){
                this.winnerTeam = gameState.getAliveTeams().keySet().stream().findFirst().orElse(0);
                this.setGameState(GameState.ENDED);
                return;
            }
        }
        if(state == GameState.STARTING && players.size() < mode.getMinPlayers()){
            this.setGameState(GameState.WAITING);
        }

        FastBoard fastBoard = this.scoreboards.get(player.getUniqueId());
        fastBoard.delete();
        this.scoreboards.remove(player.getUniqueId());

        player.getInventory().clear();
        player.setGameMode(org.bukkit.GameMode.ADVENTURE);
        player.teleport(LuckyWars.getInstance().getLobbyLocation());
    }

    /**
     * adds spectator to the game
     *
     * @param player - player turns into spectator
     * */
    public void addSpectator(Player player){
        this.spectators.add(player.getUniqueId());
        player.getInventory().clear();
        player.setGameMode(org.bukkit.GameMode.SPECTATOR);
        player.teleport(LocationUtils.posToLocation(data.getSpectatorSpawn(), world));
    }

    /**
     * assigns team spawn location to player
     * player teleports to that location
     *
     * @param player - player who will teleport to the assignedSpawnLocation
     * */
    public void assignSpawnLocation(Player player){
        if (players.get(player.getUniqueId()) == null){
            if (LuckyWars.getInstance().isDebugMessages()){
                System.out.println("player.getUniqueID was null Game 214");
            }
            return;
        }
        int team = players.get(player.getUniqueId());
        List<Pos> spawnPoints = this.data.getSpawnPoints();

        Pos pos;

        if (team - 1 >= 0 && team - 1 < spawnPoints.size()) {
            pos = spawnPoints.get(team - 1);
        } else {
            pos = spawnPoints.get(0);
        }

        player.teleport(LocationUtils.posToLocation(pos, world));
    }

    /**
     * updates the scoreboard
     *
     * @param board - the scoreboard of the player
     * @param player - the player assigned to the scoreboard
     * */
    private void updateBoard(FastBoard board, Player player){
        board.updateLines(MessageUtils.color(getScoreboardLines(player)));
    }

    /**
     * loops through scoreboards map and deletes them
     * clears the map
     * loops through tasks map and cancels them
     * clears the map
     * */
    public void delete(){
        for (FastBoard board : this.scoreboards.values()){
            board.delete();
        }
        this.scoreboards.clear();
        for (BukkitTask task : this.tasks){
            task.cancel();
        }
        tasks.clear();
    }

    /**
     * Clears all the maps in the game class
     * */
    public void reset(){
        this.players.clear();
        this.teams.clear();
        this.spectators.clear();

        delete();

        this.totalKillsPerPlayer.clear();
    }

    /**
     * gets the scoreboard lines for the specific player
     *
     * @param player - gets the lines for that specific player
     * */
    public List<String> getScoreboardLines(Player player){
        List<String> lines = new ArrayList<>();
        lines.add("");
        switch (state){
            case WAITING:
                lines.add("Players: &6" + players.size() + "/" + mode.getMaxPlayers());
                lines.add("");
                lines.add("Waiting...");
                lines.add("");
                break;
            case STARTING:
                lines.add("Players: &6" + players.size() + "/" + mode.getMaxPlayers());
                lines.add("");
                lines.add("Starting in &6" + ((StartingGameState) states.get(state)).getSecondsLeft());
                lines.add("");
                break;
            case ACTIVE:
                lines.add("Players: &6" + ((ActiveGameState) states.get(state)).getPlayersAlive() + "/" + mode.getMaxPlayers());
                lines.add("");
                String totalKills = (totalKillsPerPlayer.containsKey(player.getUniqueId())) ? totalKillsPerPlayer.get(player.getUniqueId()) + "": "0";
                lines.add("Kills (You): &6" + totalKills);
                lines.add("");
                // I think this part is for teams
                if(teams.get(players.get(player.getUniqueId())).size() != 1){
                    lines.add("Team:");
                    Set<UUID> teammates = teams.get(players.get(player.getUniqueId()));
                    teammates.remove(player.getUniqueId());
                    for (UUID teammate : teammates) {
                        Player bTeammate = Bukkit.getPlayer(teammate);
                        // TODO check if dead or alive (change red or green)
                        lines.add("&6" + bTeammate.getName());
                    }
                    lines.add("");
                }
                break;
            case ENDED:
                // TODO winner names
                lines.add("Game ended");
                lines.add("Team " + winnerTeam + " won");
                lines.add("");
                break;
        }
        return lines;
    }

    /**
     * sets the game state
     *
     * @param state - the state that will replace the current one
     * */
    public void setGameState(GameState state){
        if(this.state == state) return;
        this.states.get(this.state).post(this);
        this.state = state;
        this.states.get(this.state).pre(this);
    }

    public UUID getUuid() {
        return uuid;
    }

    public GameMode getMode() {
        return mode;
    }

    public String getMap() {
        return map;
    }

    public Map<UUID, Integer> getPlayers() {
        return players;
    }

    public Map<Integer, Set<UUID>> getTeams() {
        return teams;
    }

    public MapData getData() {
        return data;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public GameState getState() {
        return state;
    }

    public int getWinnerTeam() {
        return winnerTeam;
    }

    public void setWinnerTeam(int winnerTeam) {
        this.winnerTeam = winnerTeam;
    }

    public HashMap<UUID, Integer> getTotalKillsPerPlayer() {
        return totalKillsPerPlayer;
    }
}