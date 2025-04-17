package me.xxgradzix.advancedclans.data.database.entities.clan;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import entities.Clan;
import entities.User;
import lombok.Getter;
import lombok.Setter;
import me.xxgradzix.advancedclans.data.database.persister.StringListPersister;
import me.xxgradzix.advancedclans.data.database.persister.UUIDListPersister;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.countdown.CountdownOwner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DatabaseTable(tableName = "advancedguilds_clan")
public class ClanImpl implements Clan, CountdownOwner {

    @Getter
    @DatabaseField(id = true, unique = true)
    private String tag;

    @Getter
    @Setter
    @DatabaseField
    private UUID ownerUUID;

    @Getter
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


    public ClanImpl(String tag, User owner, boolean pvpEnable) {
        this.tag = tag;
        this.ownerUUID = owner.getUuid();
        this.members = new ArrayList<>();

        this.members.add(ownerUUID);
        this.pvpEnable = pvpEnable;
        alliances = new ArrayList<>();

        this.hideout = null;
    }

    public ClanImpl() {
    }

    @Override
    public boolean isAlliance(String tag)
    {
        return alliances.contains(tag.toUpperCase());
    }

    @Override
    public boolean isMember(User userImpl) {
        return members.contains(userImpl.getUuid());
    }

    @Override
    public boolean isMember(UUID uuid) {
        return members.contains(uuid);
    }

    @Override
    public void removeAlliance(Clan clanImpl) {
        alliances.remove(clanImpl.getTag());
    }

    @Override
    public boolean removeAlliance(String clan) {
        return alliances.remove(clan);
    }

    @Override
    public void addAlliance(Clan clanImpl) {
        alliances.add(clanImpl.getTag());
    }

    @Override
    public boolean hasInvite(User userImpl) {
        return invitedPlayers.contains(userImpl.getUuid());
    }

    @Override
    public void joinUser(User userImpl) {
        invitedPlayers.remove(userImpl.getUuid());
        members.add(userImpl.getUuid());
    }

    @Override
    public void resetInvite() {
        inviteAlliances.clear();
        invitedPlayers.clear();
    }

    @Override
    public void removeMember(User userImpl) {
        if(userImpl == null)
            return;
        if(ownerDeputyUUID != null && ownerDeputyUUID.equals(userImpl.getUuid()))
            ownerDeputyUUID = null;

        members.remove(userImpl.getUuid());
    }

    @Override
    public void invite(User userImpl) {
        invitedPlayers.add(userImpl.getUuid());
    }

    @Override
    public void cancelInvite(User userImpl) {
        invitedPlayers.remove(userImpl.getUuid());
    }

    @Override
    public UUID getDeputyOwnerUUID() {
        if(ownerDeputyUUID == null)
            return UUID.fromString("00000000-0000-0000-0000-000000000000");
        return ownerDeputyUUID;
    }


    @Override
    public void setDeputyOwnerUUID(User newDeputyOwner) {
        this.ownerDeputyUUID = newDeputyOwner.getUuid();
    }

    @Override
    public boolean isSuggestAlliance(Clan clanImpl) {
        return inviteAlliances.contains(clanImpl.getTag());
    }

    @Override
    public void inviteAlliance(Clan clanImpl) {
        inviteAlliances.add(clanImpl.getTag());
    }

    @Override
    public void removeInviteAlliance(Clan clanImpl) {
        inviteAlliances.remove(clanImpl.getTag());
    }

    @Override
    public void removeSuggestAlliance(Clan clanImpl) {
        inviteAlliances.remove(clanImpl.getTag());
    }

    @Override
    public boolean isOwner(User userImpl) {
        return ownerUUID.equals(userImpl.getUuid());
    }

    @Override
    public boolean isOwner(UUID uuid) {
        return ownerUUID.equals(uuid);
    }

    @Override
    public boolean isDeputy(User userImpl) {
       return (ownerDeputyUUID != null && ownerDeputyUUID.equals(userImpl.getUuid()));
    }

    @Override
    public boolean isDeputy(UUID uuid) {
        return (ownerDeputyUUID != null && ownerDeputyUUID.equals(uuid));
    }

    @Override
    public void togglePvp() {
        pvpEnable = !pvpEnable;
    }

    @Override
    public String getHideoutId() {
        return hideout;
    }

    @Override
    public boolean hasHideout() {
        return hideout != null && !hideout.isEmpty() && !hideout.isBlank() && !hideout.equalsIgnoreCase("null");
    }

    @Override
    public void setHideoutId(String hideout) {
        this.hideout = hideout;
    }
}
