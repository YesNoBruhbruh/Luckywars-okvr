package com.luckywars.game.luckyblock.actions.rare;

import com.luckywars.game.luckyblock.LuckyBlockAction;
import com.luckywars.game.luckyblock.Rarity;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class TeleportBow extends LuckyBlockAction {

    @Override
    public void onOpen(Location location, Player player) {
        ItemStack teleportBow = new ItemStack(Material.BOW);
        ItemMeta meta = teleportBow.getItemMeta();

        meta.setDisplayName(MessageUtils.color("&bTeleportation Bow"));
        meta.setLore(Arrays.asList(MessageUtils.color("&eThis bow teleports you to wherever you shoot it!")));
        meta.addEnchant(Enchantment.LUCK, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        teleportBow.setItemMeta(meta);

        if (player.getInventory().firstEmpty() == -1){
            // this means player's inventory is full
            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), teleportBow);
        }else{
            player.getInventory().addItem(teleportBow);
        }
    }


    @Override
    public Rarity chance() {
        return Rarity.RARE;
    }
}