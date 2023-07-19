package com.luckywars.game.luckyblock.actions.legendary;

import com.luckywars.game.luckyblock.LuckyBlockAction;
import com.luckywars.game.luckyblock.Rarity;
import com.luckywars.game.utils.PreLoadedItemLists;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LegendaryDropItem extends LuckyBlockAction {

    @Override
    public void onOpen(Location location, Player player) {
        int size = PreLoadedItemLists.legendaryItems.size();
        int i = ThreadLocalRandom.current().nextInt(0, size);
        List<ItemStack> items = PreLoadedItemLists.legendaryItems.get(i);
        for(ItemStack item : items){
            location.getWorld().dropItemNaturally(location, item);
        }
    }

    @Override
    public Rarity chance() {
        return Rarity.LEGENDARY;
    }
}
