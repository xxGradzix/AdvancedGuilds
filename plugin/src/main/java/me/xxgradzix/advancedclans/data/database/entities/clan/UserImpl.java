package me.xxgradzix.advancedclans.data.database.entities.clan;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import entities.Clan;
import entities.User;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.countdown.CountdownOwner;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@DatabaseTable(tableName = "advancedguilds_user")
public class UserImpl implements User, CountdownOwner {

    @DatabaseField(id = true, unique = true)
    private UUID uuid;
    @DatabaseField
    private int kills;
    @DatabaseField
    private int death;
    @DatabaseField
    private int points;
    @DatabaseField
    private String clanTag;

    public UserImpl(UUID uuid, int kills, int death, int points, ClanImpl clanImpl) {
        this.kills = kills;
        this.death = death;
        this.points = points;
        this.uuid = uuid;
        if(clanImpl !=null) {
            setClan(clanImpl);
        }
    }

    public UserImpl() {
    }

    public UserImpl(Player player, int points) {
        this.uuid = player.getUniqueId();
        this.kills = 0;
        this.death = 0;
        this.points = points;
    }

    @Override
    public void increaseDeath() {
        this.death++;
    }

    @Override
    public void increaseKill( ) {
        this.kills++;
    }

    @Override
    public void takePoint(int points) {
        this.points-=points;
    }

    @Override
    public void addPoint(int points) {
        this.points+=points;
    }

    @Override
    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public void setClan(Clan clanImpl) {

        if(this.clanTag != null && !this.clanTag.isEmpty()) {
            removePermission(uuid, this.clanTag.toLowerCase());
        }

        if(clanImpl != null) {
            addPermission(uuid, clanImpl.getTag().toLowerCase());
        }

        if(clanImpl == null) {
            this.clanTag = null;
            return;
        }
        this.clanTag = clanImpl.getTag();
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


    @Override
    public String getClanTag() {
        return clanTag;
    }

    @Override
    public boolean hasClan() {
        return clanTag != null && !clanTag.isEmpty() && !clanTag.equalsIgnoreCase("null");
    }

    @Override
    public void resetKill() {
        kills = 0;
    }

    @Override
    public void resetDeath() {
        death = 0;
    }

    @Override
    public int getKills() {
        return kills;
    }

    @Override
    public int getDeath() {
        return death;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
