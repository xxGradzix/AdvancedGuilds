package me.xxgradzix.advancedclans.data.database.entities.clans;

import lombok.Getter;

public class PlayerStat {
    @Getter
    private final String name;
    private final double value;

    public PlayerStat(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public double getDouble() {
        return value;
    }
    public int getInt() {
        return (int) value;
    }

}
