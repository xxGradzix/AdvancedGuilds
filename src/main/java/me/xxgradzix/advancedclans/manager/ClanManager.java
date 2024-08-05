package me.xxgradzix.advancedclans.manager;

import lombok.Setter;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.DataManager;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.events.*;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import me.xxgradzix.advancedclans.scheduler.TopRankScheduler;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import me.xxgradzix.advancedclans.utils.ConsoleColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class ClanManager {
    private final AdvancedGuilds plugin;
    private final DataManager dataManager;

    private final UserManager userManager;
    @Setter
    private TopRankScheduler topRankScheduler;

    private final HashMap<String, Clan> clansData = new HashMap<>();

    public ClanManager(AdvancedGuilds plugin, DataManager dataManager, UserManager userManager)
    {
        this.plugin = plugin;
        this.dataManager = dataManager;
        this.userManager = userManager;
    }

    public void setOwner(Player executor, Player target) {

        User owner = userManager.getUserData().get(executor.getUniqueId());
        Clan ownerClan = owner.getClan();

        User targerUser = userManager.getUserData().get(target.getUniqueId());

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

        // check event and set new owner
        boolean success = handleSetOwner(ownerClan, targerUser);
        if(success) {
            MessageManager.sendMessageFormated(executor, MessageManager.NEW_OWNER_SET.replace("{player}", target.getName()), MessageType.CHAT);
        }
    }

    private boolean handleSetOwner(Clan clan, User newOwnerUUID) {
        ChangeOwnerClanEvent event = new ChangeOwnerClanEvent(clan, clan.getOwnerUUID(), newOwnerUUID);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            clan.setOwner(newOwnerUUID);

            dataManager.updateClan(clan);
            return true;
        }
        return false;
    }

    public void changePvpStatus(Player deputyOwner) {
        User user = userManager.getUserData().get(deputyOwner.getUniqueId());
        Clan clan = user.getClan();

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
        dataManager.updateClan(clan);
    }


//    public void forceSetOwner(Player sender, String username) {
//        if(!(sender.isOp())) return;
//
//        Optional<UUID> uuidOptional = getPlayerUUIDByNickname(username);
//        if(uuidOptional.isEmpty()) {
//            MessageManager.sendMessageFormated(sender, MessageManager.PLAYER_NOT_ONLINE, MessageType.CHAT);
//            return;
//        }
//        UUID newOwnerUUID = uuidOptional.get();
//        User user = userManager.getUserData().get(newOwnerUUID);
//        if(!user.hasClan()) {
//            MessageUtil.sendMessage(sender, lang.langAdminUserNoClan);
//            return;
//        }
//        Clan clan = user.getClan();
//        // check event and set new owner
//        boolean success = handleSetOwner(clan, newOwnerUUID);
//        if(success)
//            MessageUtil.sendMessage(sender, lang.langadminSuccessfullySetOwner);
//    }

    public void inviteUser(Player deputyOwner, Player target) {
        User user = userManager.getUserData().get(deputyOwner.getUniqueId());
        Clan clan = user.getClan();
        User targetUser = userManager.getUserData().get(target.getUniqueId());

        if(!isDeputyOwner(clan, deputyOwner) && !isOwner(clan, user)) {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.NOT_DEPUTY, MessageType.CHAT);
            return;
        }

        if(!target.isOnline()) {

            MessageManager.sendMessageFormated(deputyOwner, MessageManager.PLAYER_NOT_ONLINE, MessageType.CHAT);
            return;
        }

        // limit clan
        if(isLimitMember(clan)) {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.LIMIT_MEMBERS_REACHED, MessageType.CHAT);
            return;
        }

        if(hasClan(target)) {
            MessageManager.sendMessageFormated(deputyOwner, MessageManager.PLAYER_BELONGS_TO_ANOTHER_CLAN, MessageType.CHAT);
            return;
        }
        if(clan.hasInvite(targetUser))
        {
            // cancel invite to clan
            CancelInviteClanEvent event = new CancelInviteClanEvent(clan, target);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                clan.cancelInvite(targetUser);
                MessageManager.sendMessageFormated(deputyOwner, MessageManager.CLAN_INVITE_CANCELED_SENDER.replace("{player}", target.getName()), MessageType.CHAT);
                MessageManager.sendMessageFormated(target, MessageManager.CLAN_INVITE_CANCELED_RECIEVER.replace("{clan}", clan.getTag()), MessageType.CHAT);
            }
            return;
        }

        // invite player
        InviteClanEvent event = new InviteClanEvent(clan, target);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            clan.invite(targetUser);

            MessageManager.sendMessageFormated(deputyOwner, MessageManager.CLAN_INVITED_SENDER.replace("{player}", target.getName()), MessageType.CHAT);
            MessageManager.sendMessageFormated(target, MessageManager.CLAN_INVITE_RECEIVED.replace("{clan}", clan.getTag()), MessageType.CHAT);
        }
    }

    private boolean hasClan(Player target) {
        User user = userManager.getUserData().get(target.getUniqueId());
        return user.hasClan();
    }

    private boolean isDeputyOwner(Clan clan, Player player) {
        if(clan==null)
            return false;
        return clan.getDeputyOwnerUUID().getUuid().equals(player.getUniqueId());
    }

    private boolean isLimitMember(Clan clan) {
        return clan.getMembers().size()>= 15;   // TODO COnfig get max members | was getMaxMember(clan);
    }


    // checking permission and count members
//    public int getMaxMember(Clan clan)
//    {
//        int ownerMax = getUserMaxMember(clan.getOwnerUUID());
//        int deputyOwnerMax = getUserMaxMember(clan.getDeputyOwnerUUID());
//        return Math.max(ownerMax, deputyOwnerMax);
//    }
//
//    private int getUserMaxMember(UUID uuid) {
//        if(uuid==null)
//            return 0;
//
//        Player player = Bukkit.getPlayer(uuid);
//        if(player!=null)
//        {
//            Map<String, Integer> permissionLimitMember = config.permissionLimitMember;
//            for(Map.Entry<String, Integer> permissionData : permissionLimitMember.entrySet())
//            {
//                String permission = permissionData.getKey();
//                int max = permissionData.getValue();
//                if(player.hasPermission(permission))
//                    return max;
//            }
//        }
//        return 0;
//    }


    public void infoClan(Player player, Clan clan) {

        OptionalInt clanRankIndexByTag = topRankScheduler.getClanRankIndexByTag(clan.getTag());
        int index = 9999;
        if(clanRankIndexByTag.isPresent())
            index = clanRankIndexByTag.getAsInt()+1;

        String infoMessage = MessageManager.CLAN_INFO;

        infoMessage = infoMessage.replace("{tag}", clan.getTag())
                        .replace("{owner}", getPlayerName(clan.getOwnerUUID().getUuid()))
                        .replace("{deputy-owner}", getPlayerName(clan.getDeputyOwnerUUID()))
                        .replace("{points}", getAveragePoint(clan))
                        .replace("{members-online}", String.valueOf(countOnlineMember(clan)))
                        .replace("{members-size}", String.valueOf(clan.getMembers().size()))
                        .replace("{rank}", String.valueOf(index))
                        .replace("{members}", getClanMembers(clan));

        MessageManager.sendMessageFormated(player, infoMessage, MessageType.CHAT);
    }

    private String getClanMembers(Clan clan) {
//        ForeignCollection<User> members = clan.getMembers();
        List<UUID> members = clan.getMembers();
        List<String> membersText = new ArrayList<>();

//        for(User user : members)
//        {
//            OfflinePlayer player = Bukkit.getOfflinePlayer(user.getUuid());
//            if(player.isOnline())
//                membersText.add(ChatColor.GREEN +player.getName()); // TODO add color of online player
//            else
//                membersText.add(ChatColor.RED+player.getName()); // TODO add color of offline player
//
//        }
        for(UUID uuid : members)
        {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
            if(player.isOnline())
                membersText.add(ChatColor.GREEN +player.getName()); // TODO add color of online player
            else
                membersText.add(ChatColor.RED+player.getName()); // TODO add color of offline player

        }
        return String.join(", ", membersText);
    }

    public int countOnlineMember(Clan clan) {
        int online = 0;
//        for (User user : clan.getMembers()) {
//            Player player = Bukkit.getPlayer(user.getUuid());
//            if(player!=null)
//                online++;
//        }
        for (UUID uuid : clan.getMembers()) {
            Player player = Bukkit.getPlayer(uuid);
            if(player!=null)
                online++;
        }
        return online;
    }

    public String getAveragePoint(Player player)
    {
        User user = userManager.getUserData().get(player.getUniqueId());
        if(user==null || !user.hasClan())
            return "Nie ma klanu"; // TODO none points or clan not found from config

        Clan clan = user.getClan();
        if(!doesClanFulfillThreshold(clan)) {
            return ColorFixer.addColors(" nie ma wystarczajaco czlonkow "); // TODO not enough player info
        }
        return getAveragePoint(clan);
    }

    public String getAveragePoint(Clan clan)
    {
//        ForeignCollection<User> members = clan.getMembers();
        List<UUID> members = clan.getMembers();
        int sum = 0;
        int count = 0;

//        for (User user : members) {
//            User tempUser = userManager.getUserData().get(user.getUuid());
//            if(tempUser==null) {
//                OfflinePlayer player = Bukkit.getOfflinePlayer(user.getUuid());
//                plugin.getLogger().info(ConsoleColor.RED+"Bląd - Gracz o nazwie " + player.getName() + "  nalezy do klanu "+clan.getTag() + " ale nie znajduje go jako obiekt User");
//                continue;
//            }
//            sum += tempUser.getPoints();
//            count++;
//        }
        for (UUID uuid : members) {
            User tempUser = userManager.getUserData().get(uuid);
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
            return " NIE MA "; // TODO none player

        return Bukkit.getOfflinePlayer(uuid).getName();
    }
    private String getPlayerName(User user) {
        if(user == null || user.getUuid()==null)
            return " NIE MA "; // TODO none player

        return Bukkit.getOfflinePlayer(user.getUuid()).getName();
    }
    public void joinClan(Player player, Clan clan)
    {
        User user = userManager.getUserData().get(player.getUniqueId());
        if(user.hasClan())
        {
            MessageManager.sendMessageFormated(player, MessageManager.PLAYER_BELONGS_TO_ANOTHER_CLAN, MessageType.CHAT);
            return;
        }
        if(!clan.hasInvite(user))
        {
            // not received invite
            MessageManager.sendMessageFormated(player, MessageManager.NOT_RECEIVED_INVITE, MessageType.CHAT);
            return;
        }
        if(isLimitMember(clan))
        {
            MessageManager.sendMessageFormated(player, MessageManager.LIMIT_MEMBERS_REACHED, MessageType.CHAT);
            return;
        }

        // join to clan - call event
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
        // join to clan - call event
        joinClanCheckEvent(player, clan);
    }

    private void joinClanCheckEvent(Player player, Clan clan) {
        User user = userManager.getUserData().get(player.getUniqueId());
        JoinClanEvent event = new JoinClanEvent(clan, player);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            user.setClan(clan);
            clan.joinUser(user);

            dataManager.updateClan(clan);
            dataManager.updateUser(user);

            MessageManager.sendMessageFormated(player, MessageManager.CLAN_INVITE_ACCEPTED, MessageType.CHAT);
        }
    }
    public void deleteClan(Player owner)
    {
        User user = userManager.getUserData().get(owner.getUniqueId());

        Clan clan = user.getClan();

        isOwner(clan, user);

        // call event and delete clan
        handleDeleteClan(clan, owner);
    }

    public void deleteClanByAdmin(Player admin, Clan clan) {
        // call event and delete clan
        boolean isDeleted = handleDeleteClan(clan, null);
        if(isDeleted) {
            MessageManager.sendMessageFormated(admin, MessageManager.CLAN_DELETED.replace("{clan}", clan.getTag()), MessageType.CHAT);
        }
    }
    private boolean handleDeleteClan(Clan clan, Player player) {
        // null mean the player deleted clan is admin
        if(player==null) {
            return deleteClan(clan, null);
        }

        DeleteClanEvent event = new DeleteClanEvent(player, clan);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            return deleteClan(clan, player);
        }

        return false;
    }

    private boolean deleteClan(Clan clan, Player player) {
        String tag = clan.getTag();
//        for(User user : clan.getMembers()) {
//            user.setClan(null);
//            dataManager.updateUser(user);
//        }
        for(UUID uuid : clan.getMembers()) {
            User user = userManager.getUserData().get(uuid);
            user.setClan(null);
            dataManager.updateUser(user);
        }

        for(String alliance : clan.getAlliances()) {
            Clan allianceClan = clansData.get(alliance.toUpperCase());
            if(allianceClan==null){
                plugin.getLogger().info("Alliance clan not found: "+alliance);
                continue;
            }
            allianceClan.removeAlliance(clan);
            dataManager.updateClan(allianceClan);
        }

        deleteClan(tag);

        dataManager.deleteClan(clan);
        // remove clan to system ranking
        topRankScheduler.removeClan(clan);

        // if player is null that mean clan is removed by admin
        if(player!=null) {
            MessageManager.broadcastMessageFormated(MessageManager.CLAN_DELETED.replace("{clan}", tag), MessageType.CHAT);
        }
        return true;
    }
    public void createClan(Player player, String tag) {
        if(hasClan(player))
        {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_ALREADY_BELONG_TO_CLAN, MessageType.CHAT);
            return;
        }

        // check MIN AND MAX LENGTHS of tag
        if(tag.length() < 3 || tag.length() > 5)
        {
            MessageManager.sendMessageFormated(player, MessageManager.TAG_LENGTH, MessageType.CHAT);
            return;
        }
        // check the name is not busy
        if(isTagClaimed(tag.toUpperCase()))
        {
            MessageManager.sendMessageFormated(player, MessageManager.TAG_ALREADY_EXISTS, MessageType.CHAT);
            return;
        }
        User user = userManager.getUserData().get(player.getUniqueId());
        if(user==null)
            return;

        boolean enablePayment = false; // TODO check if payment is enabled
        if(enablePayment) {
            boolean status = checkPayments(player);
            if(!status)
                return;
        }

        // create clan
        CreateClanEvent event = new CreateClanEvent(player, tag);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            Clan clan = new Clan(tag.toUpperCase(), user, false);
            clansData.put(tag.toUpperCase(), clan);
            user.setClan(clan);

            dataManager.createIfNotExist(clan);
            dataManager.updateUser(user);

            // add clan to system ranking
            topRankScheduler.addClan(clan);
            // TODO add clan to ranking
            MessageManager.sendMessageFormated(player, MessageManager.CLAN_CREATED.replace("{clan}", tag), MessageType.CHAT);

        }

    }

    private boolean checkPayments(Player player) {
        // TODO check payment
        return true;
//        if(config.costType == CostType.VAULT) {
//            Economy economy = plugin.getEconomy();
//            if(!economy.has(player, config.costCreate))
//            {
//                MessageUtil.sendMessage(player, lang.noMoney.replace("{cost}", String.valueOf(config.costCreate)));
//                return false;
//            }
//            economy.withdrawPlayer(player, config.costCreate);
//            return true;
//        } else {
//            int amount = ItemUtil.calcItemAmount(player, config.itemCost);
//            int needAmount = (int) config.costCreate;
//            if(amount<needAmount)
//            {
//                MessageUtil.sendMessage(player, lang.noItem
//                        .replace("{amount}", String.valueOf(needAmount))
//                );
//                return false;
//            }
//            ItemUtil.removeItems(player, config.itemCost, needAmount);
//            return true;
//        }
    }

    // kick user from clan by admin
    public void forceKickUser(Player sender, User user) {
        if(!user.hasClan()) {
            MessageManager.sendMessageFormated(sender, MessageManager.PLAYER_DOES_NOT_BELONG_TO_ANY_CLAN, MessageType.CHAT);
            return;
        }
        // get user clan
        Clan clan = user.getClan();
        // kick player from clan
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

        User deputyOwner = userManager.getUserData().get(player.getUniqueId());

        Clan clan = deputyOwner.getClan();

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
        // kick player from clan
        User kUser = userManager.getUserData().get(targetUUID);
        handleKickUser(player, kUser, clan);
    }


    private void handleKickUser(Player player, User kickedUser, Clan clan) {
        PlayerKickClanEvent event = new PlayerKickClanEvent(clan, kickedUser.getUuid());
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            clan.removeMember(kickedUser);
            kickedUser.setClan(null);

            Player kickedPlayer = Bukkit.getPlayer(kickedUser.getUuid());

            if(kickedPlayer!=null) {
                MessageManager.sendMessageFormated(kickedPlayer, MessageManager.KICKED_FROM_CLAN, MessageType.CHAT);
            }

            dataManager.updateClan(clan);
            dataManager.updateUser(kickedUser);

            // if its null then mean the kicked user is from the console
            if(player!=null) {
                MessageManager.sendMessageFormated(player, MessageManager.SUCCESSFULLY_KICKED_PLAYER.replace("{player}", player.getName()), MessageType.CHAT);
            }
        }
    }

    private boolean isSame(Player player, UUID targetUUID) {
        return player.getUniqueId().equals(targetUUID);
    }

    public void leaveClan(Player player) {
        User user = userManager.getUserData().get(player.getUniqueId());
        Clan clan = user.getClan();
        if(isOwner(clan, user))
        {
            MessageManager.sendMessageFormated(player, MessageManager.CANNOT_LEAVE_WHILE_OWNER, MessageType.CHAT);
            return;
        }
        // left clan
        LeaveClanEvent event = new LeaveClanEvent(player, clan);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            clan.removeMember(user);
            user.setClan(null);

            dataManager.updateClan(clan);
            dataManager.updateUser(user);

            MessageManager.sendMessageFormated(player, MessageManager.LEAVE_CLAN, MessageType.CHAT);
        }
    }
    private boolean isMemberInYourClan(Clan clan, User user) {
        return clan.getMembers().contains(user);
    }

    private boolean isOwner(Clan clan, User user) {
        if(clan == null)
            return false;
        return clan.getOwnerUUID().equals(user);
    }
    private boolean isOwner(Clan clan, UUID user) {
        if(clan == null)
            return false;
        return clan.getOwnerUUID().getUuid().equals(user);
    }
    private boolean isTagClaimed(String tag) {
        return clansData.get(tag) != null;
    }

    public void alliance(Player player, Clan allianceClan) {
        User deputyOwner = userManager.getUserData().get(player.getUniqueId());
        if(!isDeputyOwner(deputyOwner.getClan(), player) && !isOwner(deputyOwner.getClan(), deputyOwner))
        {
            MessageManager.sendMessageFormated(player, MessageManager.NOT_DEPUTY, MessageType.CHAT);
            return;
        }
        Clan clan = deputyOwner.getClan();

        if(isMemberInYourClan(allianceClan, deputyOwner)) {
            MessageManager.sendMessageFormated(player, MessageManager.CANNOT_ALLIANCE_YOUR_OWN_CLAN, MessageType.CHAT);
            return;
        }
        if(clan.isAlliance(allianceClan.getTag())) {

            DisbandAllianceEvent event = new DisbandAllianceEvent(clan, allianceClan);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                // remove alliance from both clan
                clan.removeAlliance(allianceClan);
                allianceClan.removeAlliance(clan);
                // add to database
//                clanService.deleteAlliance(clan.getTag());
                dataManager.updateClan(clan);
                dataManager.updateClan(allianceClan);

                // message
                MessageManager.sendMessageFormated(player, MessageManager.ALLIANCE_DISBANDED, MessageType.CHAT);
            }
            return;
        }
        // check limit
        if(isLimitAlliance(clan)) {
            MessageManager.sendMessageFormated(player, MessageManager.LIMIT_ALLIANCES_REACHED, MessageType.CHAT);
            return;
        }

        // check if you have already been invited to the alliance.
        if(allianceClan.isSuggestAlliance(clan))
        {
            CreateAllianceEvent event = new CreateAllianceEvent(clan, allianceClan);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                // add to both clan alliance
                allianceClan.removeSuggestAlliance(clan);
                clan.addAlliance(allianceClan);
                allianceClan.addAlliance(clan);
                // add to database

                dataManager.updateClan(clan);
                dataManager.updateClan(allianceClan);

                // message
                MessageManager.sendMessageFormated(player, MessageManager.ALLIANCE_CREATED, MessageType.CHAT);
            }
            return;
        }
        if(!clan.isSuggestAlliance(allianceClan))
        {
            clan.inviteAlliance(allianceClan);

            MessageManager.sendMessageFormated(player, MessageManager.INVITED_CLAN_TO_ALLIANCE.replace("{clan}", allianceClan.getTag()), MessageType.CHAT);
            Player allianceOwner = Bukkit.getPlayer(allianceClan.getOwnerUUID().getUuid());
            if (allianceOwner != null)
                MessageManager.sendMessageFormated(allianceOwner, MessageManager.YOUR_CLAN_WAS_INVITED_TO_ALLIANCE.replace("{clan}", clan.getTag()), MessageType.CHAT);

        } else {
            clan.removeInviteAlliance(allianceClan);
            MessageManager.sendMessageFormated(player, MessageManager.CANCELED_INVITE_FROM_ALLIANCE.replace("{clan}", allianceClan.getTag()), MessageType.CHAT);

        }


    }

    public void removeDeputy(Player owner) {
        User user = userManager.getUserData().get(owner.getUniqueId());
        Clan clan = user.getClan();
        Player player = owner.getPlayer();
        if(deputyIsEmpty(clan)) {
            MessageManager.sendMessageFormated(player, MessageManager.DEPUTY_EMPTY, MessageType.CHAT);
            return;
        }
        DeleteDeputyEvent event = new DeleteDeputyEvent(clan, clan.getDeputyOwnerUUID());
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            clan.setDeputyOwnerUUID(null);
            dataManager.updateClan(clan);
            MessageManager.sendMessageFormated(player, MessageManager.DEPUTY_REMOVED, MessageType.CHAT);
        }
    }

    private boolean deputyIsEmpty(Clan clan) {
         return clan.getDeputyOwnerUUID()==null;
    }

    public void setDeputy(Player owner, Player target) {
        User user = userManager.getUserData().get(owner.getUniqueId());
        User targetUser = userManager.getUserData().get(target.getUniqueId());

        if(!isDeputyOwner(user.getClan(), owner) && !isOwner(user.getClan(), user)){
            MessageManager.sendMessageFormated(owner, MessageManager.NOT_DEPUTY, MessageType.CHAT);
            return;
        }

        Clan clan = user.getClan();
        if(!isMemberInYourClan(clan, targetUser))
        {
            MessageManager.sendMessageFormated(owner, MessageManager.PLAYER_DOES_NOT_BELONG_TO_YOUR_CLAN, MessageType.CHAT);
            return;
        }

        if(clan.isDeputy(targetUser)) {
            MessageManager.sendMessageFormated(owner, MessageManager.PLAYER_ALREADY_DEPUTY.replace("{player}", target.getName()), MessageType.CHAT);
            return;
        }

        DeputyChangeClanEvent event = new DeputyChangeClanEvent(user.getClan(), owner, target);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            clan.setDeputyOwnerUUID(targetUser);
            dataManager.updateClan(clan);
            MessageManager.sendMessageFormated(owner, MessageManager.CLAN_DEPUTY_SET.replace("{player}", target.getName()).replace("{clan}", clan.getTag()), MessageType.CHAT);
        }
    }

    public boolean doesClanFulfillThreshold(Clan clan) {
        return clan.getMembers().size() >= 3; // todo set members required for ranking from config
    }
    private boolean isLimitAlliance(Clan clan) {
        return clan.getAlliances().size() >= 4; // todo set alliance limit from config
    }

    public Clan getClan(String tag) {
        return clansData.get(tag.toUpperCase());
    }

    public Clan deleteClan(String tag) {
        return clansData.remove(tag.toUpperCase());
    }

    public HashMap<String, Clan> getClansData() {
        return clansData;
    }


    public void loadAllClans() {
//        clanEntityRepository.getAllEntities().forEach(clan -> clansData.put(clan.getTag(), clan));
        dataManager.getAllClans().forEach(clan -> clansData.put(clan.getTag().toUpperCase(), clan));

    }

}
