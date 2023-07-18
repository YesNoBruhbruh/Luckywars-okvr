package com.luckywars.game.utils;

import com.luckywars.game.LuckyWars;
import com.luckywars.game.object.Pos;
import com.luckywars.game.region.Cuboid;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.NumberConversions;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {

    public static Pos locationToPos(Location location){
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        return new Pos(x, y, z, yaw, pitch);
    }

    public static Location posToLocation(Pos pos, World world){
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        float yaw = pos.getYaw();
        float pitch = pos.getPitch();
        Location location = new Location(world, x, y, z);
        location.setYaw(yaw);
        location.setPitch(pitch);
        return location;
    }

    public static List<Pos> posFromTwoPoints(Pos pos1, Pos pos2) {
        List<Pos> posList = new ArrayList<>();

        int topBlockX = (Math.max(doubleToInt(pos1.getX()), doubleToInt(pos2.getX())));
        int bottomBlockX = (Math.min(doubleToInt(pos1.getX()), doubleToInt(pos2.getX())));

        int topBlockY = (Math.max(doubleToInt(pos1.getY()), doubleToInt(pos2.getY())));
        int bottomBlockY = (Math.min(doubleToInt(pos1.getY()), doubleToInt(pos2.getY())));

        int topBlockZ = (Math.max(doubleToInt(pos1.getZ()), doubleToInt(pos2.getZ())));
        int bottomBlockZ = (Math.min(doubleToInt(pos1.getZ()), doubleToInt(pos2.getZ())));

        for (int x = bottomBlockX; x <= topBlockX; x++)
        {
            for (int z = bottomBlockZ; z <= topBlockZ; z++)
            {
                for (int y = bottomBlockY; y <= topBlockY; y++)
                {

                    // location of the blocks
                    posList.add(new Pos(x, y, z));
                }
            }
        }

        return posList;
    }

    // make two versions of this method, one is this and the other for the ones containing, "|"
    public static Pos stringToPos(String s) {
        if (s == null || s.trim().equals("")) {
            return null;
        }

        final String[] parts = s.split(":");
        if (parts.length == 3) {
            final double x = Double.parseDouble(parts[0]);
            final double y = Double.parseDouble(parts[1]);
            final double z = Double.parseDouble(parts[2]);
            return new Pos(x, y, z);
        }
        return null;
    }

    public static List<Pos> stringsToPos(String s) {
        if (s == null || s.trim().equals("")) {
            return null;
        }
//        if (!s.contains("|")){
//            if (LuckyWars.getInstance().isDebugMessages()){
//                System.out.println("ERROR: LocationUtils, Line 83");
//            }
//            return null;
//        }
        // now that we know that it isn't null, empty or doesn't have "|" we can do the things we need to do.

        //Step 1: seperate all the pos
        //Step 2: make a new pos using the seperated pos using #stringToPos() method
        //Step 3: add the pos to posList
        //Step 4: return posList.

        List<Pos> posList = new ArrayList<>();

        // Split the combinedPos string into individual Pos strings
        String[] posArray = s.split("\\|");

        for (String str : posArray) {
            // make a new posList and add that to list.
            Pos pos1 = stringToPos(str);
            posList.add(pos1);
        }
        return posList;
    }

    /**
     * Use this only when making cuboids ONLY!
     * */
    public static List<Cuboid> stringsToCuboid(String s){
        if (s == null || s.trim().equals("")) {
            return null;
        }
//        if (!s.contains("|")){
//            if (LuckyWars.getInstance().isDebugMessages()){
//                System.out.println("ERROR: LocationUtils, Line 121");
//            }
//            return null;
//        }
        List<Cuboid> cuboids = new ArrayList<>();

        // Split the combinedPos string into individual Pos strings
        String[] posArray = s.split("\\|");

// Create instances of Cuboid using two consecutive Pos strings
        for (int i = 0; i < posArray.length; i += 2) {
            if (i + 1 < posArray.length) {
                Pos pos1 = stringToPos(posArray[i]);
                Pos pos2 = stringToPos(posArray[i + 1]);
                Cuboid cuboid = new Cuboid(pos1, pos2);

                cuboids.add(cuboid);
            }
        }
        return cuboids;
    }

    public static int doubleToInt(double number){
        return NumberConversions.floor(number);
    }
}