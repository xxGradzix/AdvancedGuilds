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

            String objective = "N/A";
            String chance = "N/A";
            String timeLeft = "N/A";

            if(expeditionDto != null) {
                haveExpeditionGoing = true;
                boolean finished = expeditionDto.isFinished();

                chance = ColorFixer.addColors("&7szansa powodzenia: " + ExpeditionManager.getChancePercentByChance(expeditionDto.getChance()) + "%");

                objective = ColorFixer.addColors("&7ᴄᴇʟ: " + expeditionDto.getObjective().getName());

                timeLeft = ColorFixer.addColors("&7ᴘᴏᴢᴏꜱᴛᴀłᴏ: ") + MessageManager.secondsToTimeFormat(expeditionDto.secondsToCompletion());
                if(finished) {
                    timeLeft = ColorFixer.addColors("&aɢᴏᴛᴏᴡᴇ ᴅᴏ ᴏᴅʙɪᴏʀᴜ");
                }
            }

            switch (identifier.toLowerCase()) {
                case "expedition_time_left":

                    return timeLeft;

                case "expedition_objective":

                    return objective;

                case "expedition_chance":
                    return chance;
            }
            return null;
        }
        return null;
    }


}
