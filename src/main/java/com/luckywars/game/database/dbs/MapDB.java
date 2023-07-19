package com.luckywars.game.database.dbs;

import com.luckywars.game.object.MapData;
import com.luckywars.game.object.Pos;
import com.luckywars.game.utils.LocationUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class MapDB {

    private final Connection connection;

    public MapDB(Connection connection){
        this.connection = connection;
    }

    public MapData loadMapData(String map){
        try {

            PreparedStatement ps = connection.prepareStatement("SELECT * from mapdata WHERE NAME=?");
            ps.setString(1, map);

            ResultSet results = ps.executeQuery();

            if (results.next()){

                // we make a default MapData first.
                MapData mapData = new MapData(
                        "",
                        new ArrayList<>(),
                        0,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new Pos(0, 0, 0));

                // now we update all of it.

                mapData.setName(map);
                mapData.setAuthors(Collections.singletonList(results.getString("AUTHORS")));
                mapData.setLastEdit(results.getLong("LASTEDIT"));
                mapData.setLuckyBlocks(LocationUtils.stringsToPos(results.getString("LUCKYBLOCKS")));
                mapData.setSpawnPoints(LocationUtils.stringsToPos(results.getString("SPAWNPOINTS")));
                mapData.setCages(LocationUtils.stringsToCuboid(results.getString("CAGES")));
                mapData.setSpectatorSpawn(LocationUtils.stringToPos(results.getString("SPECTATORSPAWN")));

                ps.close();

                return mapData;
            }

//            PreparedStatement ps = connection.prepareStatement("SELECT DATA from mapdata WHERE NAME=?");
//            ps.setString(1, map);
//
//            ResultSet results = ps.executeQuery();
//            if(results.next()){
//                String json = results.getString("DATA");
//                return MapDataSerializer.deserialize(json);
//            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void saveMapData(String map, MapData data){
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO mapdata (NAME, AUTHORS, LASTEDIT, LUCKYBLOCKS, SPAWNPOINTS, CAGES, SPECTATORSPAWN) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, map);
            ps.setString(2, data.getAuthors().toString());
            ps.setLong(3, data.getLastEdit());
            // for luckyblocks and other things, just turn them into a string and then use a for loop to combine them into one string and
            // set it that way

            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();

            for (int i = 0; i < data.getLuckyBlocks().size(); i++){
                if (i > 0) {
                    sb1.append("|");
                }

                sb1.append(data.getLuckyBlocks().get(i).toString());
            }
            ps.setString(4, sb1.toString());

            for (int i = 0; i < data.getSpawnPoints().size(); i++){
                if (i > 0) {
                    sb2.append("|");
                }

                sb2.append(data.getSpawnPoints().get(i).toString());
            }
            ps.setString(5, sb2.toString());

            for (int i = 0; i < data.getCages().size(); i++){
                if (i > 0) {
                    sb3.append("|");
                }

                sb3.append(data.getCages().get(i).getPos1().toString());
                sb3.append("|");
                sb3.append(data.getCages().get(i).getPos2().toString());
            }
            ps.setString(6, sb3.toString());
            ps.setString(7, data.getSpectatorSpawn().toString());

//            ps.setString(2, MapDataSerializer.serialize(data));
//
//            ps.setString(3, MapDataSerializer.serialize(data));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}