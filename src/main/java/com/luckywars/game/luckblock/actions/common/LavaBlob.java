package com.luckywars.game.luckblock.actions.common;

import com.luckywars.game.luckblock.LuckyBlockAction;
import com.luckywars.game.luckblock.Rarity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class LavaBlob extends LuckyBlockAction {
    private final Material[] foods = { Material.PORK, Material.RAW_BEEF, Material.MUTTON };

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
