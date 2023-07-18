package com.luckywars.game.region;

import com.luckywars.game.object.Pos;

public class Selection {

    private final Pos firstPos;
    private final Pos secondPos;

    public Selection(Pos firstPos, Pos secondPos) {
        this.firstPos = firstPos;
        this.secondPos = secondPos;
    }

    public Pos getFirstPos() {
        return firstPos;
    }

    public Pos getSecondPos() {
        return secondPos;
    }
}
