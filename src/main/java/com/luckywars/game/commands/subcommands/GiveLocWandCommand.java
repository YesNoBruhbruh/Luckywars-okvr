package com.luckywars.game.commands.subcommands;

import com.luckywars.game.commands.SubCommand;
import com.luckywars.game.utils.ItemBuilder;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveLocWandCommand extends SubCommand {
    @Override
    public String getName() {
        return "locwand";
    }

    @Override
    public String getDescription() {
        return "&aGives you the Location Wand!";
    }

    @Override
    public String getSyntax() {
        return "/luckywars locwand";
    }

    @Override
    public String getPermission() {
        return "luckywars.command.locwand";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!isInventoryFull(player)) {
            player.getInventory().addItem(createLocWand());
        }else{
            player.getWorld().dropItemNaturally(player.getLocation(), createLocWand());
        }
    }

    private ItemStack createLocWand() {
        return new ItemBuilder(Material.BLAZE_ROD).setDisplayName("&eLOCATION WAND").build();
    }

    private boolean isInventoryFull(Player player) {
        return player.getInventory().firstEmpty() == -1;
    }
}