package me.xxgradzix.advancedclans.data.database.entities.clan;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.countdown.CountdownOwner;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@DatabaseTable(tableName = "advancedguilds_user")
public class User implements CountdownOwner {

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
            setClan(clan);
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

        if(this.clanTag != null && !this.clanTag.isEmpty()) {
            removePermission(uuid, this.clanTag.toLowerCase());
        }

        if(clan != null) {
            addPermission(uuid, clan.getTag().toLowerCase());
        }

        if(clan == null) {
            this.clanTag = null;
            return;
        }
        this.clanTag = clan.getTag();
    }

    private static final LuckPerms luckPerms = LuckPermsProvider.get();

    public static void addPermission(UUID userUuid, String permission) {
//        luckPerms.getUserManager().modifyUser(userUuid, user -> {
//            user.data().add(Node.builder(permission).build());
//        });

        luckPerms.getUserManager().modifyUser(userUuid, user -> {
//            user.data().add(Node.builder(permission).build());
            user.data().add(InheritanceNode.builder(permission).build());
        });

//        net.luckperms.api.model.user.User user = luckPerms.getUserManager().getUser(userUuid);
//        user.data().add(InheritanceNode.builder("dummy_group").build());
//        GameManager.permissionProvider.getUserManager().saveUser(user);
    }

    public static void removePermission(UUID userUuid, String permission) {
//        luckPerms.getUserManager().modifyUser(userUuid, user -> {
//            user.data().toCollection().stream().filter(node -> node.getKey().contains(permission)).forEach(user.data()::remove);
////            user.data().remove(Node.builder(permission).build());
//        });
        luckPerms.getUserManager().modifyUser(userUuid, user -> {
//            user.data().add(Node.builder(permission).build());
            user.data().remove(InheritanceNode.builder(permission).build());
        });

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

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
