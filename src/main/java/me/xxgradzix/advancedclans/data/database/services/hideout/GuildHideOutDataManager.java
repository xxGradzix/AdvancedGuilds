package me.xxgradzix.advancedclans.data.database.services.hideout;

import eu.decentsoftware.holograms.api.DHAPI;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import me.xxgradzix.advancedclans.data.database.repositories.hideout.GuildHideoutEntityRepository;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.exceptions.ClanDoesNotExistException;
import me.xxgradzix.advancedclans.exceptions.PlayerDoesNotBelongToClanException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutDoesNotExistException;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.UpgradePattern;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.xxgradzix.advancedclans.data.database.controllers.hideouts.GuildHideOutController.*;

public class GuildHideOutDataManager {

    private static final HashMap<String, GuildHideout> guildHideouts = new HashMap<>();
    private static final HashMap<GuildHideout, Location> guildHideoutEntries = new HashMap<>();

    private static GuildHideoutEntityRepository guildHideoutEntityRepository;


    public GuildHideOutDataManager(GuildHideoutEntityRepository guildHideoutEntityRepository) {
        GuildHideOutDataManager.guildHideoutEntityRepository = guildHideoutEntityRepository;
    }

    /** ADMIN METHODS **/

    public static GuildHideout resetOrCreateHideOut(String hideOutWorldName) {

        GuildHideout guildHideout = guildHideouts.get(hideOutWorldName);

        if (guildHideout == null) {
            guildHideout = new GuildHideout(hideOutWorldName);
        }

        Clan clan = ClanAndUserDataManager.getCachedClan(guildHideout.getClanTag());

        if(clan != null){
            clan.setHideoutId(null);
            ClanAndUserDataManager.updateClan(clan);
        }

        guildHideout.reset();

        guildHideoutEntries.remove(guildHideout);
        refreshHideoutOutpostHolograms(guildHideout, guildHideout.getEntryBlockLocation());
        try {
            guildHideouts.put(hideOutWorldName, guildHideout);
            guildHideoutEntityRepository.createOrUpdateEntity(guildHideout);
            return guildHideout;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setHideOutOperatingLocation(GuildHideout guildHideout, Location location) {
        if(location == null) return;

        location = location.getBlock().getLocation();

        guildHideout.setEntryBlockLocation(location);

        updateHideOut(guildHideout);
        guildHideoutEntries.put(guildHideout, location);

        refreshHideoutOutpostHolograms(guildHideout, location);
    }

    public static void refreshHideoutOutpostHolograms(GuildHideout guildHideout, Location location) {
        if(location == null) return;
        location = location.getBlock().getLocation();


        final String defaultHideoutHologram = guildHideout.getWorldName() + DEFAULT_HIDEOUT_HOLOGRAM_SUFFIX;
        final String occupiedHideoutHologram = guildHideout.getWorldName() + OCCUPIED_HIDEOUT_HOLOGRAM_SUFFIX;

        DHAPI.removeHologram(defaultHideoutHologram);
        DHAPI.removeHologram(occupiedHideoutHologram);

        if(guildHideout.getClanTag() == null || guildHideout.getClanTag().isEmpty()) {
            DHAPI.createHologram(defaultHideoutHologram, location.clone().add(0.5, 2, 0.5), Arrays.asList(ColorFixer.addColors("#b59651&lᴋʀʏᴊóᴡᴋᴀ ɢɪʟᴅʏᴊɴᴀ"), ColorFixer.addColors("&7ᴘᴏᴌóż"), ColorFixer.addColors("#55875fᴢᴇꜱᴛᴀᴡ ᴅᴏ ᴛᴡᴏʀᴢᴇɴɪᴀ ɢɪʟᴅɪɪ"), ColorFixer.addColors("&7ᴀʙʏ ᴢᴀᴊąć ᴛą ᴋʀʏᴊóᴡᴋę")));
            UpgradePattern pattern = upgradePatterns.get(GuildHideout.Upgrade.OUTPOST_PODEST);
            paste(location, pattern.getSchemFile());
        } else {
            DHAPI.createHologram(occupiedHideoutHologram, location.clone().add(0.5, 2, 0.5), Arrays.asList(ColorFixer.addColors("&7&lᴋʀʏᴊóᴡᴋᴀ ɢɪʟᴅɪɪ #b59651&l" + guildHideout.getClanTag()), ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴢᴇᴊść ᴅᴏ ᴋʀʏᴊóᴡᴋɪ")));
            UpgradePattern pattern = upgradePatterns.get(GuildHideout.Upgrade.OUTPOST_HUT);
            paste(location, pattern.getSchemFile());
        }
    }

    /**
     * PLAYER METHODS
     **/

    public static GuildHideout getHideOutByLocation(Location location) {
        if(guildHideoutEntries.containsValue(location)) {
            for (Map.Entry<GuildHideout, Location> entry: guildHideoutEntries.entrySet()) {
                if(entry.getValue().equals(location)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public static void attemptTeleportToHideOut(Player player, GuildHideout guildHideout) throws HideOutDoesNotExistException, ClanDoesNotExistException, PlayerDoesNotBelongToClanException {

        if (guildHideout == null) throw new HideOutDoesNotExistException("Hideout does not exist");

        String clanTag = guildHideout.getClanTag();

        Clan clan = ClanAndUserDataManager.getCachedClan(clanTag);

        if (clan == null) throw new ClanDoesNotExistException();

        if(clan.getMembers().contains(player.getUniqueId()) && !player.isOp()) throw new PlayerDoesNotBelongToClanException();

        Location teleportLocation = guildHideout.getTeleportLocation();
        Bukkit.getScheduler().runTask(AdvancedGuilds.instance, () -> player.teleport(teleportLocation));

    }

    public static void occupyHideOut(String hideOutWorldName, Clan clan) throws HideOutDoesNotExistException {

        GuildHideout guildHideout = guildHideouts.get(hideOutWorldName);

        if (guildHideout == null) throw new HideOutDoesNotExistException("Hideout does not exist");

        guildHideout = resetOrCreateHideOut(hideOutWorldName);
        guildHideout.setClanTag(clan);

        clan.setHideoutId(guildHideout.getWorldName());
        ClanAndUserDataManager.updateClan(clan);

        updateHideOut(guildHideout);
    }

    public static void occupyHideOut(GuildHideout hideout, Clan clan) {

        hideout = resetOrCreateHideOut(hideout.getWorldName());
        hideout.setClanTag(clan);

        clan.setHideoutId(hideout.getWorldName());
        ClanAndUserDataManager.updateClan(clan);

        updateHideOut(hideout);
        refreshHideoutOutpostHolograms(hideout, hideout.getEntryBlockLocation());
    }


    public static void updateHideOut(GuildHideout hideout) {
        try {
            guildHideoutEntityRepository.createOrUpdateEntity(hideout);
            guildHideouts.put(hideout.getWorldName(), hideout);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<GuildHideout> loadAllHideOuts() {
        try {
            List<GuildHideout> allEntities = guildHideoutEntityRepository.getAllEntities();
            for(GuildHideout hideout : allEntities) {
                guildHideouts.put(hideout.getWorldName(), hideout);
                if(hideout.getEntryBlockLocation() != null) {
                    guildHideoutEntries.put(hideout, hideout.getEntryBlockLocation());
                    refreshHideoutOutpostHolograms(hideout, hideout.getEntryBlockLocation());
                }
            }
            return allEntities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static GuildHideout getHideOut(@NotNull String name) {
        return guildHideouts.get(name);
    }

    public static boolean isHideoutOccupied(String worldName) {

        GuildHideout guildHideout = guildHideouts.get(worldName);
        if(guildHideout == null) return false;
        if(guildHideout.getClanTag() == null) return false;
        Clan clan = ClanAndUserDataManager.getCachedClan(guildHideout.getClanTag());

        return clan != null;
    }
}
