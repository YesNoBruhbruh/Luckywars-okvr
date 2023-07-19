package com.luckywars.game.luckblock.actions.common;

import com.luckywars.game.luckblock.LuckyBlockAction;
import com.luckywars.game.luckblock.Rarity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ThousandSticks extends LuckyBlockAction {
    @Override
    public void onOpen(Location location, Player player) {

        player.getInventory().addItem(new ItemStack(Material.STICK, 1000));
    }

    @Override
    public Rarity chance() {
        return Rarity.COMMON;
    }
}
