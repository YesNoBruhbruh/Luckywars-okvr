package com.luckywars.game.luckyblock;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class LuckyBlockAction {

    public abstract void onOpen(Location location, Player player);

    public abstract Rarity chance();
}