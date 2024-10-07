package me.xxgradzix.advancedclans.data.database.controllers.clansCOre;

import lombok.Setter;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.config.Config;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.data.database.services.hideout.GuildHideOutDataManager;
import me.xxgradzix.advancedclans.events.*;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import me.xxgradzix.advancedclans.scheduler.TopRankScheduler;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import me.xxgradzix.advancedclans.utils.ConsoleColor;
import me.xxgradzix.advancedclans.utils.ItemUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class ClanController {
    private final AdvancedGuilds plugin;
    private final ClanAndUserDataManager clanAndUserDataManager;

    @Setter
    private TopRankScheduler topRankScheduler;


    public ClanController(AdvancedGuilds plugin, ClanAndUserDataManager clanAndUserDataManager)
    {
        this.plugin = plugin;
        this.clanAndUserDataManager = clanAndUserDataManager;
    }

    public void setOwner(Player executor, Player target) {

        User owner = ClanAndUserDataManager.getCachedUser(executor.getUniqueId());
        Clan ownerClan = ClanAndUserDataManager.getCachedClan(owner.getClanTag());

        User targerUser = ClanAndUserDataManager.getCachedUser(target.getUniqueId());

        boolean isExecutorOwner = isOwner(ownerClan, owner);

        if(!isExecutorOwner) {
            MessageManager.sendMessageFormated(executor, MessageManager.NOT_OWNER, MessageType.CHAT);
            return;
        }

        if(isSame(executor, targerUser.getUuid()))
        {
            MessageManager.sendMessageFormated(executor, MessageManager.ALREADY_OWNER, MessageType.CHAT);
            return;
        }
        if(!isMemberInYourClan(ownerClan, targerUser))
        {
            MessageManager.sendMessageFormated(executor, MessageManager.PLAYER_DOES_NOT_BELONG_TO_YOUR_CLAN, MessageType.CHAT);
            return;
        }

        boolean success = handleSetOwner(ownerClan, targerUser);
        if(success) {
            MessageManager.sendMessageFormated(executor, MessageManager.NEW_OWNER_SET.replace("{player}", target.getName()), MessageType.CHAT);
        }
    }

    private boolean handleSetOwner(Clan clan, User newOwnerUUID) {
        ChangeOwnerClanEvent event = new ChangeOwnerClanEvent(clan, ClanAndUserDataManager.getCachedUser(clan.getOwnerUUID()), newOwnerUUID);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            clan.setOwnerUUID(newOwnerUUID.getUuid());

            ClanAndUserDataManager.updateClan(clan);
            return true;
        }
        return false;
    }

    public void changePvpStatus(Player deputyOwner) {
        User user = ClanAndUserDataManager.getCachedUser(deputyOwner.getUniqueId());

        Clan clan = ClanAndUserDataManager.getCachedClan(user.getClanTag());

        if (!isDeputyOwner(clan, deputyOwner) && !isOwner(clan, user)) {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.NOT_DEPUTY, MessageType.CHAT);
            return;
        }
        clan.togglePvp();
        if (clan.isPvpEnable()) {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.PVP_ENABLED, MessageType.CHAT);
        } else {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.PVP_DISABLED, MessageType.CHAT);
        }
        ClanAndUserDataManager.updateClan(clan);
    }

    public void inviteUser(Player deputyOwner, Player target) {
        User user = ClanAndUserDataManager.getCachedUser(deputyOwner.getUniqueId());
        Clan clan = ClanAndUserDataManager.getCachedClan(user.getClanTag());
        User targetUser = ClanAndUserDataManager.getCachedUser(target.getUniqueId());

        if(!isDeputyOwner(clan, deputyOwner) && !isOwner(clan, user)) {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.NOT_DEPUTY, MessageType.CHAT);
            return;
        }

        if(!target.isOnline()) {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.PLAYER_NOT_ONLINE, MessageType.CHAT);
            return;
        }

        if(isLimitMember(clan)) {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.LIMIT_MEMBERS_REACHED, MessageType.CHAT);
            return;
        }

        if(hasClan(target)) {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.PLAYER_BELONGS_TO_ANOTHER_CLAN, MessageType.CHAT);
            return;
        }
        if(clan.hasInvite(targetUser)) {
            CancelInviteClanEvent event = new CancelInviteClanEvent(clan, target);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                clan.cancelInvite(targetUser);
                MessageManager.sendMessageFormated(deputyOwner, MessageManager.CLAN_INVITE_CANCELED_SENDER.replace("{player}", target.getName()), MessageType.CHAT);
                MessageManager.sendMessageFormated(target, MessageManager.CLAN_INVITE_CANCELED_RECIEVER.replace("{clan}", clan.getTag()), MessageType.CHAT);
            }
            return;
        }

        InviteClanEvent event = new InviteClanEvent(clan, target);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            clan.invite(targetUser);
            ClanAndUserDataManager.updateClan(clan);
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.CLAN_INVITED_SENDER.replace("{player}", target.getName()), MessageType.CHAT);
            MessageManager.sendMessageFormated(target, MessageManager.CLAN_INVITE_RECEIVED.replace("{clan}", clan.getTag()), MessageType.CHAT);
        }
    }

    private boolean hasClan(Player target) {
        User user = ClanAndUserDataManager.getCachedUser(target.getUniqueId());
        return user.hasClan();
    }

    private boolean isDeputyOwner(Clan clan, Player player) {
        if(clan==null)
            return false;

        if(clan.getDeputyOwnerUUID()==null)
            return false;
        return clan.getDeputyOwnerUUID().equals(player.getUniqueId());
    }

    private boolean isLimitMember(Clan clan) {
        return clan.getMembers().size()>= Config.maxPlayers;
    }

    public void infoClan(Player player, Clan clan) {
        OptionalInt clanRankIndexByTag = topRankScheduler.getClanRankIndexByTag(clan.getTag());
        int index = 9999;
        if(clanRankIndexByTag.isPresent())
            index = clanRankIndexByTag.getAsInt()+1;
        String infoMessage = MessageManager.CLAN_INFO;

        infoMessage = infoMessage.replace("{tag}", clan.getTag())
                        .replace("{owner}", getPlayerName(clan.getOwnerUUID()))
                        .replace("{deputy-owner}", getPlayerName(clan.getDeputyOwnerUUID()))
                        .replace("{points}", getAveragePoint(clan))
                        .replace("{members-online}", String.valueOf(countOnlineMember(clan)))
                        .replace("{members-size}", String.valueOf(clan.getMembers().size()))
                        .replace("{rank}", String.valueOf(index))
                        .replace("{members}", getClanMembers(clan));

        MessageManager.sendMessageFormated(player, infoMessage, MessageType.CHAT);
    }

    private String getClanMembers(Clan clan) {
        List<UUID> members = clan.getMembers();
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

    public int countOnlineMember(Clan clan) {
        int online = 0;
        for (UUID uuid : clan.getMembers()) {
            Player player = Bukkit.getPlayer(uuid);
            if(player!=null)
                online++;
        }
        return online;
    }

    public String getAveragePoint(Player player)
    {
        User user = ClanAndUserDataManager.getCachedUser(player.getUniqueId());
        if(user==null || !user.hasClan())
            return Config.noClan;

        Clan clan = ClanAndUserDataManager.getCachedClan(user.getClanTag());
        if(!doesClanFulfillThreshold(clan)) {
            return ColorFixer.addColors(Config.noEnoughPlayerForRankingInClan);
        }
        return getAveragePoint(clan);
    }

    public String getAveragePoint(Clan clan)
    {
        List<UUID> members = clan.getMembers();
        int sum = 0;
        int count = 0;
        for (UUID uuid : members) {
            User tempUser = ClanAndUserDataManager.getCachedUser(uuid);
            if(tempUser==null) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                plugin.getLogger().info(ConsoleColor.RED+"Bląd - Gracz o nazwie " + player.getName() + "  nalezy do klanu "+clan.getTag() + " ale nie znajduje go jako obiekt User");
                continue;
            }
            sum += tempUser.getPoints();
            count++;
        }
        double average = (double) sum / count;
        return String.valueOf((int) average);
    }

    private String getPlayerName(UUID uuid)
    {
        if(uuid==null)
            return Config.nonePlayer;

        return Bukkit.getOfflinePlayer(uuid).getName();
    }
    private String getPlayerName(User user) {
        if(user == null || user.getUuid()==null)
            return Config.nonePlayer;

        return Bukkit.getOfflinePlayer(user.getUuid()).getName();
    }
    public void joinClan(Player player, Clan clan)
    {
        User user = ClanAndUserDataManager.getCachedUser(player.getUniqueId());
        if(user.hasClan()) {
            MessageManager.sendMessageFormated(player, MessageManager.PLAYER_BELONGS_TO_ANOTHER_CLAN, MessageType.CHAT);
            return;
        }
        if(!clan.hasInvite(user)) {
            MessageManager.sendMessageFormated(player, MessageManager.NOT_RECEIVED_INVITE.replace("{clan}", clan.getTag()), MessageType.CHAT);
            return;
        }
        if(isLimitMember(clan)) {
            MessageManager.sendMessageFormated(player, MessageManager.LIMIT_MEMBERS_REACHED, MessageType.CHAT);
            return;
        }
        joinClanCheckEvent(player, clan);
    }

    public void forceJoin(Player admin, User user, Clan clan) {
        Player player = Bukkit.getPlayer(user.getUuid());

        if(player == null) {
            throw new NullPointerException("nuuloo");
        }

        if(user.hasClan()) {
            MessageManager.sendMessageFormated(admin, MessageManager.PLAYER_BELONGS_TO_ANOTHER_CLAN, MessageType.CHAT);
            return;
        }
        joinClanCheckEvent(player, clan);
    }

    private void joinClanCheckEvent(Player player, Clan clan) {
        User user = ClanAndUserDataManager.getCachedUser(player.getUniqueId());
        JoinClanEvent event = new JoinClanEvent(clan, player);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            user.setClan(clan);
            clan.joinUser(user);
            ClanAndUserDataManager.updateClan(clan);
            ClanAndUserDataManager.updateUser(user);
            MessageManager.sendMessageFormated(player, MessageManager.CLAN_INVITE_ACCEPTED, MessageType.CHAT);
        }
    }
    public void deleteClan(Player owner)
    {
        User user = ClanAndUserDataManager.getCachedUser(owner.getUniqueId());
        Clan clan = ClanAndUserDataManager.getCachedClan(user.getClanTag());
        isOwner(clan, user);
        handleDeleteClan(clan, owner);
    }

    public void deleteClanByAdmin(Player admin, Clan clan) {
        boolean isDeleted = handleDeleteClan(clan, null);
        if(isDeleted) {
            MessageManager.sendMessageFormated(admin, MessageManager.CLAN_DELETED.replace("{clan}", clan.getTag()), MessageType.CHAT);
        }
    }
    private boolean handleDeleteClan(Clan clan, Player player) {
        if(player==null) return deleteClan(clan, null);
        DeleteClanEvent event = new DeleteClanEvent(player, clan);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            if(clan.getHideoutId() != null) {
                GuildHideOutDataManager.resetOrCreateHideOut(clan.getHideoutId());
            }
            return deleteClan(clan, player);
        }
        return false;
    }

    private boolean deleteClan(Clan clan, Player player) {
        String tag = clan.getTag();
        for(UUID uuid : clan.getMembers()) {
            User user = ClanAndUserDataManager.getCachedUser(uuid);
            user.setClan(null);
            ClanAndUserDataManager.updateUser(user);
        }
        for(String alliance : clan.getAlliances()) {
            Clan allianceClan = clanAndUserDataManager.getCachedClan(alliance);
            if(allianceClan==null){
                plugin.getLogger().info("Alliance clan not found: "+alliance);
                continue;
            }
            allianceClan.removeAlliance(clan);
            ClanAndUserDataManager.updateClan(allianceClan);
        }
        deleteClan(tag);
        ClanAndUserDataManager.deleteClan(clan);
        topRankScheduler.removeClan(clan);
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
        User user = ClanAndUserDataManager.getCachedUser(player.getUniqueId());
        if(user==null)
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
            Clan clan = new Clan(tag.toUpperCase(), user, false);

            ClanAndUserDataManager.updateClan(clan);
            user.setClan(clan);

            ClanAndUserDataManager.updateUser(user);

            topRankScheduler.addClan(clan);
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
    public void forceKickUser(Player sender, User user) {
        if(!user.hasClan()) {
            MessageManager.sendMessageFormated(sender, MessageManager.PLAYER_DOES_NOT_BELONG_TO_ANY_CLAN, MessageType.CHAT);
            return;
        }
        Clan clan = ClanAndUserDataManager.getCachedClan(user.getClanTag());
        handleKickUser(null, user, clan);
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
        User deputyOwner = ClanAndUserDataManager.getCachedUser(player.getUniqueId());
        Clan clan = ClanAndUserDataManager.getCachedClan(deputyOwner.getClanTag());
        Optional<UUID> optionalUUID = getPlayerUUIDByNickname(nickname);
        if(optionalUUID.isEmpty()) {
            MessageManager.sendMessageFormated(player, MessageManager.PLAYER_NOT_ONLINE, MessageType.CHAT);
            return;
        }
        UUID targetUUID = optionalUUID.get();
        if(!clan.isMember(targetUUID)) {
            MessageManager.sendMessageFormated(player, MessageManager.PLAYER_DOES_NOT_BELONG_TO_YOUR_CLAN, MessageType.CHAT);
            return;
        }
        if(isSame(player, targetUUID))
        {
            MessageManager.sendMessageFormated(player, MessageManager.CANNOT_KICK_YOURSELF, MessageType.CHAT);
            return;
        }
        if(isOwner(clan, targetUUID)) {
            MessageManager.sendMessageFormated(player, MessageManager.CANNOT_KICK_OWNER, MessageType.CHAT);
            return;
        }
        User kUser = ClanAndUserDataManager.getCachedUser(targetUUID);
        handleKickUser(player, kUser, clan);
    }


    private void handleKickUser(Player player, User kickedUser, Clan clan) {
        PlayerKickClanEvent event = new PlayerKickClanEvent(clan, kickedUser.getUuid());
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            clan.removeMember(kickedUser);
            kickedUser.setClan(null);
            Player kickedPlayer = Bukkit.getPlayer(kickedUser.getUuid());
            if(kickedPlayer!=null) MessageManager.sendMessageFormated(kickedPlayer, MessageManager.KICKED_FROM_CLAN, MessageType.CHAT);
            ClanAndUserDataManager.updateClan(clan);
            ClanAndUserDataManager.updateUser(kickedUser);
            if(player!=null) MessageManager.sendMessageFormated(player, MessageManager.SUCCESSFULLY_KICKED_PLAYER.replace("{player}", player.getName()), MessageType.CHAT);
        }
    }

    private boolean isSame(Player player, UUID targetUUID) {
        return player.getUniqueId().equals(targetUUID);
    }

    public void leaveClan(Player player) {
        User user = ClanAndUserDataManager.getCachedUser(player.getUniqueId());
        Clan clan = ClanAndUserDataManager.getCachedClan(user.getClanTag());
        if(isOwner(clan, user)) {
            MessageManager.sendMessageFormated(player, MessageManager.CANNOT_LEAVE_WHILE_OWNER, MessageType.CHAT);
            return;
        }
        LeaveClanEvent event = new LeaveClanEvent(player, clan);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            clan.removeMember(user);
            user.setClan(null);
            ClanAndUserDataManager.updateClan(clan);
            ClanAndUserDataManager.updateUser(user);
            MessageManager.sendMessageFormated(player, MessageManager.LEAVE_CLAN, MessageType.CHAT);
        }
    }
    private boolean isMemberInYourClan(Clan clan, User user) {
        return clan.getMembers().contains(user.getUuid());
    }

    private boolean isOwner(Clan clan, User user) {
        if(clan == null) return false;
        return clan.getOwnerUUID().equals(user.getUuid());
    }
    private boolean isOwner(Clan clan, UUID user) {
        if(clan == null) return false;
        return clan.getOwnerUUID().equals(user);
    }
    private boolean isTagClaimed(String tag) {
        return ClanAndUserDataManager.getCachedClan(tag) != null;
    }

    public void alliance(Player player, Clan allianceClan) {
        User deputyOwner = ClanAndUserDataManager.getCachedUser(player.getUniqueId());
        if(!isDeputyOwner(ClanAndUserDataManager.getCachedClan(deputyOwner.getClanTag()), player) && !isOwner(ClanAndUserDataManager.getCachedClan(deputyOwner.getClanTag()), deputyOwner))
        {
            MessageManager.sendMessageFormated(player, MessageManager.NOT_DEPUTY, MessageType.CHAT);
            return;
        }
        Clan clan = ClanAndUserDataManager.getCachedClan(deputyOwner.getClanTag());

        if(isMemberInYourClan(allianceClan, deputyOwner)) {
            MessageManager.sendMessageFormated(player, MessageManager.CANNOT_ALLIANCE_YOUR_OWN_CLAN, MessageType.CHAT);
            return;
        }
        if(clan.isAlliance(allianceClan.getTag())) {
            DisbandAllianceEvent event = new DisbandAllianceEvent(clan, allianceClan);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                clan.removeAlliance(allianceClan);
                allianceClan.removeAlliance(clan);
                ClanAndUserDataManager.updateClan(clan);
                ClanAndUserDataManager.updateClan(allianceClan);
                MessageManager.sendMessageFormated(player, MessageManager.ALLIANCE_DISBANDED, MessageType.CHAT);
            }
            return;
        }
        if(isLimitAlliance(clan)) {
            MessageManager.sendMessageFormated(player, MessageManager.LIMIT_ALLIANCES_REACHED, MessageType.CHAT);
            return;
        }
        if(allianceClan.isSuggestAlliance(clan)) {
            CreateAllianceEvent event = new CreateAllianceEvent(clan, allianceClan);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                allianceClan.removeSuggestAlliance(clan);
                clan.addAlliance(allianceClan);
                allianceClan.addAlliance(clan);
                ClanAndUserDataManager.updateClan(clan);
                ClanAndUserDataManager.updateClan(allianceClan);
                MessageManager.sendMessageFormated(player, MessageManager.ALLIANCE_CREATED, MessageType.CHAT);
            }
            return;
        }
        if(!clan.isSuggestAlliance(allianceClan)) {
            clan.inviteAlliance(allianceClan);
            MessageManager.sendMessageFormated(player, MessageManager.INVITED_CLAN_TO_ALLIANCE.replace("{clan}", allianceClan.getTag()), MessageType.CHAT);
            Player allianceOwner = Bukkit.getPlayer(allianceClan.getOwnerUUID());
            if (allianceOwner != null) MessageManager.sendMessageFormated(allianceOwner, MessageManager.YOUR_CLAN_WAS_INVITED_TO_ALLIANCE.replace("{clan}", clan.getTag()), MessageType.CHAT);
        } else {
            clan.removeInviteAlliance(allianceClan);
            MessageManager.sendMessageFormated(player, MessageManager.CANCELED_INVITE_FROM_ALLIANCE.replace("{clan}", allianceClan.getTag()), MessageType.CHAT);
        }
    }

    public void removeDeputy(Player owner) {
        User user = ClanAndUserDataManager.getCachedUser(owner.getUniqueId());
        Clan clan = ClanAndUserDataManager.getCachedClan(user.getClanTag());
        Player player = owner.getPlayer();
        if(deputyIsEmpty(clan)) {
            MessageManager.sendMessageFormated(player, MessageManager.DEPUTY_EMPTY, MessageType.CHAT);
            return;
        }
        DeleteDeputyEvent event = new DeleteDeputyEvent(clan, ClanAndUserDataManager.getCachedUser(clan.getDeputyOwnerUUID()));
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            clan.setDeputyOwnerUUID(null);
            ClanAndUserDataManager.updateClan(clan);
            MessageManager.sendMessageFormated(player, MessageManager.DEPUTY_REMOVED, MessageType.CHAT);
        }
    }

    private boolean deputyIsEmpty(Clan clan) {
         return clan.getDeputyOwnerUUID()==null;
    }

    public void setDeputy(Player owner, Player target) {
        User user = ClanAndUserDataManager.getCachedUser(owner.getUniqueId());
        User targetUser = ClanAndUserDataManager.getCachedUser(target.getUniqueId());

        if(!isDeputyOwner(ClanAndUserDataManager.getCachedClan(user.getClanTag()), owner) && !isOwner(ClanAndUserDataManager.getCachedClan(user.getClanTag()), user)){
            MessageManager.sendMessageFormated(owner, MessageManager.NOT_DEPUTY, MessageType.CHAT);
            return;
        }

        Clan clan = ClanAndUserDataManager.getCachedClan(user.getClanTag());
        if(!isMemberInYourClan(clan, targetUser)) {
            MessageManager.sendMessageFormated(owner, MessageManager.PLAYER_DOES_NOT_BELONG_TO_YOUR_CLAN, MessageType.CHAT);
            return;
        }

        if(clan.isDeputy(targetUser)) {
            MessageManager.sendMessageFormated(owner, MessageManager.PLAYER_ALREADY_DEPUTY.replace("{player}", target.getName()), MessageType.CHAT);
            return;
        }

        DeputyChangeClanEvent event = new DeputyChangeClanEvent(ClanAndUserDataManager.getCachedClan(user.getClanTag()), owner, target);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            clan.setDeputyOwnerUUID(targetUser);
            ClanAndUserDataManager.updateClan(clan);
            MessageManager.sendMessageFormated(owner, MessageManager.CLAN_DEPUTY_SET.replace("{player}", target.getName()).replace("{clan}", clan.getTag()), MessageType.CHAT);
        }
    }

    public boolean doesClanFulfillThreshold(Clan clan) {
        return clan.getMembers().size() >= Config.minPlayersForRanking;
    }
    private boolean isLimitAlliance(Clan clan) {
        return clan.getAlliances().size() >= Config.allianceLimit;
    }

    public Clan getClan(String tag) {
        return ClanAndUserDataManager.getCachedClan(tag);
    }

    public void deleteClan(String tag) {
        ClanAndUserDataManager.deleteClan(ClanAndUserDataManager.getCachedClan(tag));
    }


    public void loadAllClans() {
        ClanAndUserDataManager.loadAllClans();
    }

}