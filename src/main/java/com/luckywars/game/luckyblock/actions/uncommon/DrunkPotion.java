package com.luckywars.game.luckyblock.actions.uncommon;

import com.luckywars.game.luckyblock.LuckyBlockAction;
import com.luckywars.game.luckyblock.Rarity;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DrunkPotion extends LuckyBlockAction {

    @Override
    public void onOpen(Location location, Player player) {
        for (PotionEffectType effect : PotionEffectType.values()) {
            if (effect != null){
                player.addPotionEffect(new PotionEffect(effect, 100, 1));
            }
        }

        player.sendMessage(MessageUtils.rainbow("You are drunk."));
    }

    @Override
    public Rarity chance() {
        return Rarity.UNCOMMON;
    }
}