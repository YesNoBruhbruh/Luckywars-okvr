package com.luckywars.game.setupwizard;

import com.luckywars.game.game.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WizardManager {

    private final Map<UUID, Session> sessions;

    public WizardManager(){
        this.sessions = new HashMap<>();
    }

    public void createSession(Player player, String map, GameMode gameMode){
        this.sessions.put(player.getUniqueId(), new Session(player, map, gameMode));
    }

    public void endSession(Player player, boolean save){
        this.sessions.get(player.getUniqueId()).delete(save);
        this.sessions.remove(player.getUniqueId());
    }

    public Session getSession(UUID uuid){
        return sessions.get(uuid);
    }

    public boolean hasSession(UUID uuid){
        return sessions.containsKey(uuid);
    }

}
