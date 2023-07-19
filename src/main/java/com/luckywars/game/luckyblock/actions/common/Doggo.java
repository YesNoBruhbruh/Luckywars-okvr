package com.luckywars.game.luckyblock.actions.common;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.luckyblock.LuckyBlockAction;
import com.luckywars.game.luckyblock.Rarity;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Doggo extends LuckyBlockAction implements Listener {
    private final Material[] foods = { Material.PORK, Material.RAW_BEEF, Material.MUTTON };

    private Entity doggo;
    private final String prefix = MessageUtils.color("&a&l[&a&lDoggo]&a&l:");

    @Override
    public void onOpen(Location location, Player player) {
        doggo = location.getWorld().spawnEntity(location, EntityType.WOLF);
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

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if (event.getEntity().getType() == EntityType.WOLF){
            // this means the entity is a wolf
            Entity doggo = event.getEntity();
            if (doggo.getCustomName() != null && doggo.getCustomName().equalsIgnoreCase(MessageUtils.decolor(prefix))){
                // this means that doggo is actually the doggo
                // this makes doggo invincible
                event.setCancelled(true);
            }
        }
    }

    @Override
    public Rarity chance() {
        return Rarity.COMMON;
    }
}
