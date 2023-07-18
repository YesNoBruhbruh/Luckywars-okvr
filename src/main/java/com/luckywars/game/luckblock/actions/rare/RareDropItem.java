package com.luckywars.game.luckblock.actions.rare;

import com.luckywars.game.luckblock.LuckyBlockAction;
import com.luckywars.game.luckblock.Rarity;
import com.luckywars.game.utils.PreLoadedItemLists;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RareDropItem extends LuckyBlockAction {

    @Override
    public void onOpen(Location location, Player player) {
        int size = PreLoadedItemLists.rareItems.size();
        int i = ThreadLocalRandom.current().nextInt(0, size);
        List<ItemStack> items = PreLoadedItemLists.rareItems.get(i);
        for(ItemStack item : items){
            location.getWorld().dropItemNaturally(location, item);
        }
    }

    @Override
    public Rarity chance() {
        return Rarity.RARE;
    }

}
