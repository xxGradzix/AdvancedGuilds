package me.xxgradzix.advancedclans.data.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import me.xxgradzix.advancedclans.data.database.persister.StringListPersister;
import me.xxgradzix.advancedclans.data.database.persister.UUIDListPersister;
import org.stringtemplate.v4.ST;

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

    @Getter
    @Setter
    @DatabaseField
    private UUID ownerUUID;

    @DatabaseField
    private UUID ownerDeputyUUID;

    @Getter
    @DatabaseField(persisterClass = UUIDListPersister.class, columnDefinition = "LONGBLOB")
    private List<UUID> members;


    @Getter
    @DatabaseField(persisterClass = StringListPersister.class, columnDefinition = "LONGBLOB")
    private List<String> alliances;

    @Getter
    @DatabaseField
    private boolean pvpEnable;

    @DatabaseField
    private String hideout;


    private final List<UUID> invitedPlayers = new ArrayList<>();

    private final List<String> inviteAlliances = new ArrayList<>();



    public Clan(String tag, User owner, boolean pvpEnable) {
        this.tag = tag;
        this.ownerUUID = owner.getUuid();
        this.members = new ArrayList<>();

        this.members.add(ownerUUID);
        this.pvpEnable = pvpEnable;
        alliances = new ArrayList<>();

        this.hideout = null;
    }

    public Clan() {
    }

    public boolean isAlliance(String tag)
    {
        return alliances.contains(tag.toUpperCase());
    }

    public boolean isMember(User user) {
        return members.contains(user.getUuid());
    }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid);
    }

    public void removeAlliance(Clan clan) {
        alliances.remove(clan.getTag());
    }

    public boolean removeAlliance(String clan) {
        return alliances.remove(clan);
    }

    public void addAlliance(Clan clan) {
        alliances.add(clan.getTag());
    }

    public boolean hasInvite(User user) {
        return invitedPlayers.contains(user.getUuid());
    }

    public void joinUser(User user) {
        invitedPlayers.remove(user.getUuid());
        members.add(user.getUuid());
    }

    public void resetInvite() {
        inviteAlliances.clear();
        invitedPlayers.clear();
    }

    public void removeMember(User user) {
        if(user == null)
            return;
        if(ownerDeputyUUID != null && ownerDeputyUUID.equals(user.getUuid()))
            ownerDeputyUUID = null;

        members.remove(user.getUuid());
    }

    public void invite(User user) {
        invitedPlayers.add(user.getUuid());
    }

    public void cancelInvite(User user) {
        invitedPlayers.remove(user.getUuid());
    }

    public UUID getDeputyOwnerUUID() {
        return ownerDeputyUUID;
    }


    public void setDeputyOwnerUUID(User newDeputyOwner) {
        this.ownerDeputyUUID = newDeputyOwner.getUuid();
    }

    public boolean isSuggestAlliance(Clan clan) {
        return inviteAlliances.contains(clan.getTag());
    }

    public void inviteAlliance(Clan clan) {
        inviteAlliances.add(clan.getTag());
    }

    public void removeInviteAlliance(Clan clan) {
        inviteAlliances.remove(clan.getTag());
    }

    public void removeSuggestAlliance(Clan clan) {
        inviteAlliances.remove(clan.getTag());
    }

    public boolean isOwner(User user) {
        return ownerUUID.equals(user.getUuid());
    }

    public boolean isOwner(UUID uuid) {
        return ownerUUID.equals(uuid);
    }

    public boolean isDeputy(User user) {
       return (ownerDeputyUUID != null && ownerDeputyUUID.equals(user.getUuid()));
    }

    public boolean isDeputy(UUID uuid) {
        return (ownerDeputyUUID != null && ownerDeputyUUID.equals(uuid));
    }

    public void togglePvp() {
        pvpEnable = !pvpEnable;
    }

    public String getHideoutId() {
        return hideout;
    }

    public boolean hasHideout() {
        return hideout != null && !hideout.isEmpty() && !hideout.isBlank() && !hideout.equalsIgnoreCase("null");
    }

    public void setHideoutId(String hideout) {
        this.hideout = hideout;
    }
}
