package com.luckywars.game.luckblock.actions.common;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.luckblock.LuckyBlockAction;
import com.luckywars.game.luckblock.Rarity;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Doggo extends LuckyBlockAction {
    private final Material[] foods = { Material.PORK, Material.RAW_BEEF, Material.MUTTON };

    @Override
    public void onOpen(Location location, Player player) {

        String prefix = MessageUtils.color("&a&l[&a&lDoggo]&a&l:");
        Entity doggo = location.getWorld().spawnEntity(location, EntityType.WOLF);
        doggo.setCustomName(prefix);
        doggo.setCustomNameVisible(true);

        Bukkit.getScheduler().runTaskLater(LuckyWars.getInstance(), () -> {
            Material result = foods[new Random().nextInt(foods.length)];
            int amount = new Random().nextInt(10)+1;

            ItemStack foodItem = new ItemStack(result, amount);

            location.getWorld().dropItemNaturally(location, foodItem);
            player.sendMessage(prefix + MessageUtils.color(" &aHere is some food!"));
            doggo.remove();
        }, 25);

    }

    @Override
    public Rarity chance() {
        return Rarity.COMMON;
    }
}
