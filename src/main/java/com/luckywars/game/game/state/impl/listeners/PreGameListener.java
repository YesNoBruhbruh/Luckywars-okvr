package com.luckywars.game.game.state.impl.listeners;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.game.Game;
import com.luckywars.game.game.state.GameState;
import com.luckywars.game.game.state.GameStateListener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PreGameListener extends GameStateListener {

    private final Game game;

    public PreGameListener(Game game){
        this.game = game;
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        if(game.getPlayers().containsKey(event.getPlayer().getUniqueId())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        if(game.getPlayers().containsKey(event.getPlayer().getUniqueId())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) {
        if(game.getPlayers().containsKey(event.getWhoClicked().getUniqueId())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onItemDrop(PlayerDropItemEvent event) {
        if(game.getPlayers().containsKey(event.getPlayer().getUniqueId())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onArrowShoot(ProjectileLaunchEvent event) {
        if(game.getPlayers().containsKey(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onItemPickup(PlayerPickupItemEvent event){
        if(game.getPlayers().containsKey(event.getPlayer().getUniqueId())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onDamage(EntityDamageEvent event){
        if(game.getPlayers().containsKey(event.getEntity().getUniqueId())){
            event.setCancelled(true);
            if (LuckyWars.getInstance().isDebugMessages()){
                System.out.println("its getting cancelled by PreGamelistener");
            }
        }
    }

    @EventHandler
    private void onFoodChange(FoodLevelChangeEvent event){
        if(game.getPlayers().containsKey(event.getEntity().getUniqueId())){
            event.setFoodLevel(20);
        }
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if (game.getPlayers().containsKey(player.getUniqueId())){
            game.removePlayer(player);
        }
    }
}