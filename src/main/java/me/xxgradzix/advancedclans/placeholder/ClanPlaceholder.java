package me.xxgradzix.advancedclans.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.entities.PlayerStat;
import me.xxgradzix.advancedclans.entities.RankType;
import me.xxgradzix.advancedclans.manager.ClanManager;
import me.xxgradzix.advancedclans.manager.UserManager;
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
    
    private final UserManager userManager;
    private final ClanManager clanManager;
    private final TopRankScheduler topRankScheduler;
//    private Config config;
    public ClanPlaceholder(AdvancedGuilds plugin, UserManager userManager, ClanManager clanManager, TopRankScheduler topRankScheduler)
    {
        this.plugin = plugin;
        this.userManager = userManager;
        this.clanManager = clanManager;
        this.topRankScheduler = topRankScheduler;
//        this.config = plugin.getConfigPlugin();
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
            User user = userManager.getUserData().get(player.getUniqueId());
            if (user == null) {
                return "";
            }
            switch (identifier.toLowerCase()) {
                case "user_has_clan":
                    return user.hasClan() ? ColorFixer.addColors("config.hasClan") : ColorFixer.addColors("config.hasNotClan)");
                case "user_format_points":
                    return ColorFixer.addColors(
//                            config.formatUserPoints.replace("{points}", String.valueOf(user.getPoints()))
                            String.valueOf(user.getPoints()) // TODO get from config
                    );
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
            User user = userManager.getUserData().get(player.getUniqueId());
            if (user == null) {
                return "";
            }
            switch (identifier.toLowerCase()) {
                case "clan_format_points":
                    String averagePoints = clanManager.getAveragePoint(player);
                    return ColorFixer.addColors(
//                            config.formatClanPoints.replace("{points}", averagePoints)
                            averagePoints // TODO get from config
                    );
                case "clan_format_tag":
                    if (user.getClan() == null) return "config.noneTag";
                    return ColorFixer.addColors(
//                            config.formatTag.replace("{tag}", user.getClan().getTag())
                            user.getClan().getTag() // TODO get from config
                    );
                case "clan_points":
                    if (user.getClan() == null) return "";
                    return clanManager.getAveragePoint(player);
                case "clan_tag":
                    if (user.getClan() == null) return "";
                    return user.getClan().getTag();
                case "clan_members_size":
                    if (user.getClan() == null) return "0";
                    return String.valueOf(user.getClan().getMembers().size());
                case "clan_members_online":
                    if (user.getClan() == null) return "0";
                    return String.valueOf(clanManager.countOnlineMember(user.getClan()));
            }
            return null;
        }
        return null;
    }

    @Override
    public String onPlaceholderRequest(Player first, Player second, String identifier) {
        if (first == null || second == null) return null;

        if (identifier.equalsIgnoreCase("tag")) {
            User user1 = userManager.getUserData().get(first.getUniqueId());
            User user2 = userManager.getUserData().get(second.getUniqueId());

            if (user1 == null || user2 == null) return null;

            Clan clan1 = user2.getClan();
            if (clan1 == null) return "";

            String tag = clan1.getTag();

            if (clan1.isMember(first.getUniqueId())) {
//                return ColorFixer.addColors(config.formatMember.replace("{tag}", String.valueOf(tag)));
                return ColorFixer.addColors(String.valueOf(tag)); //TODO get from config
            }

            Clan clan2 = user1.getClan();
            if (clan2 != null && clan1.isAlliance(clan2.getTag())) {
//                return ColorFixer.addColors(config.formatAlliance.replace("{tag}", String.valueOf(tag)));
                return ColorFixer.addColors(String.valueOf(tag)); //TODO get from config

            }

//            return ColorFixer.addColors(config.formatNormal.replace("{tag}", String.valueOf(tag)));
            return ColorFixer.addColors(String.valueOf(tag)); //TODO get from config
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
