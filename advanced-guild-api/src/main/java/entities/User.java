package entities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface User {


    void increaseDeath();
    void increaseKill();

    void takePoint(int points);

    void addPoint(int points);

    void setPoints(int points);

    void setClan(Clan clanImpl);

    String getClanTag();

    boolean hasClan();

    void resetKill();

    void resetDeath();

    int getKills();

    int getDeath();

    int getPoints();

    UUID getUuid();

    Player getPlayer();
    
}
