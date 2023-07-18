package com.luckywars.game.luckblock.actions.rare;

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

public class ExplosiveChicken extends LuckyBlockAction {

    @Override
    public void onOpen(Location location, Player player) {

        String prefix = MessageUtils.color("&e[&cExplosive chicken]&e:");

        Entity chicken = location.getWorld().spawnEntity(location, EntityType.CHICKEN);
        chicken.setCustomName(MessageUtils.color("&cExplosive chicken"));
        chicken.setCustomNameVisible(true);
        Bukkit.getScheduler().runTaskLater(LuckyWars.getInstance(), () -> {
            player.sendMessage(prefix + MessageUtils.color(" &cDIE MUHAHAHAHAHAHA >:)"));
            player.playSound(location, Sound.CHICKEN_EGG_POP, 1f, 1f);
            chicken.getLocation().getWorld().createExplosion(chicken.getLocation(), 5, false);
            chicken.remove();
        }, 60);

    }

    @Override
    public Rarity chance() {
        return Rarity.RARE;
    }

}
