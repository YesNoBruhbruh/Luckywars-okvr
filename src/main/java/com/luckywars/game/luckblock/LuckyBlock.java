package com.luckywars.game.luckblock;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.utils.SkullUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import xyz.xenondevs.particle.ParticleBuilder;
import xyz.xenondevs.particle.ParticleEffect;

public class LuckyBlock {

    private final ArmorStand entity;
    private final int id;
    private Location location;

    public LuckyBlock(Location location, int id){
        this.id = id;

        location.setY(location.getY() - 1.4);
        location.setX(location.getBlockX() + 0.5);
        location.setZ(location.getBlockZ() + 0.5);
        location.setYaw((float) 45);
        this.location = location;

        // Spawn armor stand
        this.entity = location.getWorld().spawn(location, ArmorStand.class);
        this.entity.setGravity(false);
        this.entity.setVisible(false);
        this.entity.setCustomName("luckyblock-" + id);
        this.entity.setCustomNameVisible(false);

        // Set luckyblock helmet
        this.entity.setHelmet(SkullUtils.getLuckBlock());

        this.entity.teleport(location);

        LuckyWars.getInstance().getLuckyBlockManager().getLuckyBlocks().put(id, this);
    }

    public void open(Player player){
        LuckyWars.getInstance().getGamePlayer(player.getUniqueId()).addLuckyBlock();
        LuckyWars.getInstance().getLuckyBlockManager().getAction().onOpen(entity.getLocation(), player);
    }

    private int rotated = 0;
    private BukkitTask task;
    public void playAnimation(Player player){
        LuckyWars.getInstance().getLuckyBlockManager().getLuckyBlocks().remove(this.id);

        // Animation logic
        task = Bukkit.getScheduler().runTaskTimer(LuckyWars.getInstance(), () -> {
            Location newLoc = location;
            for(int i = 0; i < 21; i++){
                newLoc = newLoc.add(0, 0.01, 0);
                newLoc.setYaw((float) (newLoc.getYaw() + 0.5));
                this.entity.teleport(newLoc);
            }
            rotated++;
            if(rotated == 12){
                stopTask();

                location = location.add(0, 1.4, 0);

                destroy();
                new ParticleBuilder(ParticleEffect.FLAME, location)
                        .setSpeed((float) 0.1)
                        .setAmount(10)
                        .display();

                Bukkit.getScheduler().runTask(LuckyWars.getInstance(), () -> open(player));
            }
        }, 0, 1);
    }

    public void stopTask(){
        task.cancel();
    }

    public void destroy(){
        this.entity.remove();
    }
}