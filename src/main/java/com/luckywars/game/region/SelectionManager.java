package com.luckywars.game.region;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SelectionManager {

    private final Map<UUID, Selection> selections;

    public SelectionManager() {
        this.selections = new HashMap<>();
    }

    public void deleteSelection(UUID uuid) {
        this.selections.remove(uuid);
    }

    public Selection getSelection(UUID uuid) {
        return this.selections.get(uuid);
    }

    public Map<UUID, Selection> getSelections() {
        return selections;
    }
}
