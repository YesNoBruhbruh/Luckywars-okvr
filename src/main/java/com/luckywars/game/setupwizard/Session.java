package com.luckywars.game.setupwizard;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.game.GameMode;
import com.luckywars.game.object.MapData;
import com.luckywars.game.object.Pos;
import com.luckywars.game.region.Cuboid;
import com.luckywars.game.region.Selection;
import com.luckywars.game.region.SelectionManager;
import com.luckywars.game.setupwizard.gui.ConfirmExitGui;
import com.luckywars.game.setupwizard.gui.WizardGui;
import com.luckywars.game.utils.Hologram;
import com.luckywars.game.utils.ItemBuilder;
import com.luckywars.game.utils.MessageUtils;
import com.luckywars.game.utils.WorldUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Session {

    private final Player player;

    private final MapData data;

    private final GameMode gameMode;

    private final List<Hologram> holograms;

    public Session(Player player, String mapName, GameMode gameMode){
        this.player = player;
        this.gameMode = gameMode;
        this.holograms = new ArrayList<>();

        player.sendMessage(MessageUtils.format("Creating session for map " + mapName + ". pls wait..."));

        this.data = new MapData(mapName,
                /* TODO map authors */ Arrays.asList("sqcred", "YesNoBruhbruh"),
                System.currentTimeMillis(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new Pos(0, 0, 0, 0, 0));

        player.getInventory().clear();

        if(WorldUtils.worldExists(mapName)){
            player.sendMessage(MessageUtils.format("&cMap " + mapName + " not found!"));
            LuckyWars.getInstance().getWizardManager().endSession(player, false);
            return;
        }
        WorldUtils.loadWorld(mapName);

        Bukkit.getScheduler().runTaskLater(LuckyWars.getInstance(), () -> {
            player.setGameMode(org.bukkit.GameMode.CREATIVE);
            player.teleport(new Location(Bukkit.getWorld(mapName), 0, 200, 0));

            ItemStack guiItem = new ItemBuilder(Material.WATCH).setDisplayName("&6Open Gui").build();
            ItemStack centerXZItem = new ItemBuilder(Material.ARROW).setDisplayName("&6Center X-Z").build();
            ItemStack centerYawPitchItem = new ItemBuilder(Material.ARROW).setDisplayName("&6Center Yaw-Pitch").build();
            ItemStack exitItem = new ItemBuilder(Material.BARRIER).setDisplayName("&cExit Wizard").build();

            player.getInventory().setItem(0, guiItem);
            player.getInventory().setItem(3, centerXZItem);
            player.getInventory().setItem(5, centerYawPitchItem);
            player.getInventory().setItem(8, exitItem);

            player.sendMessage(MessageUtils.format("Created session, you can now edit the positions."));
        }, 20);

    }

    public void onInteract(ItemStack item){
        switch (item.getType()){
            case WATCH:
                new WizardGui(this);
                break;
            case BARRIER:
                new ConfirmExitGui(player);
                break;
            case ARROW:
                if(item.hasItemMeta()){
                    switch (ChatColor.stripColor(item.getItemMeta().getDisplayName())){
                        case "Center X-Z":
                            Location newLoc = player.getLocation();
                            newLoc.setX(newLoc.getBlockX() + 0.5);
                            newLoc.setZ(newLoc.getBlockZ() + 0.5);
                            player.teleport(newLoc);
                            break;
                        case "Center Yaw-Pitch":
                            // TODO because idk how yet
                            break;
                    }
                }
                break;
        }
    }

    public void delete(boolean save){
        player.getInventory().clear();
        player.teleport(LuckyWars.getInstance().getLobbyLocation());

        for(Hologram hologram : holograms){
            hologram.destroy();
        }

        WorldUtils.unloadWorld(data.getName());

        if(save){
            data.setLastEdit(System.currentTimeMillis());
            LuckyWars.getInstance().getMapDB().saveMapData(data.getName(), data);
        }

        player.sendMessage(MessageUtils.format("Successfully ended session, " + (save ? "&aSaved Map" : "&cDidnt save map")));
    }

    public void addLuckyBlock(){
        Location location = player.getLocation();
        this.data.getLuckyBlocks().add(new Pos(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
        this.holograms.add(new Hologram(location, () -> "&6LuckyBlock Pos", 20));
        player.sendMessage(MessageUtils.format("Successfully added &6Luckyblock"));
    }

    public void addCage() {
        SelectionManager selectionManager = LuckyWars.getInstance().getSelectionManager();
        Selection selection = selectionManager.getSelection(player.getUniqueId());

        if (selection == null) {
            player.sendMessage(MessageUtils.format("&cYou have not even made any selections at all! make one by using the loc wand!"));
            return;
        }

        Pos pos1 = selection.getFirstPos();
        Pos pos2 = selection.getSecondPos();

        if (pos1 == null) {
            player.sendMessage(MessageUtils.format("&cYour first pos selection is null!"));
            return;
        }
        if (pos2 == null){
            player.sendMessage(MessageUtils.format("&cYour second pos selection is null!"));
            return;
        }

        if (data.getCages().size() == GameMode.SOLO.getMaxPlayers()) {
            player.sendMessage(MessageUtils.format("&cYou cant add more than " + GameMode.SOLO.getMaxPlayers() + " cages!"));
            return;
        }

        Cuboid cage = new Cuboid(pos1, pos2);
        this.data.getCages().add(cage);
        player.sendMessage(MessageUtils.format("&aSuccessfully added a cage!"));
        if (LuckyWars.getInstance().isDebugMessages()) {
            System.out.println(cage.getPos1());
            System.out.println(cage.getPos2());
        }
    }

    public void addSpawnPoint(){
        if(data.getSpawnPoints().size() == gameMode.getMinPlayers()){
            player.sendMessage(MessageUtils.format("&cYou cant add more then " + gameMode.getMaxPlayers() + " spawnpoints!"));
            return;
        }
        Location location = player.getLocation();
        this.data.getSpawnPoints().add(new Pos(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
        this.holograms.add(new Hologram(location, () -> "&6Spawnpoint Pos", 20));
        player.sendMessage(MessageUtils.format("&aSuccessfully added &6Spawnpoint"));
    }

    public void setSpectatorSpawn(){
        Location location = player.getLocation();
        this.data.setSpectatorSpawn((new Pos(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch())));
        this.holograms.add(new Hologram(location, () -> "&6Spectator Pos", 20));
        player.sendMessage(MessageUtils.format("Successfully set &6Spectator Spawn"));
    }

    public Player getPlayer() {
        return player;
    }

    public MapData getData() {
        return data;
    }
}