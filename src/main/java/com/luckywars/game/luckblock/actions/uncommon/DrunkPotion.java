package com.luckywars.game.luckblock.actions.uncommon;

import com.luckywars.game.luckblock.LuckyBlockAction;
import com.luckywars.game.luckblock.Rarity;
import com.luckywars.game.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DrunkPotion extends LuckyBlockAction {

    @Override
    public void onOpen(Location location, Player player) {
        for (PotionEffectType effect : PotionEffectType.values()) {
            player.addPotionEffect(new PotionEffect(effect, 420, 1));
        }

        player.sendMessage(MessageUtils.color(("&x&f&f&0&0&0&0Y&x&f&f&4&c&0&0o&x&f&f&9&9&0&0u &x&f&f&e&5&0&0a&x&9&9&f&f&0&0r&x&0&0&f&f&0&0e &x&0&0&6&6&9&9d&x&0&f&0&0&e&6r&x&3&c&0&0&9&bu&x&6&8&0&0&a&2n&x&9&4&0&0&d&3k")));
    }

    @Override
    public Rarity chance() {
        return Rarity.UNCOMMON;
    }

}
