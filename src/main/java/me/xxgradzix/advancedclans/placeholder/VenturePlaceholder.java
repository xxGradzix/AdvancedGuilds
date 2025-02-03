package me.xxgradzix.advancedclans.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.xxgradzix.advancedclans.data.database.entities.clan.User;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataService;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition.ExpeditionDto;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition.ExpeditionManager;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


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
            User user = ClanAndUserDataService.getCachedUser(player.getUniqueId());
            if (user == null) {
                return "";
            }

            boolean haveExpeditionGoing = false;

            ExpeditionDto expeditionDto = ExpeditionManager.getExpeditionDtoByPlayer(player);

            String objective = "§7ᴏʙᴇᴄɴɪᴇ ɴɪᴇ ᴍᴀꜱᴢ ᴡʏᴘʀᴀᴡʏ";
            String level = "";

            String objectiveAndLevel = objective + " " + level;

            String literal = ColorFixer.addColors("§7ᴀʙʏ ʀᴏᴢᴘᴏᴄᴢᴀᴄ ᴡʏᴘʀᴀᴡę, ᴋʟɪᴋɴɪᴊ #b59651&lᴘᴘᴍ§r§7 ɴᴀ");
            String timeLeft = "&#004A6E&lᴋ&#004D75&lᴀ&#00507D&lᴘ&#005284&lɪ&#00558C&lᴛ&#005893&lᴀ&#005B9A&lɴᴀ &#0060A9&lᴇ&#0063B1&lᴋ&#0066B8&lꜱ&#0068C0&lᴘ&#006BC7&lᴇ&#006ECE&lᴅ&#0071D6&lʏ&#0073DD&lᴄ&#0076E5&lᴊ&#0079EC&lɪ";
            String speedUp = "";

            if(expeditionDto != null) {
                haveExpeditionGoing = true;
                boolean finished = expeditionDto.isFinished();

                objective = ColorFixer.addColors("&7ᴄᴇʟ: " + expeditionDto.getObjective().getName());
                level = ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ: " + ExpeditionManager.getDiffLevelByLevel(expeditionDto.getExpeditionLevel()));

                objectiveAndLevel = ColorFixer.addColors(objective + " &8| &r" + level);

                literal = ColorFixer.addColors("&7ᴇᴋꜱᴘᴇᴅʏᴄᴊᴀ ᴜᴋᴏńᴄᴢʏ ꜱɪę ᴢᴀ: ");
                timeLeft = MessageManager.secondsToTimeFormat(expeditionDto.secondsLeft());

                int requiredCoins = expeditionDto.secondsLeft() / (60 * 30);

                speedUp = ColorFixer.addColors("&8&l[§e§lᴘᴘᴍ§r&8&l] &#FFDB41&l⚡ &#FCDF52&lᴘ&#FBE25A&lʀ&#FAE462&lᴢ&#F9E66B&lʏ&#F7E873&lꜱ&#F6EA7C&lᴘ&#F5EC84&lɪ&#F4EF8C&lᴇ&#F2F195&lꜱ&#F1F39D&lᴢ &8&l(&6&l" + requiredCoins + " ᴍᴏɴᴇᴛ ᴘʀᴇᴍɪᴜᴍ&8&l)");

                if(finished) {
                    timeLeft = ColorFixer.addColors("&aɢᴏᴛᴏᴡᴇ ᴅᴏ ᴏᴅʙɪᴏʀᴜ");
                }
            }

            switch (identifier.toLowerCase()) {
                case "expedition_time_left":

                    return timeLeft;

                case "expedition_objective":
                    return objective;

                case "expedition_objective_and_level":
                    return objectiveAndLevel;

                case "expedition_difficulty":
                    return level;

                case "expedition_speed_up":
                    return speedUp;

                case "expedition_time_left_literal":
                    return literal;
            }
            return null;
        }
        return null;
    }


}
