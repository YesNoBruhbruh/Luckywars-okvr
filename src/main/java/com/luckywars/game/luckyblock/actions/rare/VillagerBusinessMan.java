package com.luckywars.game.luckyblock.actions.rare;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.luckyblock.LuckyBlockAction;
import com.luckywars.game.luckyblock.Rarity;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VillagerBusinessMan extends LuckyBlockAction {

    @Override
    public void onOpen(Location location, Player player) {

        String prefix = MessageUtils.color("&e[&aThe BusinessMan]&e:");

        Entity villager = location.getWorld().spawnEntity(location, EntityType.VILLAGER);
        villager.setCustomName(MessageUtils.color("&aThe BusinessMan"));
        villager.setCustomNameVisible(true);
        Bukkit.getScheduler().runTaskLater(LuckyWars.getInstance(), () -> {
            player.sendMessage(prefix + MessageUtils.color(" &aI have loads of money, have some!"));
            player.playSound(location, Sound.VILLAGER_YES, 1f, 1f);
            location.getWorld().dropItemNaturally(villager.getLocation(), new ItemStack(Material.DIAMOND, 3));
            location.getWorld().dropItemNaturally(villager.getLocation(), new ItemStack(Material.EMERALD_BLOCK, 10));
            villager.remove();
        }, 40);
    }

    @Override
    public Rarity chance() {
        return Rarity.RARE;
    }
}
