package com.luckywars.game.region;

import com.luckywars.game.object.Pos;
import org.bukkit.Location;

import static com.luckywars.game.utils.LocationUtils.doubleToInt;

public class Cuboid {

    private final Pos pos1;
    private final Pos pos2;

    private final int minX;
    private final int maxX;
    private int minY, maxY;
    private final int minZ;
    private final int maxZ;

    public Cuboid(Pos pos1, Pos pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;

        maxX = Math.max(doubleToInt(pos1.getX()), doubleToInt(pos2.getX()));
        minX = Math.min(doubleToInt(pos1.getX()), doubleToInt(pos2.getX()));

        maxY = Math.max(doubleToInt(pos1.getY()), doubleToInt(pos2.getY()));
        minY = Math.min(doubleToInt(pos1.getY()), doubleToInt(pos2.getY()));

        maxZ = Math.max(doubleToInt(pos1.getZ()), doubleToInt(pos2.getZ()));
        minZ = Math.min(doubleToInt(pos1.getZ()), doubleToInt(pos2.getZ()));
    }

    public boolean isInRegion(Location location) {
        return (location.getBlockX() <= maxX && location.getBlockX() >= minX) && (location.getY() <= maxY && location.getY() >= minY) && (location.getBlockZ() <= maxZ && location.getBlockZ() >= minZ);
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public Pos getPos1() {
        return pos1;
    }

    public Pos getPos2() {
        return pos2;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMinY() {
        return minY;
    }

//    private int doubleToInt(double number){
//        return NumberConversions.floor(number);
//    }
}