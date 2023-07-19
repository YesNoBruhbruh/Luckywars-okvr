package com.luckywars.game.luckyblock;

public enum Rarity {

    COMMON(0.01),
    UNCOMMON(0.35),
    RARE(0.75),
    EPIC(0.95),
    LEGENDARY(0.99);

    private final double chance;

    Rarity(double chance) {
        this.chance = chance;
    }

    public double getChance() {
        return chance;
    }
}