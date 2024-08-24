package me.xxgradzix.advancedclans.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.config.Config;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.services.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.entities.PlayerStat;
import me.xxgradzix.advancedclans.entities.RankType;
import me.xxgradzix.advancedclans.controllers.ClanController;
import me.xxgradzix.advancedclans.controllers.UserController;
import me.xxgradzix.advancedclans.scheduler.TopRankScheduler;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public class ClanPlaceholder extends PlaceholderExpansion implements Relational {

    @Override
    public @NotNull String getIdentifier() {
        return "advancedguilds";
    }

    @Override
    public @NotNull String getAuthor() {
        return "xxGradzix";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    private final AdvancedGuilds plugin;
    
    private final UserController userController;
    private final ClanController clanController;
    private final TopRankScheduler topRankScheduler;
//    private Config config;
    public ClanPlaceholder(AdvancedGuilds plugin, UserController userController, ClanController clanController, TopRankScheduler topRankScheduler)
    {
        this.plugin = plugin;
        this.userController = userController;
        this.clanController = clanController;
        this.topRankScheduler = topRankScheduler;
        this.register();
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String identifier) {
        if (offlinePlayer.getPlayer() == null) return null;
        Player player = offlinePlayer.getPlayer();
        if (identifier.startsWith("top")) {
            String[] args = identifier.split("_");
            if (args.length >= 4 && isNumber(args[2])) {
                int top = Integer.parseInt(args[2]);
                if (identifier.startsWith("top_kill")) {
                    return handleTopType(RankType.KILLS, identifier, top);
                }
                if (identifier.startsWith("top_death")) {
                    return handleTopType(RankType.DEATHS, identifier, top);
                }
                if (identifier.startsWith("top_points")) {
                    return handleTopType(RankType.USER_POINTS, identifier, top);
                }
                if (identifier.startsWith("top_clan")) {
                    return handleTopType(RankType.CLAN_POINTS, identifier, top);
                }
            }

        }
        if (identifier.startsWith("user")) {
            User user = ClanAndUserDataManager.getCachedUser(player.getUniqueId());
            if (user == null) {
                return "";
            }
            switch (identifier.toLowerCase()) {
                case "user_has_clan":
                    return user.hasClan() ? ColorFixer.addColors(Config.hasClan) : ColorFixer.addColors(Config.hasNotClan);
                case "user_format_points":
                    return ColorFixer.addColors(Config.formatUserPoints.replace("{points}", String.valueOf(user.getPoints())));
                case "user_points":
                    return String.valueOf(user.getPoints());
                case "user_kills":
                    return String.valueOf(user.getKills());
                case "user_death":
                    return String.valueOf(user.getDeath());
            }
            return null;
        }
        if (identifier.startsWith("clan")) {
            User user = ClanAndUserDataManager.getCachedUser(player.getUniqueId());
            if (user == null) {
                return "";
            }
            switch (identifier.toLowerCase()) {
                case "clan_format_points":
                    String averagePoints = clanController.getAveragePoint(player);
                    return ColorFixer.addColors(
                            Config.formatClanPoints.replace("{points}", averagePoints)
//                            averagePoints
                    );
                case "clan_format_tag":
                    if (ClanAndUserDataManager.getCachedClan(user.getClanTag()) == null) return "config.noneTag";
                    return ColorFixer.addColors(
                            Config.formatTag.replace("{tag}", ClanAndUserDataManager.getCachedClan(user.getClanTag()).getTag())
                    );
                case "clan_points":
                    if (ClanAndUserDataManager.getCachedClan(user.getClanTag()) == null) return "";
                    return clanController.getAveragePoint(player);
                case "clan_tag":
                    if (ClanAndUserDataManager.getCachedClan(user.getClanTag()) == null) return "";
                    return ClanAndUserDataManager.getCachedClan(user.getClanTag()).getTag();
                case "clan_members_size":
                    if (ClanAndUserDataManager.getCachedClan(user.getClanTag()) == null) return "0";
                    return String.valueOf(ClanAndUserDataManager.getCachedClan(user.getClanTag()).getMembers().size());
                case "clan_members_online":
                    if (ClanAndUserDataManager.getCachedClan(user.getClanTag()) == null) return "0";
                    return String.valueOf(clanController.countOnlineMember(ClanAndUserDataManager.getCachedClan(user.getClanTag())));
            }
            return null;
        }
        return null;
    }

    @Override
    public String onPlaceholderRequest(Player first, Player second, String identifier) {
        if (first == null || second == null) return null;

        if (identifier.equalsIgnoreCase("tag")) {
            User user1 = ClanAndUserDataManager.getCachedUser(first.getUniqueId());
            User user2 = ClanAndUserDataManager.getCachedUser(second.getUniqueId());

            if (user1 == null || user2 == null) return null;

            Clan clan1 = ClanAndUserDataManager.getCachedClan(user2.getClanTag());
            if (clan1 == null) return "";

            String tag = clan1.getTag();

            if (clan1.isMember(first.getUniqueId())) {
                return ColorFixer.addColors(Config.formatMember.replace("{tag}", String.valueOf(tag)));
            }

            Clan clan2 = ClanAndUserDataManager.getCachedClan(user1.getClanTag());
            if (clan2 != null && clan1.isAlliance(clan2.getTag())) {
                return ColorFixer.addColors(Config.formatAlliance.replace("{tag}", String.valueOf(tag)));
            }

            return ColorFixer.addColors(Config.formatNormal.replace("{tag}", String.valueOf(tag)));
        }
        return null;
    }

    private boolean isNumber(String arg) {
        try {
            int a = Integer.parseInt(arg);
            return true;
        } catch (NumberFormatException ignore) {}

        return false;
    }

    private String handleTopType(RankType rankType, String identifier, int top) {
        Optional<PlayerStat> rank = topRankScheduler.getRank(rankType, top);
        if(rank.isEmpty())
            return "";

        PlayerStat playerStat = rank.get();
        if (identifier.endsWith("_value")) {
            return String.valueOf(playerStat.getInt());
        }
        if (identifier.endsWith("_name")) {
            return String.valueOf(playerStat.getName());
        }
        return "";
    }

}
