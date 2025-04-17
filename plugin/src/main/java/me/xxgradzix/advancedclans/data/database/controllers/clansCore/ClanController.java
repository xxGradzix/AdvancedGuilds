package me.xxgradzix.advancedclans.data.database.controllers.clansCore;

import com.xxgradzix.advancedguildsapi.events.clan.*;
import entities.Clan;
import entities.User;
import lombok.Setter;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.config.Config;
import me.xxgradzix.advancedclans.data.database.controllers.hideouts.GuildHideOutController;
import me.xxgradzix.advancedclans.data.database.entities.clan.ClanImpl;
import me.xxgradzix.advancedclans.data.database.entities.clan.UserImpl;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataService;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import me.xxgradzix.advancedclans.scheduler.TopRankScheduler;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import me.xxgradzix.advancedclans.utils.ConsoleColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class ClanController {

    private final AdvancedGuilds plugin;
    private final ClanAndUserDataService clanAndUserDataService;

    @Setter
    private TopRankScheduler topRankScheduler;


    public ClanController(AdvancedGuilds plugin, ClanAndUserDataService clanAndUserDataService)
    {
        this.plugin = plugin;
        this.clanAndUserDataService = clanAndUserDataService;
    }

    public void setOwner(Player executor, Player target) {

        User owner = ClanAndUserDataService.getCachedUser(executor.getUniqueId());
        Clan ownerClanImpl = ClanAndUserDataService.getCachedClan(owner.getClanTag());

        User targerUserImpl = ClanAndUserDataService.getCachedUser(target.getUniqueId());

        boolean isExecutorOwner = isOwner(ownerClanImpl, owner);

        if(!isExecutorOwner) {
            MessageManager.sendMessageFormated(executor, MessageManager.NOT_OWNER, MessageType.CHAT);
            return;
        }

        if(isSame(executor, targerUserImpl.getUuid()))
        {
            MessageManager.sendMessageFormated(executor, MessageManager.ALREADY_OWNER, MessageType.CHAT);
            return;
        }
        if(!isMemberInYourClan(ownerClanImpl, targerUserImpl))
        {
            MessageManager.sendMessageFormated(executor, MessageManager.PLAYER_DOES_NOT_BELONG_TO_YOUR_CLAN, MessageType.CHAT);
            return;
        }

        boolean success = handleSetOwner(ownerClanImpl, targerUserImpl);
        if(success) {
            MessageManager.sendMessageFormated(executor, MessageManager.NEW_OWNER_SET.replace("{player}", target.getName()), MessageType.CHAT);
        }
    }

    private boolean handleSetOwner(Clan clanImpl, User newOwnerUUID) {
        ChangeOwnerClanEvent event = new ChangeOwnerClanEvent(clanImpl, ClanAndUserDataService.getCachedUser(clanImpl.getOwnerUUID()), newOwnerUUID);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            clanImpl.setOwnerUUID(newOwnerUUID.getUuid());

            ClanAndUserDataService.updateClan(clanImpl);
            return true;
        }
        return false;
    }

    public void changePvpStatus(Player deputyOwner) {
        User userImpl = ClanAndUserDataService.getCachedUser(deputyOwner.getUniqueId());

        Clan clanImpl = ClanAndUserDataService.getCachedClan(userImpl.getClanTag());

        if (!isDeputyOwner(clanImpl, deputyOwner) && !isOwner(clanImpl, userImpl)) {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.NOT_DEPUTY, MessageType.CHAT);
            return;
        }
        clanImpl.togglePvp();
        if (clanImpl.isPvpEnable()) {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.PVP_ENABLED, MessageType.CHAT);
        } else {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.PVP_DISABLED, MessageType.CHAT);
        }
        ClanAndUserDataService.updateClan(clanImpl);
    }

    public void inviteUser(Player deputyOwner, Player target) {
        User userImpl = ClanAndUserDataService.getCachedUser(deputyOwner.getUniqueId());
        Clan clanImpl = ClanAndUserDataService.getCachedClan(userImpl.getClanTag());
        User targetUserImpl = ClanAndUserDataService.getCachedUser(target.getUniqueId());

        if(!isDeputyOwner(clanImpl, deputyOwner) && !isOwner(clanImpl, userImpl)) {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.NOT_DEPUTY, MessageType.CHAT);
            return;
        }

        if(!target.isOnline()) {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.PLAYER_NOT_ONLINE, MessageType.CHAT);
            return;
        }

        if(isLimitMember(clanImpl)) {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.LIMIT_MEMBERS_REACHED, MessageType.CHAT);
            return;
        }

        if(hasClan(target)) {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.PLAYER_BELONGS_TO_ANOTHER_CLAN, MessageType.CHAT);
            return;
        }
        if(clanImpl.hasInvite(targetUserImpl)) {
            CancelInviteClanEvent event = new CancelInviteClanEvent(clanImpl, target);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                clanImpl.cancelInvite(targetUserImpl);
                MessageManager.sendMessageFormated(deputyOwner, MessageManager.CLAN_INVITE_CANCELED_SENDER.replace("{player}", target.getName()), MessageType.CHAT);
                MessageManager.sendMessageFormated(target, MessageManager.CLAN_INVITE_CANCELED_RECIEVER.replace("{clan}", clanImpl.getTag()), MessageType.CHAT);
            }
            return;
        }

        InviteClanEvent event = new InviteClanEvent(clanImpl, target);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            clanImpl.invite(targetUserImpl);
            ClanAndUserDataService.updateClan(clanImpl);
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.CLAN_INVITED_SENDER.replace("{player}", target.getName()), MessageType.CHAT);
            MessageManager.sendMessageFormated(target, MessageManager.CLAN_INVITE_RECEIVED.replace("{clan}", clanImpl.getTag()), MessageType.CHAT);
        }
    }

    private boolean hasClan(Player target) {
        User userImpl = ClanAndUserDataService.getCachedUser(target.getUniqueId());
        return userImpl.hasClan();
    }

    private boolean isDeputyOwner(Clan clanImpl, Player player) {
        if(clanImpl ==null)
            return false;

        if(clanImpl.getDeputyOwnerUUID()==null)
            return false;
        return clanImpl.getDeputyOwnerUUID().equals(player.getUniqueId());
    }

    private boolean isLimitMember(Clan clanImpl) {
        return clanImpl.getMembers().size()>= Config.maxPlayers;
    }

    public void infoClan(Player player, Clan clanImpl) {
        OptionalInt clanRankIndexByTag = topRankScheduler.getClanRankIndexByTag(clanImpl.getTag());
        int index = 9999;
        if(clanRankIndexByTag.isPresent())
            index = clanRankIndexByTag.getAsInt()+1;
        String infoMessage = MessageManager.CLAN_INFO;

        infoMessage = infoMessage.replace("{tag}", clanImpl.getTag())
                        .replace("{owner}", getPlayerName(clanImpl.getOwnerUUID()))
                        .replace("{deputy-owner}", getPlayerName(clanImpl.getDeputyOwnerUUID()))
                        .replace("{points}", getAveragePoint(clanImpl))
                        .replace("{members-online}", String.valueOf(countOnlineMember(clanImpl)))
                        .replace("{members-size}", String.valueOf(clanImpl.getMembers().size()))
                        .replace("{rank}", String.valueOf(index))
                        .replace("{members}", getClanMembers(clanImpl));

        MessageManager.sendMessageFormated(player, infoMessage, MessageType.CHAT);
    }

    private String getClanMembers(Clan clanImpl) {
        List<UUID> members = clanImpl.getMembers();
        List<String> membersText = new ArrayList<>();

        for(UUID uuid : members)
        {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            if(player.isOnline())
                membersText.add(Config.onlinePlayerColor + player.getName());
            else
                membersText.add(Config.offlinePlayerColor + player.getName());

        }
        return String.join(", ", membersText);
    }

    public int countOnlineMember(Clan clanImpl) {
        int online = 0;
        for (UUID uuid : clanImpl.getMembers()) {
            Player player = Bukkit.getPlayer(uuid);
            if(player!=null)
                online++;
        }
        return online;
    }

    public String getAveragePoint(Player player)
    {
        User userImpl = ClanAndUserDataService.getCachedUser(player.getUniqueId());
        if(userImpl ==null || !userImpl.hasClan())
            return Config.noClan;

        Clan clanImpl = ClanAndUserDataService.getCachedClan(userImpl.getClanTag());
        if(!doesClanFulfillThreshold(clanImpl)) {
            return ColorFixer.addColors(Config.noEnoughPlayerForRankingInClan);
        }
        return getAveragePoint(clanImpl);
    }

    public String getAveragePoint(Clan clanImpl)
    {
        List<UUID> members = clanImpl.getMembers();
        int sum = 0;
        int count = 0;
        for (UUID uuid : members) {
            User tempUserImpl = ClanAndUserDataService.getCachedUser(uuid);
            if(tempUserImpl ==null) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                plugin.getLogger().info(ConsoleColor.RED+"Bląd - Gracz o nazwie " + player.getName() + "  nalezy do klanu "+ clanImpl.getTag() + " ale nie znajduje go jako obiekt User");
                continue;
            }
            sum += tempUserImpl.getPoints();
            count++;
        }
        double average = (double) sum / count;
        return String.valueOf((int) average);
    }

    private String getPlayerName(UUID uuid) {
        System.out.println("UUID: "+uuid);
        if(uuid==null || uuid.equals(new UUID(0, 0))) {
            System.out.println(Config.nonePlayer);
            return Config.nonePlayer;

        }

        return Bukkit.getOfflinePlayer(uuid).getName();
    }
    private String getPlayerName(User userImpl) {
        if(userImpl == null || userImpl.getUuid()==null)
            return Config.nonePlayer;

        return Bukkit.getOfflinePlayer(userImpl.getUuid()).getName();
    }

    public void joinClan(Player player, Clan clanImpl)
    {
        User userImpl = ClanAndUserDataService.getCachedUser(player.getUniqueId());
        if(userImpl.hasClan()) {
            MessageManager.sendMessageFormated(player, MessageManager.PLAYER_BELONGS_TO_ANOTHER_CLAN, MessageType.CHAT);
            return;
        }
        if(!clanImpl.hasInvite(userImpl)) {
            MessageManager.sendMessageFormated(player, MessageManager.NOT_RECEIVED_INVITE.replace("{clan}", clanImpl.getTag()), MessageType.CHAT);
            return;
        }
        if(isLimitMember(clanImpl)) {
            MessageManager.sendMessageFormated(player, MessageManager.LIMIT_MEMBERS_REACHED, MessageType.CHAT);
            return;
        }
        joinClanCheckEvent(player, clanImpl);
    }

    public void forceJoin(Player admin, User userImpl, Clan clanImpl) {
        Player player = Bukkit.getPlayer(userImpl.getUuid());

        if(player == null) {
            throw new NullPointerException("nuuloo");
        }

        if(userImpl.hasClan()) {
            MessageManager.sendMessageFormated(admin, MessageManager.PLAYER_BELONGS_TO_ANOTHER_CLAN, MessageType.CHAT);
            return;
        }
        joinClanCheckEvent(player, clanImpl);
    }

    private void joinClanCheckEvent(Player player, Clan clanImpl) {
        User userImpl = ClanAndUserDataService.getCachedUser(player.getUniqueId());
        JoinClanEvent event = new JoinClanEvent(clanImpl, player);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            userImpl.setClan(clanImpl);
            clanImpl.joinUser(userImpl);
            ClanAndUserDataService.updateClan(clanImpl);
            ClanAndUserDataService.updateUser(userImpl);

            MessageManager.sendMessageFormated(player, MessageManager.CLAN_INVITE_ACCEPTED, MessageType.CHAT);
        }
    }
    public void deleteClan(Player owner)
    {
        User userImpl = ClanAndUserDataService.getCachedUser(owner.getUniqueId());
        Clan clanImpl = ClanAndUserDataService.getCachedClan(userImpl.getClanTag());
        isOwner(clanImpl, userImpl);
        handleDeleteClan(clanImpl, owner);
    }

    public void deleteClanByAdmin(Player admin, ClanImpl clanImpl) {
        boolean isDeleted = handleDeleteClan(clanImpl, null);
        if(isDeleted) {
            MessageManager.sendMessageFormated(admin, MessageManager.CLAN_DELETED.replace("{clan}", clanImpl.getTag()), MessageType.CHAT);
        }
    }

    private boolean handleDeleteClan(Clan clanImpl, Player player) {
        if(player==null) return deleteClan(clanImpl, null);
        DeleteClanEvent event = new DeleteClanEvent(player, clanImpl);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            if (clanImpl ==null) {
                plugin.getLogger().info("Clan is null");
                return false;
            }
            if(clanImpl.getHideoutId() != null) {
                GuildHideOutController.resetOrCreateHideOut(clanImpl.getHideoutId());
            }
            return deleteClan(clanImpl, player);
        }
        return false;
    }

    private boolean deleteClan(Clan clanImpl, Player player) {
        String tag = clanImpl.getTag();
        for(UUID uuid : clanImpl.getMembers()) {
            User userImpl = ClanAndUserDataService.getCachedUser(uuid);
            userImpl.setClan(null);
            ClanAndUserDataService.updateUser(userImpl);
        }
        for(String alliance : clanImpl.getAlliances()) {
            Clan allianceClanImpl = clanAndUserDataService.getCachedClan(alliance);
            if(allianceClanImpl ==null){
                plugin.getLogger().info("Alliance clan not found: "+alliance);
                continue;
            }
            allianceClanImpl.removeAlliance(clanImpl);
            ClanAndUserDataService.updateClan(allianceClanImpl);
        }
        deleteClan(tag);
        ClanAndUserDataService.deleteClan(clanImpl);
        topRankScheduler.removeClan(clanImpl);
        if(player!=null) MessageManager.broadcastMessageFormated(MessageManager.CLAN_DELETED.replace("{clan}", tag), MessageType.CHAT);
        return true;
    }

    public void createClan(Player player, String tag) {
        if(hasClan(player)) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_ALREADY_BELONG_TO_CLAN, MessageType.CHAT);
            return;
        }
        if(tag.length() < 3 || tag.length() > 5) {
            MessageManager.sendMessageFormated(player, MessageManager.TAG_LENGTH, MessageType.CHAT);
            return;
        }
        if(isTagClaimed(tag.toUpperCase())) {
            MessageManager.sendMessageFormated(player, MessageManager.TAG_ALREADY_EXISTS, MessageType.CHAT);
            return;
        }
        User userImpl = ClanAndUserDataService.getCachedUser(player.getUniqueId());
        if(userImpl ==null)
            return;

        boolean enablePayment = Config.paymentEnabled;
        if(enablePayment) {
            boolean status = checkPayments(player);
            if(!status)
                return;
        }
        CreateClanEvent event = new CreateClanEvent(player, tag);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            Clan clanImpl = new ClanImpl(tag.toUpperCase(), userImpl, false);

            ClanAndUserDataService.updateClan(clanImpl);
            userImpl.setClan(clanImpl);

            ClanAndUserDataService.updateUser(userImpl);

            topRankScheduler.addClan(clanImpl);
            MessageManager.sendMessageFormated(player, MessageManager.CLAN_CREATED.replace("{clan}", tag), MessageType.CHAT);
        }
    }

    private enum CostType {
        VAULT,
        ITEM
    }

    private static final CostType costType = CostType.VAULT;

    private boolean checkPayments(Player player) {
//        return true;
        if(costType == CostType.VAULT) {
            Economy economy = AdvancedGuilds.getEconomy();
            if(!economy.has(player, Config.costCreate))
            {
                MessageManager.sendMessageFormated(player, MessageManager.NO_MONEY.replace("{cost}", String.valueOf(Config.costCreate)), MessageType.CHAT);
                return false;
            }
            economy.withdrawPlayer(player, Config.costCreate);
            return true;
        }
//        else {
//            int amount = ItemUtil.calcItemAmount(player, Config.itemCost);
//            int needAmount = (int) Config.costCreate;
//            if(amount<needAmount)
//            {
//                MessageManager.sendMessageFormated(player, MessageManager.NO_ITEMS.replace("{cost}", String.valueOf(Config.costCreate)), MessageType.CHAT);
//                return false;
//            }
//            ItemUtil.removeItems(player, Config.itemCost, needAmount);
//            return true;
//        }
        return false;
    }

    // kick user from clan by admin
    public void forceKickUser(Player sender, User userImpl) {
        if(!userImpl.hasClan()) {
            MessageManager.sendMessageFormated(sender, MessageManager.PLAYER_DOES_NOT_BELONG_TO_ANY_CLAN, MessageType.CHAT);
            return;
        }
        Clan clanImpl = ClanAndUserDataService.getCachedClan(userImpl.getClanTag());
        handleKickUser(null, userImpl, clanImpl);
    }
    private Optional<UUID> getPlayerUUIDByNickname(String nickname) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(nickname);
        if(offlinePlayer!=null) {
            return Optional.of(offlinePlayer.getUniqueId());
        }
        Player player = Bukkit.getPlayer(nickname);
        if(player==null) {
            return Optional.empty();
        }
        return Optional.of(player.getUniqueId());
    }

    public void kickUser(Player player, String nickname) {
        User deputyOwner = ClanAndUserDataService.getCachedUser(player.getUniqueId());
        Clan clanImpl = ClanAndUserDataService.getCachedClan(deputyOwner.getClanTag());
        Optional<UUID> optionalUUID = getPlayerUUIDByNickname(nickname);
        if(optionalUUID.isEmpty()) {
            MessageManager.sendMessageFormated(player, MessageManager.PLAYER_NOT_ONLINE, MessageType.CHAT);
            return;
        }
        UUID targetUUID = optionalUUID.get();
        if(!clanImpl.isMember(targetUUID)) {
            MessageManager.sendMessageFormated(player, MessageManager.PLAYER_DOES_NOT_BELONG_TO_YOUR_CLAN, MessageType.CHAT);
            return;
        }
        if(isSame(player, targetUUID))
        {
            MessageManager.sendMessageFormated(player, MessageManager.CANNOT_KICK_YOURSELF, MessageType.CHAT);
            return;
        }
        if(isOwner(clanImpl, targetUUID)) {
            MessageManager.sendMessageFormated(player, MessageManager.CANNOT_KICK_OWNER, MessageType.CHAT);
            return;
        }
        User kUserImpl = ClanAndUserDataService.getCachedUser(targetUUID);
        handleKickUser(player, kUserImpl, clanImpl);
    }


    private void handleKickUser(Player player, User kickedUserImpl, Clan  clanImpl) {
        PlayerKickClanEvent event = new PlayerKickClanEvent(clanImpl, kickedUserImpl.getUuid());
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            clanImpl.removeMember(kickedUserImpl);
            kickedUserImpl.setClan(null);
            Player kickedPlayer = Bukkit.getPlayer(kickedUserImpl.getUuid());
            if(kickedPlayer!=null) MessageManager.sendMessageFormated(kickedPlayer, MessageManager.KICKED_FROM_CLAN, MessageType.CHAT);
            ClanAndUserDataService.updateClan(clanImpl);
            ClanAndUserDataService.updateUser(kickedUserImpl);
            if(kickedPlayer!=null) MessageManager.sendMessageFormated(player, MessageManager.SUCCESSFULLY_KICKED_PLAYER.replace("{player}", kickedPlayer.getName()), MessageType.CHAT);
        }
    }

    private boolean isSame(Player player, UUID targetUUID) {
        return player.getUniqueId().equals(targetUUID);
    }

    public void leaveClan(Player player) {
        User userImpl = ClanAndUserDataService.getCachedUser(player.getUniqueId());
        Clan clanImpl = ClanAndUserDataService.getCachedClan(userImpl.getClanTag());
        if(isOwner(clanImpl, userImpl)) {
            MessageManager.sendMessageFormated(player, MessageManager.CANNOT_LEAVE_WHILE_OWNER, MessageType.CHAT);
            return;
        }
        LeaveClanEvent event = new LeaveClanEvent(player, clanImpl);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            clanImpl.removeMember(userImpl);
            userImpl.setClan(null);
            ClanAndUserDataService.updateClan(clanImpl);
            ClanAndUserDataService.updateUser(userImpl);
            MessageManager.sendMessageFormated(player, MessageManager.LEAVE_CLAN, MessageType.CHAT);
        }
    }
    private boolean isMemberInYourClan(Clan clanImpl, User userImpl) {
        return clanImpl.getMembers().contains(userImpl.getUuid());
    }

    private boolean isOwner(Clan clanImpl, User userImpl) {
        if(clanImpl == null) return false;
        return clanImpl.getOwnerUUID().equals(userImpl.getUuid());
    }
    private boolean isOwner(Clan clanImpl, UUID user) {
        if(clanImpl == null) return false;
        return clanImpl.getOwnerUUID().equals(user);
    }
    private boolean isTagClaimed(String tag) {
        return ClanAndUserDataService.getCachedClan(tag) != null;
    }

    public void alliance(Player player, Clan allianceClanImpl) {
        User deputyOwner = ClanAndUserDataService.getCachedUser(player.getUniqueId());
        if(!isDeputyOwner(ClanAndUserDataService.getCachedClan(deputyOwner.getClanTag()), player) && !isOwner(ClanAndUserDataService.getCachedClan(deputyOwner.getClanTag()), deputyOwner))
        {
            MessageManager.sendMessageFormated(player, MessageManager.NOT_DEPUTY, MessageType.CHAT);
            return;
        }
        Clan clanImpl = ClanAndUserDataService.getCachedClan(deputyOwner.getClanTag());

        if(isMemberInYourClan(allianceClanImpl, deputyOwner)) {
            MessageManager.sendMessageFormated(player, MessageManager.CANNOT_ALLIANCE_YOUR_OWN_CLAN, MessageType.CHAT);
            return;
        }
        if(clanImpl.isAlliance(allianceClanImpl.getTag())) {
            DisbandAllianceEvent event = new DisbandAllianceEvent(clanImpl, allianceClanImpl);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                clanImpl.removeAlliance(allianceClanImpl);
                allianceClanImpl.removeAlliance(clanImpl);
                ClanAndUserDataService.updateClan(clanImpl);
                ClanAndUserDataService.updateClan(allianceClanImpl);
                MessageManager.sendMessageFormated(player, MessageManager.ALLIANCE_DISBANDED, MessageType.CHAT);
            }
            return;
        }
        if(isLimitAlliance(clanImpl)) {
            MessageManager.sendMessageFormated(player, MessageManager.LIMIT_ALLIANCES_REACHED, MessageType.CHAT);
            return;
        }
        if(allianceClanImpl.isSuggestAlliance(clanImpl)) {
            CreateAllianceEvent event = new CreateAllianceEvent(clanImpl, allianceClanImpl);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                allianceClanImpl.removeSuggestAlliance(clanImpl);
                clanImpl.addAlliance(allianceClanImpl);
                allianceClanImpl.addAlliance(clanImpl);
                ClanAndUserDataService.updateClan(clanImpl);
                ClanAndUserDataService.updateClan(allianceClanImpl);
                MessageManager.sendMessageFormated(player, MessageManager.ALLIANCE_CREATED, MessageType.CHAT);
            }
            return;
        }
        if(!clanImpl.isSuggestAlliance(allianceClanImpl)) {
            clanImpl.inviteAlliance(allianceClanImpl);
            MessageManager.sendMessageFormated(player, MessageManager.INVITED_CLAN_TO_ALLIANCE.replace("{clan}", allianceClanImpl.getTag()), MessageType.CHAT);
            Player allianceOwner = Bukkit.getPlayer(allianceClanImpl.getOwnerUUID());
            if (allianceOwner != null) MessageManager.sendMessageFormated(allianceOwner, MessageManager.YOUR_CLAN_WAS_INVITED_TO_ALLIANCE.replace("{clan}", clanImpl.getTag()), MessageType.CHAT);
        } else {
            clanImpl.removeInviteAlliance(allianceClanImpl);
            MessageManager.sendMessageFormated(player, MessageManager.CANCELED_INVITE_FROM_ALLIANCE.replace("{clan}", allianceClanImpl.getTag()), MessageType.CHAT);
        }
    }

    public void removeDeputy(Player owner) {
        User userImpl = ClanAndUserDataService.getCachedUser(owner.getUniqueId());
        Clan clanImpl = ClanAndUserDataService.getCachedClan(userImpl.getClanTag());
        Player player = owner.getPlayer();
        if(deputyIsEmpty(clanImpl)) {
            MessageManager.sendMessageFormated(player, MessageManager.DEPUTY_EMPTY, MessageType.CHAT);
            return;
        }
        DeleteDeputyEvent event = new DeleteDeputyEvent(clanImpl, ClanAndUserDataService.getCachedUser(clanImpl.getDeputyOwnerUUID()));
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            clanImpl.setDeputyOwnerUUID(null);
            ClanAndUserDataService.updateClan(clanImpl);
            MessageManager.sendMessageFormated(player, MessageManager.DEPUTY_REMOVED, MessageType.CHAT);
        }
    }

    private boolean deputyIsEmpty(Clan clanImpl) {
         return clanImpl.getDeputyOwnerUUID()==null;
    }

    public void setDeputy(Player owner, OfflinePlayer target) {
        User userImpl = ClanAndUserDataService.getCachedUser(owner.getUniqueId());
        User targetUserImpl = ClanAndUserDataService.getCachedUser(target.getUniqueId());

        if(!isDeputyOwner(ClanAndUserDataService.getCachedClan(userImpl.getClanTag()), owner) && !isOwner(ClanAndUserDataService.getCachedClan(userImpl.getClanTag()), userImpl)){
            MessageManager.sendMessageFormated(owner, MessageManager.NOT_DEPUTY, MessageType.CHAT);
            return;
        }

        Clan clanImpl = ClanAndUserDataService.getCachedClan(userImpl.getClanTag());
        if(!isMemberInYourClan(clanImpl, targetUserImpl)) {
            MessageManager.sendMessageFormated(owner, MessageManager.PLAYER_DOES_NOT_BELONG_TO_YOUR_CLAN, MessageType.CHAT);
            return;
        }

        if(clanImpl.isDeputy(targetUserImpl)) {
            MessageManager.sendMessageFormated(owner, MessageManager.PLAYER_ALREADY_DEPUTY.replace("{player}", target.getName()), MessageType.CHAT);
            return;
        }

        DeputyChangeClanEvent event = new DeputyChangeClanEvent(ClanAndUserDataService.getCachedClan(userImpl.getClanTag()), owner, target);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            clanImpl.setDeputyOwnerUUID(targetUserImpl);
            ClanAndUserDataService.updateClan(clanImpl);
            MessageManager.sendMessageFormated(owner, MessageManager.CLAN_DEPUTY_SET.replace("{player}", target.getName()).replace("{clan}", clanImpl.getTag()), MessageType.CHAT);
        }
    }

    public boolean doesClanFulfillThreshold(Clan clanImpl) {
        return clanImpl.getMembers().size() >= Config.minPlayersForRanking;
    }
    private boolean isLimitAlliance(Clan clanImpl) {
        return clanImpl.getAlliances().size() >= Config.allianceLimit;
    }

    public Clan getClan(String tag) {
        return ClanAndUserDataService.getCachedClan(tag);
    }

    public void deleteClan(String tag) {
        ClanAndUserDataService.deleteClan(ClanAndUserDataService.getCachedClan(tag));
    }


    public void loadAllClans() {
        ClanAndUserDataService.loadAllClans();
    }

}
