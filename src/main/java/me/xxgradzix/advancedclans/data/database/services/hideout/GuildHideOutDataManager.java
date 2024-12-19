package me.xxgradzix.advancedclans.data.database.services.hideout;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3Imp;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.util.gson.BlockVectorAdapter;
import com.sk89q.worldedit.world.weather.WeatherType;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.*;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.GreetingFlag;
import eu.decentsoftware.holograms.api.DHAPI;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import me.xxgradzix.advancedclans.data.database.repositories.hideout.GuildHideoutEntityRepository;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.exceptions.ClanDoesNotExistException;
import me.xxgradzix.advancedclans.exceptions.PlayerDoesNotBelongToClanException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutDoesNotExistException;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.UpgradeBlueprint;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

import static com.sk89q.worldguard.protection.flags.Flags.*;
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
            refreshRegion(guildHideout);
        } catch (InvalidFlagFormat e) {
            throw new RuntimeException(e);
        }

        try {
            guildHideouts.put(hideOutWorldName, guildHideout);
            guildHideoutEntityRepository.createOrUpdateEntity(guildHideout);
            return guildHideout;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final WorldGuard WORLD_GUARD = WorldGuard.getInstance();

    private static void refreshRegion(GuildHideout guildHideout) throws InvalidFlagFormat {

        World bukkkitWorld = Bukkit.getWorld(guildHideout.getWorldName());

        if(bukkkitWorld == null) return;

        com.sk89q.worldedit.world.World adapt = BukkitAdapter.adapt(bukkkitWorld);

        RegionManager regionManager = WORLD_GUARD.getPlatform().getRegionContainer().get(adapt);

//        String worldName = guildHideout.getWorldName() + "_global";
        String worldName = "__global__";

        regionManager.removeRegion(worldName);

        ProtectedRegion region = new GlobalProtectedRegion(worldName);

        region.setFlag(Flags.INVINCIBILITY, StateFlag.State.ALLOW);

        region.setFlag(Flags.BUILD, StateFlag.State.DENY);

        region.setFlag(Flags.PVP, StateFlag.State.DENY);

        region.setFlag(Flags.ENTITY_ITEM_FRAME_DESTROY, StateFlag.State.DENY);

        region.setFlag(Flags.WIND_CHARGE_BURST, StateFlag.State.DENY);

        region.setFlag(Flags.MOB_SPAWNING, StateFlag.State.DENY);

        region.setFlag(Flags.WEATHER_LOCK, WEATHER_LOCK.parseInput(FlagContext.create().setInput("clear").build()));

        region.setFlag(TIME_LOCK, TIME_LOCK.parseInput(FlagContext.create().setInput("day").build()));

        region.setFlag(ENTRY, StateFlag.State.DENY);
        region.setFlag(Flags.ENTRY.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);

        region.setFlag(Flags.CHEST_ACCESS, StateFlag.State.DENY);
        region.setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);

        region.setFlag(GREET_MESSAGE, GREET_MESSAGE.parseInput(FlagContext.create().setInput(MessageManager.HIDEOUT_INFO_MESSAGE).build()));
        region.setFlag(GREET_TITLE, GREET_TITLE.parseInput(FlagContext.create().setInput("§7ᴡɪᴛᴀᴊ ᴡ ᴋʀʏᴊóᴡᴄᴇ").build()));

        region.setFlag(Flags.INTERACT, StateFlag.State.DENY);

        region.setPriority(10);

        regionManager.addRegion(region);

        try {
            regionManager.save();
        } catch (StorageException e) {
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
        try {
            refreshRegion(guildHideout);
        } catch (InvalidFlagFormat e) {
            throw new RuntimeException(e);
        }

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
            UpgradeBlueprint pattern = upgradePatterns.get(GuildHideout.Upgrade.OUTPOST_PODEST);
            paste(location, pattern.getSchemFile());
        } else {
            DHAPI.createHologram(occupiedHideoutHologram, location.clone().add(0.5, 2, 0.5), Arrays.asList(ColorFixer.addColors("&7&lᴋʀʏᴊóᴡᴋᴀ ɢɪʟᴅɪɪ #b59651&l" + guildHideout.getClanTag()), ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴢᴇᴊść ᴅᴏ ᴋʀʏᴊóᴡᴋɪ")));
            UpgradeBlueprint pattern = upgradePatterns.get(GuildHideout.Upgrade.OUTPOST_HUT);
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
        try {
            refreshRegion(hideout);
        } catch (InvalidFlagFormat e) {
            throw new RuntimeException(e);
        }

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
                    try {
                        refreshRegion(hideout);
                    } catch (InvalidFlagFormat e) {
                        throw new RuntimeException(e);
                    }

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
