package me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition;

import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ExpeditionManager {


    private static final HashMap<Player, ExpeditionDto> expeditionStatus = new HashMap<>();


    public static ExpeditionDto getExpeditionDtoByPlayer(Player player) {
        return expeditionStatus.get(player);
    }

    public static void addExpeditionDtoByPlayer(Player player, ExpeditionDto expeditionDto) {
        expeditionStatus.put(player, expeditionDto);
    }

    public static void removePlayerExpeditionDTO(Player player) {
        expeditionStatus.remove(player);
    }

    public static String getChancePercentByChance(double chance) {
        String result = "";

        if(chance < 0.35) {
            result += "&4";

        } else if (chance < 0.55) {
            result += "&e";

        } else {
            result += "&a";

        }
        return ColorFixer.addColors(result + ((int)chance * 100) + "%");
    }
    public static String getDiffLevelByLevel(int level) {

        return switch (level) {
            case 1 -> "&aŁᴀᴛᴡʏ";

            case 2 -> "&eśʀᴇᴅɴɪ";

            default -> "&4ᴛʀᴜᴅɴʏ";
        };
    }
}
