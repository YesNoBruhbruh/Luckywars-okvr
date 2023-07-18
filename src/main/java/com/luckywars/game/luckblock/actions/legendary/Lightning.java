package com.luckywars.game.luckblock.actions.legendary;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.luckblock.LuckyBlockAction;
import com.luckywars.game.luckblock.Rarity;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Lightning extends LuckyBlockAction {
    private BukkitTask task;

    private int time = 3;

    @Override
    public void onOpen(Location location, Player player) {

        String prefix = MessageUtils.color("&6&l[&6&lThor]&6&l:");
        player.sendMessage(prefix + MessageUtils.color(" &b&lLET THE THUNDER RAIN!"));
        Bukkit.getScheduler().runTaskLater(LuckyWars.getInstance(), () -> {
            task = Bukkit.getScheduler().runTaskTimer(LuckyWars.getInstance(), () -> {
                if (time <= 0) {
                    task.cancel();
                }else{
                    location.getWorld().spawnEntity(player.getLocation(), EntityType.LIGHTNING);
                    player.sendMessage(prefix + MessageUtils.color(" &c&lDIE HA HA HA!"));
                }
                time--;
            }, 0, 20);
        }, 30);

    }

    @Override
    public Rarity chance() {
        return Rarity.LEGENDARY;
    }
}
