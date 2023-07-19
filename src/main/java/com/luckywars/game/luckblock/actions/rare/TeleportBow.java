package com.luckywars.game.luckblock.actions.rare;

import com.luckywars.game.luckblock.LuckyBlockAction;
import com.luckywars.game.luckblock.Rarity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeleportBow extends LuckyBlockAction {

    @Override
    public void onOpen(Location location, Player player) {
        ItemStack teleportBow = new ItemStack(Material.BOW);
        ItemMeta meta = teleportBow.getItemMeta();

        meta.setDisplayName("Teleportation Bow and Arrow");
        teleportBow.setItemMeta(meta);

    }


    @Override
    public Rarity chance() {
        return Rarity.RARE;
    }
}
