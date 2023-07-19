package com.luckywars.game.utils;

import com.luckywars.game.LuckyWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Supplier;

public class Hologram {
    private final Supplier<String> title;
    private final ArmorStand entity;
    private final BukkitTask task;

    public Hologram(Location location, Supplier<String> title, int interval){
        this.title = title;
        this.entity = location.getWorld().spawn(location.subtract(0, 1, 0), ArmorStand.class);
        this.entity.setVisible(false);
        this.entity.setGravity(false);
        this.entity.setCustomName(MessageUtils.color(title.get()));
        this.entity.setCustomNameVisible(true);

        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(LuckyWars.getInstance(), this::update, 0, interval);
    }

    public void update(){
        this.entity.setCustomName(MessageUtils.color(title.get()));
    }

    public void destroy(){
        this.task.cancel();
        this.entity.remove();
    }

}
