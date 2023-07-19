package com.luckywars.game.luckyblock.actions.rare;

import com.luckywars.game.luckyblock.LuckyBlockAction;
import com.luckywars.game.luckyblock.Rarity;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;

public class CarpetBomb extends LuckyBlockAction {

    @Override
    public void onOpen(Location location, Player player) {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Location spawnLoc = new Location(location.getWorld(), location.getX() - i + 5, location.getY() + 10, location.getZ() + j - 5);
                TNTPrimed bomb = (TNTPrimed) location.getWorld().spawnEntity(spawnLoc, EntityType.PRIMED_TNT);
                bomb.setFuseTicks(40);
            }
        }
        player.sendMessage("Look up. Now.");
    }

    @Override
    public Rarity chance() {
        return Rarity.RARE;
    }
}
