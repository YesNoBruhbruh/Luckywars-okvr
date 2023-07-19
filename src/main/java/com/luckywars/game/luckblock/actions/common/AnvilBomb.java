package com.luckywars.game.luckblock.actions.common;

import com.luckywars.game.luckblock.LuckyBlockAction;
import com.luckywars.game.luckblock.Rarity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AnvilBomb extends LuckyBlockAction {

    @Override
    public void onOpen(Location location, Player player) {

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Location spawnLoc = new Location(location.getWorld(), location.getX() - i + 5, location.getY() + 10, location.getZ() + j - 5);
                location.getWorld().getBlockAt(spawnLoc).setType(Material.ANVIL);
            }
        }
        player.sendMessage("Ding ding ding ding ding ding ding ding ding ding");

    }

    @Override
    public Rarity chance() {
        return Rarity.COMMON;
    }
}
