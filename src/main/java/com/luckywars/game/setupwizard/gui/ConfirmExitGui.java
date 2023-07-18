package com.luckywars.game.setupwizard.gui;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ConfirmExitGui implements InventoryHolder {

    private final Inventory inventory;

    public ConfirmExitGui(Player player){
        this.inventory = Bukkit.createInventory(this, 27, "Do you want to save?");

        ItemStack yes = new ItemBuilder(Material.STAINED_CLAY, 1, (byte) 5).setDisplayName("&aSave Changes").addLoreLine("&7Changed will be saved to the database.").build();
        ItemStack no = new ItemBuilder(Material.STAINED_CLAY, 1, (byte) 14).setDisplayName("&cDont Save Changes").addLoreLine("&7Nothing will be saved. Changes are lost forever!").build();

        inventory.setItem(11, yes);
        inventory.setItem(15, no);

        player.openInventory(inventory);
    }

    public void handleClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        LuckyWars.getInstance().getWizardManager().endSession(player, event.getCurrentItem().getDurability() == 5);
        player.closeInventory();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

}
