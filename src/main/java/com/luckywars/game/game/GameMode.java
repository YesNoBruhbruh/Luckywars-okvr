package com.luckywars.game.game;

public enum GameMode {
    SOLO(1, 8, 2),
    DUOS(2, 16, 4),
    QUADS(4, 16, 8),
    DUELS(1, 2, 2);
//    TEST(1, 1, 1);

    private final int playersPerTeam;
    private final int maxPlayers;
    private final int minPlayers;

    GameMode(int playersPerTeam, int maxPlayers, int minPlayers) {
        this.playersPerTeam = playersPerTeam;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
    }

    public int getPlayersPerTeam() {
        return playersPerTeam;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }
}