package com.luckywars.game.database;

import java.sql.Connection;

public interface IConnectedCallback {

    void onConnected(Connection connection);
    void onDisconnect();

}
