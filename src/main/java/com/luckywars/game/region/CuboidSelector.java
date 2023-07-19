package com.luckywars.game.region;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.object.Pos;
import com.luckywars.game.utils.LocationUtils;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.xenondevs.particle.ParticleEffect;

public class CuboidSelector implements Listener {

    @EventHandler
    public void onWandUse(PlayerInteractEvent event) {
        if (event.getItem() == null) {
            return;
        }
        if (event.getItem().getItemMeta() != null && event.getItem().getItemMeta().getDisplayName() != null && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(MessageUtils.color("&eLOCATION WAND"))){
            switch (event.getAction()) {
                case LEFT_CLICK_BLOCK:
                    LuckyWars.getInstance().getSelectionManager().getSelections().put(event.getPlayer().getUniqueId(), new Selection(new Pos(event.getClickedBlock().getLocation()), null));
                    event.getPlayer().sendMessage(MessageUtils.format("&eNow select top corner using right click!"));
                    break;
                case RIGHT_CLICK_BLOCK:
                    if (!LuckyWars.getInstance().getSelectionManager().getSelections().containsKey(event.getPlayer().getUniqueId())) {
                        event.getPlayer().sendMessage(MessageUtils.format("&cPlease select bottom corner using left click first!"));
                        break;
                    }
                    Selection selection = new Selection(LuckyWars.getInstance().getSelectionManager().getSelections().get(event.getPlayer().getUniqueId()).getFirstPos(), new Pos(event.getClickedBlock().getLocation()));

                    LuckyWars.getInstance().getSelectionManager().getSelections().put(event.getPlayer().getUniqueId(), selection);

                    event.getPlayer().sendMessage(MessageUtils.format("&eNow you can add Location via clicking the menu!"));

                    for (Pos pos : LocationUtils.posFromTwoPoints(LuckyWars.getInstance().getSelectionManager().getSelections().get(event.getPlayer().getUniqueId()).getFirstPos(), LuckyWars.getInstance().getSelectionManager().getSelections().get(event.getPlayer().getUniqueId()).getSecondPos())) {
                        ParticleEffect.FLAME.display(new Location(event.getPlayer().getWorld(), pos.getX(), pos.getY(), pos.getZ()));
                    }
                    break;
                case LEFT_CLICK_AIR:
                    event.getPlayer().sendMessage(MessageUtils.format("&CLEFT CLICK ON A BLOCK YOU DUMBASS"));
                    break;
                case RIGHT_CLICK_AIR:
                    if (!LuckyWars.getInstance().getSelectionManager().getSelections().containsKey(event.getPlayer().getUniqueId())) {
                        event.getPlayer().sendMessage(MessageUtils.format("&cPlease select bottom corner using left click first!"));
                        break;
                    }
                    event.getPlayer().sendMessage(MessageUtils.format("&CRIGHT CLICK ON A BLOCK YOU DUMBASS"));
                    break;
            }
            event.setCancelled(true);
        }
    }
}