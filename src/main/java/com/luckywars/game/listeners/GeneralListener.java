package com.luckywars.game.listeners;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.object.GamePlayer;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GeneralListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if (player == null){
            if (LuckyWars.getInstance().isDebugMessages()){
                System.out.println("player is null!");
            }
            return;
        }

        if (player.getUniqueId() == null){
            if (LuckyWars.getInstance().isDebugMessages()){
                System.out.println("player's UUID is null!");
            }
            return;
        }

        if (LuckyWars.getInstance().isSendJoinCommand()){
            if (event.getPlayer().hasPermission("luckywars.command.admin")){
                event.getPlayer().sendMessage(MessageUtils.color("&cDO /lw OR /luckywars !!!"));
            }
        }

        GamePlayer gamePlayer = LuckyWars.getInstance().getGamePlayer(player.getUniqueId());

        if (gamePlayer == null){
            if (LuckyWars.getInstance().isDebugMessages()){
                System.out.println("this guy is null for some reason?");
            }
            return;
        }

        if(gamePlayer.isInParty()){
            LuckyWars.getInstance().getPartyManager().getParty(gamePlayer.getPartyId()).rejoin(player);
        }

        if (LuckyWars.getInstance().getLobbyLocation() != null){
            Bukkit.getScheduler().runTaskAsynchronously(LuckyWars.getInstance(), () -> player.teleport(LuckyWars.getInstance().getLobbyLocation()));
        }
        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        GamePlayer gamePlayer = LuckyWars.getInstance().getGamePlayer(player.getUniqueId());
        if(gamePlayer.isInParty()){
            LuckyWars.getInstance().getPartyManager().getParty(gamePlayer.getPartyId()).removePlayer(player, true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if (Bukkit.getWorld(LuckyWars.getInstance().getConfig().getString("lobby-location.world")) != null){
            World world = Bukkit.getWorld(LuckyWars.getInstance().getConfig().getString("lobby-location.world"));
            if (world.getPlayers().contains(event.getPlayer())){
                if (!LuckyWars.getInstance().getInBuildSession().contains(event.getPlayer().getUniqueId())){
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        if (Bukkit.getWorld(LuckyWars.getInstance().getConfig().getString("lobby-location.world")) != null){
            World world = Bukkit.getWorld(LuckyWars.getInstance().getConfig().getString("lobby-location.world"));
            if (world.getPlayers().contains(event.getPlayer())){
                if (!LuckyWars.getInstance().getInBuildSession().contains(event.getPlayer().getUniqueId())){
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event){
        if (LuckyWars.getInstance().getLobbyLocation() != null){
            World world = LuckyWars.getInstance().getLobbyLocation().getWorld();
            if (event.getEntity().getLocation().getWorld().equals(world)){
                event.setFoodLevel(20);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (LuckyWars.getInstance().getLobbyLocation() != null){

            if (event.getEntity() instanceof Player){
                Player player = (Player) event.getEntity();

                World world = LuckyWars.getInstance().getLobbyLocation().getWorld();

                if (player.getWorld().equals(world)){
                    event.setCancelled(true);
                    if (LuckyWars.getInstance().isDebugMessages()){
                        System.out.println("its getting cancelled by GeneralListener");
                    }
                }
            }
        }
    }
}