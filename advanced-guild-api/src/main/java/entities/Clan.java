package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface Clan {

    boolean isAlliance(String tag);

    boolean isMember(User userImpl);

    boolean isMember(UUID uuid);

    void removeAlliance(Clan clanImpl);

    boolean removeAlliance(String clan);

    void addAlliance(Clan clanImpl);

    boolean hasInvite(User userImpl);

    void joinUser(User userImpl);

    void resetInvite();

    void removeMember(User userImpl);

    void invite(User userImpl);

    void cancelInvite(User userImpl);

    UUID getDeputyOwnerUUID();

    void setDeputyOwnerUUID(User newDeputyOwner);

    boolean isSuggestAlliance(Clan clanImpl);

    void inviteAlliance(Clan clanImpl);

    void removeInviteAlliance(Clan clanImpl);

    void removeSuggestAlliance(Clan clanImpl);

    boolean isOwner(User userImpl);

    boolean isOwner(UUID uuid);

    boolean isDeputy(User userImpl);

    boolean isDeputy(UUID uuid);

    void togglePvp();

    String getHideoutId();

    boolean hasHideout();

    void setHideoutId(String hideout);

    String getTag();

    UUID getOwnerUUID();

    List<UUID> getMembers();

    List<String> getAlliances();

    boolean isPvpEnable();

    void setOwnerUUID(UUID ownerUUID);

}
