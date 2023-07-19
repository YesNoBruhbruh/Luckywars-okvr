package com.luckywars.game.luckyblock.actions.common;

import com.luckywars.game.luckyblock.LuckyBlockAction;
import com.luckywars.game.luckyblock.Rarity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ThousandSticks extends LuckyBlockAction {
    @Override
    public void onOpen(Location location, Player player) {

        // if this somehow glitches, check if the player's inventory is full and just drop it.
        // player.getInventory().firstEmpty() returns -1 if the inventory is full.
        player.getInventory().addItem(new ItemStack(Material.STICK, 1000));
    }

    @Override
    public Rarity chance() {
        return Rarity.COMMON;
    }
}