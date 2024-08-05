package me.xxgradzix.advancedclans.data.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import me.xxgradzix.advancedclans.data.database.persister.StringListPersister;
import me.xxgradzix.advancedclans.data.database.persister.UUIDListPersister;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//@Data
//@AllArgsConstructor
@DatabaseTable(tableName = "advancedguilds_clan")
public class Clan {

    @Getter
    @DatabaseField(id = true, unique = true)
    private String tag;

    @Setter
    @DatabaseField(foreign = true)
    private User owner;

    @DatabaseField(foreign = true)
    private User ownerDeputy;

    @Getter
    @DatabaseField(persisterClass = UUIDListPersister.class)
    private List<UUID> members;


    @Getter
    @DatabaseField(persisterClass = StringListPersister.class)
    private List<String> alliances;

    @Getter
    @DatabaseField
    private boolean pvpEnable;


    private List<User> invitedPlayers = new ArrayList<>();

    private List<Clan> inviteAlliances = new ArrayList<>();


    public Clan(String tag, User owner, User deputyOwner, boolean pvpEnable) {
        this(tag, owner, pvpEnable);
        this.ownerDeputy = deputyOwner;
    }

    public Clan(String tag, User owner, boolean pvpEnable) {
        this.tag = tag;
        this.owner = owner;
        this.members = new ArrayList<>();

        this.members.add(owner.getUuid());
        this.pvpEnable = pvpEnable;
//        members = new F<>();
        alliances = new ArrayList<>();
    }

    public Clan() {
    }

    public boolean isAlliance(String tag)
    {
        return alliances.contains(tag.toUpperCase());
    }

    public boolean isMember(User user)
    {
        return members.contains(user.getUuid());
    }
    public boolean isMember(UUID uuid)
    {
//        return members.stream().map(User::getUuid).anyMatch(uuid1 -> {
//            return uuid1.equals(uuid);
//        });
        return members.contains(uuid);
    }
    public boolean removeAlliance(Clan clan)
    {
        return alliances.remove(clan.getTag());
    }

    public boolean removeAlliance(String clan)
    {
        return alliances.remove(clan);
    }

    public void addAlliance(Clan clan)
    {
        alliances.add(clan.getTag());
    }

    public boolean hasInvite(User user)
    {
        return invitedPlayers.contains(user);
    }

    public void joinUser(User user) {
        invitedPlayers.remove(user);
        members.add(user.getUuid());
    }

    public void resetInvite()
    {
        inviteAlliances.clear();
        invitedPlayers.clear();
    }
    public void removeMember(User user) {
        if(user == null)
            return;
        if(ownerDeputy.equals(user))
            ownerDeputy = null;

        members.remove(user);
    }
    public void invite(User user) {
        invitedPlayers.add(user);
    }
    public void cancelInvite(User user) {
        invitedPlayers.remove(user);
    }

    public User getOwnerUUID() {
        return owner;
    }

    public User getDeputyOwnerUUID() {
        return ownerDeputy;
    }


    public void setDeputyOwnerUUID(User newDeputyOwner) {
        this.ownerDeputy = newDeputyOwner;
    }


    public boolean isSuggestAlliance(Clan clan)
    {
        return inviteAlliances.contains(clan);
    }
    public boolean inviteAlliance(Clan clan)
    {
        return inviteAlliances.add(clan);
    }

    public void removeInviteAlliance(Clan clan) {
        inviteAlliances.remove(clan);
    }

    public void removeSuggestAlliance(Clan clan)
    {
        inviteAlliances.remove(clan);
    }
    public boolean isOwner(User user) {
        return owner.equals(user);
    }
    public boolean isOwner(UUID uuid) {
        return owner.getUuid().equals(uuid);
    }

//    public void addMember(UUID uuid) {
//        members.add(uuid);
//    }

    public boolean isDeputy(User user) {
       return (ownerDeputy != null && ownerDeputy.equals(user));
    }
    public boolean isDeputy(UUID uuid) {
        return (ownerDeputy != null && ownerDeputy.getUuid().equals(uuid));
    }


    public void togglePvp() {
        pvpEnable = !pvpEnable;
    }
}
