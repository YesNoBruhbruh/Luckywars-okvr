package com.luckywars.game.luckblock.actions.uncommon;

import com.luckywars.game.luckblock.LuckyBlockAction;
import com.luckywars.game.luckblock.Rarity;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FastSpeed extends LuckyBlockAction {
    @Override
    public void onOpen(Location location, Player player) {

        String prefix = MessageUtils.color("&c[&cThe Speed God]&c:");

        PotionEffect potionEffect = new PotionEffect(PotionEffectType.SPEED, 20*7, 4);
        player.addPotionEffect(potionEffect);
        player.sendMessage(prefix + MessageUtils.color(" &aHas given you a temporary gift! use it wisely."));
    }

    @Override
    public Rarity chance() {
        return Rarity.UNCOMMON;
    }
}
