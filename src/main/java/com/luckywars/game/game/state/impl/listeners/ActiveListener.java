package com.luckywars.game.game.state.impl.listeners;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.game.Game;
import com.luckywars.game.game.state.GameState;
import com.luckywars.game.game.state.GameStateListener;
import com.luckywars.game.game.state.impl.ActiveGameState;
import com.luckywars.game.object.GamePlayer;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ActiveListener extends GameStateListener {

    private final ActiveGameState state;
    private final Game game;

    private final Map<UUID, UUID> lastDamager;
    private final Map<UUID, BukkitTask> tasks;

    public ActiveListener(Game game, ActiveGameState state){
        this.state = state;
        this.game = game;
        this.lastDamager = new HashMap<>();
        this.tasks = new HashMap<>();
    }

    @EventHandler
    public void onDeath(EntityDamageEvent entityDamageEvent){
        if(entityDamageEvent.getEntity() instanceof Player){
            Player player = (Player) entityDamageEvent.getEntity();
            if (entityDamageEvent instanceof EntityDamageByEntityEvent){
                EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) entityDamageEvent;
                if(entityDamageByEntityEvent.getDamager() instanceof Player){
                    Player damager = (Player) entityDamageByEntityEvent.getDamager();
                    // friendly-fire
                    if (game.getPlayers().get(player.getUniqueId()) == null){
                        return;
                    }
                    if (game.getPlayers().get(damager.getUniqueId()) == null){
                        return;
                    }
                    int playerTeam = game.getPlayers().get(player.getUniqueId());
                    int damagerTeam = game.getPlayers().get(damager.getUniqueId());
                    if(playerTeam == damagerTeam){
                        entityDamageByEntityEvent.setCancelled(true);
                        return;
                    }
                    lastDamager.put(player.getUniqueId(), damager.getUniqueId());
                    if(tasks.containsKey(player.getUniqueId())){
                        tasks.get(player.getUniqueId()).cancel();
                        tasks.remove(player.getUniqueId());
                    }
                    tasks.put(player.getUniqueId(), Bukkit.getScheduler().runTaskLater(LuckyWars.getInstance(), () -> {
                        tasks.remove(player.getUniqueId());
                        lastDamager.remove(player.getUniqueId());
                    }, 1200));
                }

                if(entityDamageByEntityEvent.getDamage() >= player.getHealth()){
                    if(entityDamageByEntityEvent.getDamager() instanceof Player){
                        Player killer = (Player) entityDamageByEntityEvent.getDamager();
                        // Killed by a player
                        MessageUtils.broadcast("&c" + player.getDisplayName() + " &rwas killed by &c" + killer.getDisplayName(), game.getPlayers());

                        killPlayer(player, killer);
                    } else {
                        // Killed by a mob or something
                        MessageUtils.broadcast("&c" + player.getDisplayName() + " &rwas killed by &c" + entityDamageByEntityEvent.getDamager().getCustomName(), game.getPlayers());

                        killPlayer(player, null);
                    }
                    entityDamageByEntityEvent.setCancelled(true);
                }
            }
            // this means they get damaged by something other than an entity

            if (entityDamageEvent.getDamage() >= player.getHealth()){
                if (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FALL){
                    if (lastDamager.containsKey(player.getUniqueId())) {
                        // player was pushed off by another player
                        OfflinePlayer damager = Bukkit.getOfflinePlayer(lastDamager.get(player.getUniqueId()));

                        MessageUtils.broadcast("&c" + player.getDisplayName() + " &rwas pushed off by &c" + damager.getName(), game.getPlayers());

                        killPlayer(player, damager);
                    } else {
                        // player just fell
                        MessageUtils.broadcast("&c" + player.getDisplayName() + " &rwas killed due to fall damage!", game.getPlayers());
                        killPlayer(player, null);
                    }
                }
                entityDamageEvent.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        // keep this here so that it stays flexible so server owners can set the y level for the void move
        if(event.getPlayer().getLocation().getY() < 20){
            // Player fell in void
            Player player = event.getPlayer();
            if(lastDamager.containsKey(event.getPlayer().getUniqueId())){
                // Player was hit into the void
                OfflinePlayer damager = Bukkit.getOfflinePlayer(lastDamager.get(player.getUniqueId()));
                MessageUtils.broadcast("&c" + player.getDisplayName() + " &rwas hit into the void by &c" + damager.getName(), game.getPlayers());

                killPlayer(player, damager);
            } else {
                // Player fell
                MessageUtils.broadcast("&c" + player.getDisplayName() + " &rfell into the void", game.getPlayers());

                killPlayer(player, null);
            }
        }
    }

    @EventHandler
    public void onClick(EntityDamageByEntityEvent event){
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();

        if (!(damager instanceof Player)){
            return;
        }

        // Open the right luckblock
        if(entity instanceof ArmorStand){
            if(entity.getCustomName().contains("luckyblock")){
                int id = Integer.parseInt(entity.getCustomName().split("-")[1]);
                if(!LuckyWars.getInstance().getLuckyBlockManager().getLuckyBlocks().containsKey(id)){
                    return;
                }
                LuckyWars.getInstance().getLuckyBlockManager().getLuckyBlocks().get(id).playAnimation((Player) damager);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event){
        Entity entity = event.getRightClicked();

        // Prevent players from taking the luckyblock of the head of the armorstand
        if(entity instanceof ArmorStand){
            if(entity.getCustomName().contains("luckyblock")){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        GamePlayer gamePlayer = LuckyWars.getInstance().getGamePlayer(player.getUniqueId());

        if (gamePlayer.isInGame()){
            Game game = LuckyWars.getInstance().getGameManager().getGame(gamePlayer.getGameId());

            killPlayer(player, null);

            if (game.getPlayers().containsKey(gamePlayer.getUUID())){
                game.removePlayer(gamePlayer.getOfflinePlayer().getPlayer());
            }

            gamePlayer.addLoss();
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Block block = event.getBlock();

        if (LuckyWars.getInstance().isDebugMessages()){
            System.out.println("blockbreakevent was called");
        }

        switch (block.getType()) {
            case WHEAT:
                block.getDrops().clear();
                block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.BREAD));
                break;
            case PUMPKIN:
                block.getDrops().clear();
                block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.PUMPKIN_PIE));
                break;
            case POTATO:
                block.getDrops().clear();
                block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.BAKED_POTATO));
                break;
            case IRON_ORE:
                block.getDrops().clear();
                block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT));
                new ParticleBuilder(ParticleEffect.FLAME, block.getLocation())
                        .setAmount(1)
                        .setSpeed(1)
                        .display();
                break;
            case GOLD_ORE:
                block.getDrops().clear();
                block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT));
                new ParticleBuilder(ParticleEffect.FLAME, block.getLocation())
                        .setAmount(1)
                        .setSpeed(1)
                        .display();
                break;
        }
    }

    private void killPlayer(Player victim, OfflinePlayer killer){
        if (LuckyWars.getInstance().isDebugMessages()){
            System.out.println("killPlayer was called in ActiveListener");
        }
        if(killer != null && killer.isOnline()){
            if (LuckyWars.getInstance().isDebugMessages()){
                System.out.println("killer isnt null");
            }
            Player onlineKiller = Bukkit.getPlayer(killer.getUniqueId());
            onlineKiller.playSound(onlineKiller.getLocation(), Sound.ORB_PICKUP, 1, 1);

            GamePlayer gamePlayer = LuckyWars.getInstance().getGamePlayer(onlineKiller.getUniqueId());
            gamePlayer.addKill();

            if (!game.getTotalKillsPerPlayer().containsKey(onlineKiller.getUniqueId())){
                game.getTotalKillsPerPlayer().put(onlineKiller.getUniqueId(), 1);
            }else{
                game.getTotalKillsPerPlayer().put(onlineKiller.getUniqueId(), game.getTotalKillsPerPlayer().get(onlineKiller.getUniqueId()) + 1);
            }
        }
        if (game.getPlayers().get(victim.getUniqueId()) == null){
            if (LuckyWars.getInstance().isDebugMessages()){
                System.out.println("game.getPlayers().get(victim.getUniqueId()) is null");
            }
            return;
        }
        int team = game.getPlayers().get(victim.getUniqueId());

        if (!state.getAliveTeams().get(team).contains(victim.getUniqueId())){
            if (LuckyWars.getInstance().isDebugMessages()){
                System.out.println("!state.getAliveTeams().get(team).contains(victim.getUniqueId()) ");
            }
            return;
        }
        state.getAliveTeams().get(team).remove(victim.getUniqueId());
        if(state.getAliveTeams().get(team).size() == 0){
            if (LuckyWars.getInstance().isDebugMessages()){
                System.out.println(team + " was removed from aliveTeams!");
            }
            state.getAliveTeams().remove(team);
        }

        dropItems(victim);

        game.addSpectator(victim);

        GamePlayer gamePlayer = LuckyWars.getInstance().getGamePlayer(victim.getUniqueId());

        gamePlayer.addDeath();
//        gamePlayer.addLoss();

        state.addDeath();

        if(state.getAliveTeams().size() == 1){
            // There is a winner
            if (LuckyWars.getInstance().isDebugMessages()){
                System.out.println("aliveTeams size is 1?");
            }
            game.setWinnerTeam(state.getAliveTeams().keySet().stream().findFirst().orElse(0));

            for (UUID uuid : game.getPlayers().keySet()){
                if (!state.getAliveTeams().get(game.getWinnerTeam()).contains(uuid)){
                    // this means you weren't on the winning team.
                    GamePlayer loser = LuckyWars.getInstance().getGamePlayer(uuid);

                    loser.addLoss();

                    if (loser.getOfflinePlayer().isOnline()){
                        loser.getOfflinePlayer().getPlayer().sendMessage(MessageUtils.color("&cYou lost lol"));
                    }
                }else{
                    // this means you were on the winning team.
                    GamePlayer winner = LuckyWars.getInstance().getGamePlayer(uuid);

                    winner.addWin();

                    if (winner.getOfflinePlayer().isOnline()){
                        winner.getOfflinePlayer().getPlayer().sendMessage(MessageUtils.color("&aYou won the game!"));
                    }
                }
            }

            game.setGameState(GameState.ENDED);
        }
    }

    private void dropItems(Player player) {
        for(ItemStack item : player.getInventory().getContents()){
            if(item != null && item.getType() != Material.AIR){
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }
        for(ItemStack item : player.getInventory().getArmorContents()){
            if(item != null && item.getType() != Material.AIR){
                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }
    }
}