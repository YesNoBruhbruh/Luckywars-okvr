package com.luckywars.game.object;

import com.luckywars.game.region.Cuboid;

import java.util.List;

public class MapData {

    private String name;
    private List<String> authors;

    private long lastEdit;

    private List<Pos> luckyBlocks;

    private List<Pos> spawnPoints;
    private List<Cuboid> cages;
    private Pos spectatorSpawn;

    public MapData(String name, List<String> authors, long lastEdit, List<Pos> luckyBlocks, List<Pos> spawnPoints, List<Cuboid> cages, Pos spectatorSpawn) {
        this.name = name;
        this.authors = authors;
        this.lastEdit = lastEdit;
        this.luckyBlocks = luckyBlocks;
        this.spawnPoints = spawnPoints;
        this.cages = cages;
        this.spectatorSpawn = spectatorSpawn;
    }

    public String getName() {
        return name;
    }

    public List<Pos> getSpawnPoints() {
        return spawnPoints;
    }

    public List<Cuboid> getCages() {
        return cages;
    }

    public List<Pos> getLuckyBlocks() {
        return luckyBlocks;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public long getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(long lastEdit) {
        this.lastEdit = lastEdit;
    }

    public Pos getSpectatorSpawn() {
        return spectatorSpawn;
    }

    public void setSpectatorSpawn(Pos spectatorSpawn) {
        this.spectatorSpawn = spectatorSpawn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setLuckyBlocks(List<Pos> luckyBlocks) {
        this.luckyBlocks = luckyBlocks;
    }

    public void setSpawnPoints(List<Pos> spawnPoints) {
        this.spawnPoints = spawnPoints;
    }

    public void setCages(List<Cuboid> cages) {
        this.cages = cages;
    }
}
