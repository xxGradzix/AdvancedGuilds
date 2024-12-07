package me.xxgradzix.advancedclans.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.config.Config;
import me.xxgradzix.advancedclans.data.database.controllers.clansCOre.ClanController;
import me.xxgradzix.advancedclans.data.database.controllers.clansCOre.UserController;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.data.database.services.hideout.VentureRewardDataManager;
import me.xxgradzix.advancedclans.entities.PlayerStat;
import me.xxgradzix.advancedclans.entities.RankType;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition.ExpeditionDto;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition.ExpeditionManager;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.scheduler.TopRankScheduler;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;


public class VenturePlaceholder extends PlaceholderExpansion {

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

    public VenturePlaceholder()
    {
        this.register();
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String identifier) {
        if (offlinePlayer.getPlayer() == null) return null;
        Player player = offlinePlayer.getPlayer();

        if (identifier.startsWith("expedition")) {
            User user = ClanAndUserDataManager.getCachedUser(player.getUniqueId());
            if (user == null) {
                return "";
            }

            boolean haveExpeditionGoing = false;

            ExpeditionDto expeditionDto = ExpeditionManager.getExpeditionDtoByPlayer(player);

            String objective = "§7ᴏʙᴇᴄɴɪᴇ ɴɪᴇ ᴍᴀꜱᴢ ᴡʏᴘʀᴀᴡʏ";
            String level = "§7ᴀʙʏ ʀᴏᴢᴘᴏᴄᴢᴀᴄ ᴡʏᴘʀᴀᴡę, ᴋʟɪᴋɴɪᴊ §8§lᴘᴘᴍ§r§7 ɴᴀ";
            String literal = "§7ᴋᴀᴘɪᴛᴀɴᴀ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ";
            String timeLeft = "";

            if(expeditionDto != null) {
                haveExpeditionGoing = true;
                boolean finished = expeditionDto.isFinished();

                objective = ColorFixer.addColors("&7ᴄᴇʟ: " + expeditionDto.getObjective().getName());

                level = ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ: " + ExpeditionManager.getDiffLevelByLevel(expeditionDto.getExpeditionLevel()));

                literal = ColorFixer.addColors("&7ᴘᴏᴢᴏꜱᴛᴀłᴏ: ");

                timeLeft = MessageManager.secondsToTimeFormat(expeditionDto.secondsToCompletion());

                if(finished) {
                    timeLeft = ColorFixer.addColors("&aɢᴏᴛᴏᴡᴇ ᴅᴏ ᴏᴅʙɪᴏʀᴜ");
                }
            }

            switch (identifier.toLowerCase()) {
                case "expedition_time_left":

                    return timeLeft;

                case "expedition_objective":

                    return objective;

                case "expedition_difficulty":
                    return level;

                case "expedition_time_left_literal":
                    return literal;
            }
            return null;
        }
        return null;
    }


}
