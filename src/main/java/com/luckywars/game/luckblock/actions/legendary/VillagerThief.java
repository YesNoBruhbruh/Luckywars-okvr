package com.luckywars.game.luckblock.actions.legendary;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.luckblock.LuckyBlockAction;
import com.luckywars.game.luckblock.Rarity;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class VillagerThief extends LuckyBlockAction {

    @Override
    public void onOpen(Location location, Player player) {

        String prefix = MessageUtils.color("&e[&eThe Thief]&e:");

        Entity villager = location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        villager.setCustomName(MessageUtils.color("&eThe Thief"));
        villager.setCustomNameVisible(true);
        Bukkit.getScheduler().runTaskLater(LuckyWars.getInstance(), () -> {
            player.getInventory().clear();
            player.playSound(location, Sound.VILLAGER_NO, 1f, 1f);
            player.sendMessage(prefix + MessageUtils.color(" &eI HAVE STOLEN ALL YOUR STUFF MUHAHAHAHAH >:)"));
            villager.remove();
        }, 20);

    }

    @Override
    public Rarity chance() {
        return Rarity.LEGENDARY;
    }
}
