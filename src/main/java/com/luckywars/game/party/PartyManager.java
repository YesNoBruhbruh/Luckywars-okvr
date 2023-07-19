package com.luckywars.game.party;

import com.luckywars.game.LuckyWars;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PartyManager {

    private final Map<UUID, Party> parties;

    public PartyManager(){
        this.parties = new HashMap<>();
    }

    public void createParty(Player player){
        UUID uuid = UUID.randomUUID();
        LuckyWars.getInstance().getGamePlayer(player.getUniqueId()).setPartyId(uuid);
        this.parties.put(uuid, new Party(uuid, player));
    }

    public Party getParty(UUID uuid){
        return parties.get(uuid);
    }

}
