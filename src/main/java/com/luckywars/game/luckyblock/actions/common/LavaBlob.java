package com.luckywars.game.luckyblock.actions.common;

import com.luckywars.game.luckyblock.LuckyBlockAction;
import com.luckywars.game.luckyblock.Rarity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class LavaBlob extends LuckyBlockAction {

    @Override
    public void onOpen(Location location, Player player) {

        int min = 3;
        int max = -3;
        double randIntX = Math.random() * (max - min + 1) + min;
        double randIntZ = Math.random() * (max - min + 1) + min;

        Location spawnLoc = new Location((location.getWorld()), location.getX() + randIntX, location.getY(), location.getZ() + randIntZ);
        spawnLoc.getBlock().setType(Material.LAVA);
    }
    @Override
    public Rarity chance() {
        return Rarity.COMMON;
    }
}