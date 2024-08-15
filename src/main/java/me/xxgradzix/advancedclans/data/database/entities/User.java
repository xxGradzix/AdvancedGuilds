package me.xxgradzix.advancedclans.data.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.UUID;

@DatabaseTable(tableName = "advancedguilds_user")
public class User {

    @DatabaseField(id = true, unique = true)
    private UUID uuid;
    @DatabaseField
    private int kills;
    @DatabaseField
    private int death;
    @DatabaseField
    private int points;
//    @DatabaseField(foreign = true)
//    private Clan clan;
    @DatabaseField
    private String clanTag;

    public User(UUID uuid, int kills, int death, int points, Clan clan) {
        this.kills = kills;
        this.death = death;
        this.points = points;
        this.uuid = uuid;
        if(clan!=null) {
            this.clanTag = clan.getTag();
        }
    }

    public User() {
    }

    public User(Player player, int points) {
        this.uuid = player.getUniqueId();
        this.kills = 0;
        this.death = 0;
        this.points = points;
    }

    public void increaseDeath() {
        this.death++;
    }

    public void increaseKill( ) {
        this.kills++;
    }

    public void takePoint(int points) {
        this.points-=points;
    }

    public void addPoint(int points) {
        this.points+=points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setClan(Clan clan) {
        this.clanTag = clan.getTag();
    }

    public String getClanTag() {
        return clanTag;
    }

    public boolean hasClan() {
        return clanTag != null && !clanTag.isEmpty() && !clanTag.equalsIgnoreCase("null");
    }

    public void resetKill() {
        kills = 0;
    }

    public void resetDeath() {
        death = 0;
    }

    public int getKills() {
        return kills;
    }

    public int getDeath() {
        return death;
    }

    public int getPoints() {
        return points;
    }

    public UUID getUuid() {
        return uuid;
    }
}
