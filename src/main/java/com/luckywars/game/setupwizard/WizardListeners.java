package com.luckywars.game.setupwizard;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.setupwizard.gui.ConfirmExitGui;
import com.luckywars.game.setupwizard.gui.WizardGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

public class WizardListeners implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getItem() == null) return;
        if(LuckyWars.getInstance().getWizardManager().hasSession(player.getUniqueId())){
            LuckyWars.getInstance().getWizardManager().getSession(player.getUniqueId()).onInteract(event.getItem());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
        InventoryHolder holder = event.getInventory().getHolder();

        if(holder instanceof WizardGui){
            event.setCancelled(true);
            ((WizardGui) holder).handleClick(event);
        } else if(holder instanceof ConfirmExitGui){
            event.setCancelled(true);
            ((ConfirmExitGui) holder).handleClick(event);
        }
    }

}
