package com.luckywars.game.setupwizard.gui;

import com.luckywars.game.setupwizard.Session;
import com.luckywars.game.utils.ItemBuilder;
import com.luckywars.game.utils.SkullUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class WizardGui implements InventoryHolder {

    private final Inventory inventory;
    private final Session session;

    public WizardGui(Session session){
        this.session = session;
        this.inventory = Bukkit.createInventory(this, 27, "Choose pos to set");

        ItemStack addSpawn = new ItemBuilder(Material.ENDER_PEARL).setDisplayName("&6Add Spawn point").build();
        ItemStack addLuckyBlock = new ItemBuilder(SkullUtils.getLuckBlock()).setDisplayName("&6Add Luckyblock").build();
        ItemStack setSpectator = new ItemBuilder(Material.EYE_OF_ENDER).setDisplayName("&6Spectator spawn").build();
        ItemStack addCage = new ItemBuilder(Material.GLASS).setDisplayName("&6Add Cage").build();

        inventory.setItem(11, addSpawn);
        inventory.setItem(13, addLuckyBlock);
        inventory.setItem(15, setSpectator);
        inventory.setItem(22, addCage);

        session.getPlayer().openInventory(inventory);
    }

    public void handleClick(InventoryClickEvent event){
        switch (event.getCurrentItem().getType()) {
            case ENDER_PEARL:
                this.session.addSpawnPoint();
                break;
            case SKULL_ITEM:
                this.session.addLuckyBlock();
                break;
            case EYE_OF_ENDER:
                this.session.setSpectatorSpawn();
                break;
            case GLASS:
                this.session.addCage();
                break;
        }
        session.getPlayer().closeInventory();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

}
