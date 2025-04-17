package me.xxgradzix.advancedclans.data.database.services.hideout;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.*;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import eu.decentsoftware.holograms.api.DHAPI;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.controllers.dtos.NpcNameAndSkinPersistentDTO;
import me.xxgradzix.advancedclans.data.database.entities.clan.Clan;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.hideout.fields.Upgrade;
import me.xxgradzix.advancedclans.data.database.entities.hideout.fields.UpgradeInfoHolder;
import me.xxgradzix.advancedclans.data.database.repositories.hideout.GuildHideoutEntityRepository;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataService;
import me.xxgradzix.advancedclans.exceptions.clan.ClanDoesNotExistException;
import me.xxgradzix.advancedclans.exceptions.clan.PlayerDoesNotBelongToClanException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutDoesNotExistException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.InvalidHideoutWorldNameException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.UpgradeWasNotBoughtException;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.countdown.Countdown;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.util.*;

import static com.sk89q.worldguard.protection.flags.Flags.*;
import static me.xxgradzix.advancedclans.data.database.controllers.hideouts.GuildHideOutController.*;

public class GuildHideOutDataService {

    private final HashMap<String, GuildHideout> guildHideouts = new HashMap<>();
    private final HashMap<GuildHideout, Location> guildHideoutEntries = new HashMap<>();

    private final GuildHideoutEntityRepository guildHideoutEntityRepository;

    private static final AdvancedGuilds plugin = AdvancedGuilds.instance;

    public GuildHideOutDataService(GuildHideoutEntityRepository guildHideoutEntityRepository) {
        this.guildHideoutEntityRepository = guildHideoutEntityRepository;
    }

    /** ADMIN METHODS **/

    public GuildHideout resetOrCreateHideOut(String hideOutWorldName) {

        GuildHideout guildHideout = guildHideouts.get(hideOutWorldName);

        if (guildHideout == null) {
            guildHideout = new GuildHideout(hideOutWorldName);
        }

        Clan clan = ClanAndUserDataService.getCachedClan(guildHideout.getClanTag());

        List<Countdown> toRemove = new ArrayList<>();
        for (Countdown countdown : Countdown.countdowns.values()) {
            if (countdown.getOwner() instanceof Clan fetchedClan) {
                if (fetchedClan.equals(clan)) {
                    countdown.killTask();
                    toRemove.add(countdown);
                }
            }
        }

        for (Countdown countdown : toRemove) {
            Countdown.countdowns.values().remove(countdown);
        }

        if(clan != null){
            clan.setHideoutId(null);
            ClanAndUserDataService.updateClan(clan);
        }

        guildHideout.reset();

        guildHideoutEntries.remove(guildHideout);
        refreshHideoutOutpostHolograms(guildHideout, guildHideout.getEntryBlockLocation());

        try {
            refreshRegion(guildHideout);
        } catch (InvalidFlagFormat e) {
            throw new RuntimeException(e);
        }


        UpgradeInfoHolder storageUpgrade = new UpgradeInfoHolder((long) -1, true, Upgrade.STORAGE);
        storageUpgrade.setFinished();

        guildHideout.setUpgradeHolder(storageUpgrade);
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

        World bukkkitWorld = Bukkit.getWorld(guildHideout.getHideoutID());

        if(bukkkitWorld == null) return;

        com.sk89q.worldedit.world.World adapt = BukkitAdapter.adapt(bukkkitWorld);

        RegionManager regionManager = WORLD_GUARD.getPlatform().getRegionContainer().get(adapt);

        String worldName = "__global__";

        regionManager.removeRegion(worldName);

        ProtectedRegion region = new GlobalProtectedRegion(worldName);

        region.setFlag(Flags.INVINCIBILITY, StateFlag.State.ALLOW);

        region.setFlag(Flags.BUILD, StateFlag.State.DENY);

        region.setFlag(Flags.PVP, StateFlag.State.DENY);

        region.setFlag(Flags.ENTITY_ITEM_FRAME_DESTROY, StateFlag.State.DENY);

        region.setFlag(Flags.WIND_CHARGE_BURST, StateFlag.State.DENY);

        region.setFlag(Flags.MOB_SPAWNING, StateFlag.State.DENY);

        region.setFlag(VINE_GROWTH, StateFlag.State.DENY);
        region.setFlag(CROP_GROWTH, StateFlag.State.DENY);
        region.setFlag(ROCK_GROWTH, StateFlag.State.DENY);

        region.setFlag(Flags.WEATHER_LOCK, WEATHER_LOCK.parseInput(FlagContext.create().setInput("clear").build()));

        region.setFlag(TIME_LOCK, TIME_LOCK.parseInput(FlagContext.create().setInput("day").build()));

        region.setFlag(ENTRY, StateFlag.State.DENY);
        region.setFlag(Flags.ENTRY.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);

        region.setFlag(Flags.CHEST_ACCESS, StateFlag.State.DENY);
        region.setFlag(Flags.CHEST_ACCESS.getRegionGroupFlag(), RegionGroup.NON_MEMBERS);

        region.setFlag(GREET_MESSAGE, GREET_MESSAGE.parseInput(FlagContext.create().setInput(MessageManager.HIDEOUT_INFO_MESSAGE).build()));
        region.setFlag(GREET_TITLE, GREET_TITLE.parseInput(FlagContext.create().setInput("§7ᴡɪᴛᴀᴊ ᴡ ᴋʀʏᴊóᴡᴄᴇ").build()));

        region.setFlag(Flags.INTERACT, StateFlag.State.DENY);

        if(guildHideout.getClanTag() != null && !guildHideout.getClanTag().isEmpty()) {

            DefaultDomain defaultDomain = new DefaultDomain();

            defaultDomain.addGroup(guildHideout.getClanTag().toLowerCase());

            region.setMembers(defaultDomain);

        } else {
            region.setMembers(new DefaultDomain());
        }


        region.setPriority(10);

        regionManager.addRegion(region);

        try {
            regionManager.save();
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }

    }

    public void setHideOutOperatingLocation(GuildHideout guildHideout, Location location) {
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

    public void refreshHideoutOutpostHolograms(GuildHideout guildHideout, Location location) {
        if(location == null) return;
        location = location.getBlock().getLocation();


        final String defaultHideoutHologram = guildHideout.getHideoutID() + DEFAULT_HIDEOUT_HOLOGRAM_SUFFIX;
        final String occupiedHideoutHologram = guildHideout.getHideoutID() + OCCUPIED_HIDEOUT_HOLOGRAM_SUFFIX;

        DHAPI.removeHologram(defaultHideoutHologram);
        DHAPI.removeHologram(occupiedHideoutHologram);

        if(guildHideout.getClanTag() == null || guildHideout.getClanTag().isEmpty()) {
            DHAPI.createHologram(defaultHideoutHologram, location.clone().add(0.5, 2, 0.5), Arrays.asList(ColorFixer.addColors("#b59651&lᴋʀʏᴊóᴡᴋᴀ ɢɪʟᴅʏᴊɴᴀ"), ColorFixer.addColors("&7ᴘᴏᴌóż"), ColorFixer.addColors("#55875fᴢᴇꜱᴛᴀᴡ ᴅᴏ ᴛᴡᴏʀᴢᴇɴɪᴀ ɢɪʟᴅɪɪ"), ColorFixer.addColors("&7ᴀʙʏ ᴢᴀᴊąć ᴛą ᴋʀʏᴊóᴡᴋę")));
            paste(location, Upgrade.OUTPOST_PODEST.getSchemFile());
        } else {
            DHAPI.createHologram(occupiedHideoutHologram, location.clone().add(0.5, 2, 0.5), Arrays.asList(ColorFixer.addColors("&7&lᴋʀʏᴊóᴡᴋᴀ ɢɪʟᴅɪɪ #b59651&l" + guildHideout.getClanTag()), ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴢᴇᴊść ᴅᴏ ᴋʀʏᴊóᴡᴋɪ")));
            paste(location, Upgrade.OUTPOST_HUT.getSchemFile());
        }
    }

    /**
     * PLAYER METHODS
     **/

    public GuildHideout getHideOutByLocation(Location location) {
        if(guildHideoutEntries.containsValue(location)) {
            for (Map.Entry<GuildHideout, Location> entry: guildHideoutEntries.entrySet()) {
                if(entry.getValue().equals(location)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public void attemptTeleportToHideOut(Player player, GuildHideout guildHideout) throws HideOutDoesNotExistException, ClanDoesNotExistException, PlayerDoesNotBelongToClanException {

        if (guildHideout == null) throw new HideOutDoesNotExistException("Hideout does not exist");

        String clanTag = guildHideout.getClanTag();

        Clan clan = ClanAndUserDataService.getCachedClan(clanTag);

        if (clan == null) throw new ClanDoesNotExistException();

        if(!clan.getMembers().contains(player.getUniqueId()) && !player.isOp()) throw new PlayerDoesNotBelongToClanException();

        Location teleportLocation = guildHideout.getTeleportLocation();
        Bukkit.getScheduler().runTask(AdvancedGuilds.instance, () -> player.teleport(teleportLocation));

    }

    public void attemptTeleportToOutpost(Player player, GuildHideout guildHideout) throws HideOutDoesNotExistException, ClanDoesNotExistException, PlayerDoesNotBelongToClanException {

        if (guildHideout == null) throw new HideOutDoesNotExistException("Hideout does not exist");

        String clanTag = guildHideout.getClanTag();

        Clan clan = ClanAndUserDataService.getCachedClan(clanTag);

        if (clan == null) throw new ClanDoesNotExistException();

        if(!clan.getMembers().contains(player.getUniqueId()) && !player.isOp()) throw new PlayerDoesNotBelongToClanException();

        Location teleportLocation = guildHideout.getEntryBlockLocation();
        Bukkit.getScheduler().runTask(AdvancedGuilds.instance, () -> player.teleport(teleportLocation));

    }

    public void occupyHideOut(GuildHideout hideout, Clan clan) {

        hideout = resetOrCreateHideOut(hideout.getHideoutID());
        hideout.setClanTag(clan);

        clan.setHideoutId(hideout.getHideoutID());
        ClanAndUserDataService.updateClan(clan);

        updateHideOut(hideout);
        refreshHideoutOutpostHolograms(hideout, hideout.getEntryBlockLocation());
        try {
            refreshRegion(hideout);
        } catch (InvalidFlagFormat e) {
            throw new RuntimeException(e);
        }

    }


    public void updateHideOut(GuildHideout hideout) {
        try {
            guildHideoutEntityRepository.createOrUpdateEntity(hideout);
            guildHideouts.put(hideout.getHideoutID(), hideout);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<GuildHideout> loadAllHideOuts() {
        try {
            List<GuildHideout> allEntities = guildHideoutEntityRepository.getAllEntities();
            for(GuildHideout hideout : allEntities) {
                guildHideouts.put(hideout.getHideoutID(), hideout);
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

    public GuildHideout getHideOut(@NotNull String name) {
        return guildHideouts.get(name);
    }

    public boolean isHideoutOccupied(GuildHideout guildHideout) {

        if(guildHideout == null) return false;
        if(guildHideout.getClanTag() == null) return false;
        Clan clan = ClanAndUserDataService.getCachedClan(guildHideout.getClanTag());

        return clan != null;
    }

    public void upgradeHideOut(@NotNull GuildHideout hideout, Upgrade upgrade) {

        UpgradeInfoHolder upgradeHolder = hideout.getUpgradeHolder(upgrade);

        final int upgradeTimeSeconds = upgrade.getCoolDown();

        if(upgradeHolder == null){
            upgradeHolder = new UpgradeInfoHolder( (System.currentTimeMillis() + 1000L * upgradeTimeSeconds), true, upgrade);
        } else {
            upgradeHolder.setBought(true);
            upgradeHolder.setTimeOfCompletion(System.currentTimeMillis() + 1000L * upgradeTimeSeconds);
        }
        try {
            setHolograms(hideout);
        } catch (InvalidObjectException e) {
            throw new RuntimeException(e);
        }

        hideout.setUpgradeHolder(upgradeHolder);

        updateHideOut(hideout);

        scheduleUpgrade(hideout, upgrade);
    }

    private void scheduleUpgrade(GuildHideout hideout, Upgrade upgrade) {
        UpgradeInfoHolder upgradeHolder = hideout.getUpgradeHolder(upgrade);

        if(upgradeHolder.isFinished()) return;

        if(!upgradeHolder.isBought()) return;

        long timeToCompletionSeconds;

        try {
            timeToCompletionSeconds = upgradeHolder.getTimeToCompletionSeconds();
        } catch (UpgradeWasNotBoughtException e) {
            return;
        }

        if(timeToCompletionSeconds <= 0) {
            upgradeHolder.setBought(false);
            hideout.setUpgradeHolder(upgradeHolder);
            updateHideOut(hideout);
            return;
        }

        Clan clan = ClanAndUserDataService.getCachedClan(hideout.getClanTag());

        Countdown countdown = new Countdown(plugin, clan, Countdown.HologramType.GUILD_UPGRADE, Math.toIntExact(timeToCompletionSeconds)+1, upgrade.getHologramLocation(Bukkit.getWorld(hideout.getHideoutID())), () -> {

            if(clan != null) {
                for(UUID playerUUID : clan.getMembers()) {
                    Player player = Bukkit.getPlayer(playerUUID);

                    if(player == null || !player.isOnline()) continue;

                    MessageManager.sendMessageFormated(player, MessageManager.UPGRADE_READY.replace("{upgrade}", upgrade.getUpgradeName()), MessageType.CHAT);

                }
            }

            Location loc = new Location(Bukkit.getWorld(hideout.getHideoutID()), 0, 100, 0);
            paste(loc, upgrade.getSchemFile());

            upgradeHolder.setFinished();

            hideout.setUpgradeHolder(upgradeHolder);

            hideout.getUpgradeHolder(upgrade);
            updateHideOut(hideout);

            try {
                prepareHideOutHologramsAndNpcs(hideout);
            } catch (InvalidObjectException e) {
                throw new RuntimeException(e);
            }

        });
        countdown.scheduleTimer();
    }


    /** HOLOGRAMS **/

    public static final String RETURN_TO_OUTPOST = "return_to_outpost";
    public static final String HIDEOUT_PANEL = "hideout_panel";
    public static final String MAIN_STORAGE = "hideout_storage";
    public static final String HIDEOUT_TRADER = "hideout_trader";
    public static final String STATION_HALL = "hideout_station_hall";
    public static final String HIDEOUT_WITCH = "hideout_witch";
    public static final String HIDEOUT_TELEPORT = "hideout_teleport";
    public static final String ARENA_UPGRADE = "arena_upgrade";

    public static final String HIDEOUT_VENTURE = "hideout_venture";
    public static final String HIDEOUT_BOUGHT_VENTURE = "hideout_bought_venture";
    public static final String HIDEOUT_BLACKSMITH = "hideout_blacksmith";
    public static final String HIDEOUT_ENCHANTER = "hideout_enchanter";

    private static final NpcNameAndSkinPersistentDTO traderDTO;
    private static final NpcNameAndSkinPersistentDTO witchDTO;
    private static final NpcNameAndSkinPersistentDTO teleportDTO;
    private static final NpcNameAndSkinPersistentDTO blacksmithDTO;
    private static final NpcNameAndSkinPersistentDTO ventureDTO;
    private static final NpcNameAndSkinPersistentDTO sorcererDTO;

    private static final NpcNameAndSkinPersistentDTO blackMarketDealerDTO;
    private static final NpcNameAndSkinPersistentDTO bookMakerDTO;
    private static final NpcNameAndSkinPersistentDTO arenaMasterDTO;
    private static final NpcNameAndSkinPersistentDTO spawnArenaNpcDTO;

    static {
        traderDTO = new NpcNameAndSkinPersistentDTO("&#098A8A&lʜ&#19AAA5&lᴀ&#28C9C1&lɴ&#38E9DC&lᴅ&#64E9DF&lʟ&#91E9E3&lᴀ&#BDE9E6&lʀ&#E9E9E9&lᴢ", "ddd87d9e", "irpVrcqKWn35YcwRTt4B1+S/fvXtnUlj121Lu6uCRTK6AdAaPapfHuWkxL5v49RwuP0rqZA/d8HK+bpLtLGL9q20SNff0W2mpJu9gj4xn5hv6v1mFmSuobpfrNuTYHfZcHUla2qiEPYdv56z6fxw/CGMyqwCc9MIj+PCB82BwtR0+t049zeFJjV1cJJcZSpKerA9nN5uzfsIcLpYpbOnL/16VmNZIY6lWnwQ8cTmbuYcwLekBs2F59ackBTUH3SHxOjDlYBCYPTiULvsnZjfZjKeal9O1ckp+gEWvy83Z6cTYuegIAlt/B4tFfzP0CQbTA2qEKip++AWHIEszcvXU3m9uhHFSoKlLdW5ukBGWzIyQWuYl8RCn/hYjZEOr0Lr/tUN6Qcz+6QLNGFYssm15QnA1xMzTA5XoWtQ6ACeoyz0b0x0WZcN1H53gYN3s39Be75QEIKz+QxlQlPl64PMFLPfCT44c4Y6VQnFVEE7GNvnQNDHxIjlt2GY9oQfUK+TVxMK+kbcqBLkl+QyZGT8dz+3fZvL5impI/cRfYmfvpDtg51InxEJahMQN1cc4HxtsIpVbf4uZScpBWu0NM64iPyEMSnVWKDqjx3VlWns4kDScJ79Mv57f7sdbORbzZDIGAU/6B2gsBBs9FL+O43uv/HHJULWx44tPK4e+cHO0PU=","ewogICJ0aW1lc3RhbXAiIDogMTY0MjY5Mzk5OTM2MywKICAicHJvZmlsZUlkIiA6ICJhYTZhNzUwNWVkYmU0NjNiYjk1NWYyMWY0MjNiYTM1NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJub3RhbmR5d2FyaG9sIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2FiMDQxMTQ1NjhkMmI5NzczYjI5OTc5YWQ1NzZkM2M4YmY1Yjg3NDA1NGE0YjQyMjFjOGJiY2M3YTgyMDMwMmIiCiAgICB9CiAgfQp9");
        witchDTO = new NpcNameAndSkinPersistentDTO("&#7B1EBE&lᴡ&#802CB6&lɪ&#8539AF&lᴇ&#8A47A7&lᴅ&#8E559F&lź&#936298&lᴍ&#987090&lᴀ", "", "", "");
        ventureDTO = new NpcNameAndSkinPersistentDTO("&#004A6E&lᴋ&#004D75&lᴀ&#00507D&lᴘ&#005284&lɪ&#00558C&lᴛ&#005893&lᴀ&#005B9A&lɴ &#0060A9&lᴇ&#0063B1&lᴋ&#0066B8&lꜱ&#0068C0&lᴘ&#006BC7&lᴇ&#006ECE&lᴅ&#0071D6&lʏ&#0073DD&lᴄ&#0076E5&lᴊ&#0079EC&lɪ", "524e7255", "Fy84Vb/SljdDgkX3Jh0ZyMyE/9gpkkoM5S/2J1472oRAanKrpFGLeB9SGER2x/hOMFXaTqTePbBnuANWF849PKT9Hp6js61M/GUU2gY8YQ5lvsgRlIUianH0zH/4Zf9eI2VTTfjk020Af4DMnpqXTQWmTMyfiSFs9na8iQi0ECmg0u4e8/cu4fydWJwVh56GdgIL5PXdYpv8DZWWoA6VrnuYHunqKlGaxnWNV1EJfZC5LctjJJky2yhCmeSMBZX1C4aX3woEzaZPCuSRrmwEWRngSf1G7aPD7JxrPIZMLuwk87pr4gyaKaf6qBtvk8ZAlem1TmryvLHskUunXURV7OA6KFWkDgLx7dyigzMaiJ4RlA5u0iZnEMx3PfTnGLaJ7XY/UNM0qm7Glxd+K97R8XJw+RMERswgVgurcNgamhFIFfTpFatjJN+alnjb3tIa2b166NrmxDHQXeTlXmA6Lbm2/ZlMneY59b6LftdASzNZ9Qo1GuMzOwv0RRdilrzn/hIhetOMQixL0RWV5sO+yg8w5zLRfNwy4wiZPUrKVUNo0W3hqDqEgE4fJbEIIxNeGupmpl6rhZxVWTMm/RNyP5rFWY25qsPU/vVoBY+wkDu//UbBVP1l5sVC6sKdOBMDFv9F8aD29ArxEInra1awIAulswSrZ7SDIttAQTKjIpQ=", "eyJ0aW1lc3RhbXAiOjE1ODcyNzU5OTc2OTEsInByb2ZpbGVJZCI6IjdkYTJhYjNhOTNjYTQ4ZWU4MzA0OGFmYzNiODBlNjhlIiwicHJvZmlsZU5hbWUiOiJHb2xkYXBmZWwiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2NiMjY5ZmJlODRjNWEwZDEzMjJjYmUzOWI4ZDgyNWM4ZWUzODMzZDZlNjUwOGJiY2ExZjdlZTdkYzU1NjZiZTkifX19");
        blacksmithDTO = new NpcNameAndSkinPersistentDTO("&#454545&lᴋ&#5C5C5C&lᴏ&#747474&lᴡ&#8B8B8B&lᴀ&#A2A2A2&lʟ", "007c59ee", "FgjyTFUENg2Ig1t8Q7Xk5XRT2OjwgtmZ5hYCSCqFDJWA6WSiGLnhsY83/izw/4wvUII/51mgtuPceMM2VkD4Yzl8nAACTUmhMXCMm56Xn5SPUQn8K7lVM0w+IZbAXW30QEC7O9tZ7k4lQmIj+KnEkpduDtGU9pEUSbT+9EQtgeuXXoPZIFvxB6l7Aq6nEs5fcQheUBGvGf8DqjkkP/ZKn281ZFTXc9nTU3BQpOFcqi7V5K0s+At0eNxEm+dT07VBHtsHtnrXFy44vh1af4UBiH2FU74U1hZmJIqFKPVeWQLjc8psrGwIBWyL8FuFuxGwpZWMtbpB1c6VGeePozxNefiimHpBaqVww4iaCF9Hn98YJOXJzz9LxzYAMi/NQ9sncFdBdC3GJvdr745YMqRxL8ASNrzYAas6qd9kqy8O9NqaNBYve+eVYVb6vPZzUkA+MEX4mbWkGWfZyB1tLUMHFioSikK6v3rkWxu6arPHCMOcjb720mb/QT6kjGLr/lmOWw9tuKOYrtwg6POcTCuvLr5zYS/PI0YSUw2n8D4w/2ZjBD6uzOhbn11p6kIdL8JcPV8+gttVi0sulJ9KO+WRE+iK1HyCDmPCAVM27bDNSQpFvD/Zk+vFGZZSpALiq2XktoByhMul0T52Tfbw96x3Vn9225X64qkVRdB+LYmxxyU=", "ewogICJ0aW1lc3RhbXAiIDogMTU5NzQ1MjgwNjU5NiwKICAicHJvZmlsZUlkIiA6ICJkZTU3MWExMDJjYjg0ODgwOGZlN2M5ZjQ0OTZlY2RhZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNSEZfTWluZXNraW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU4ZDBkYjQ2MTcxMWYwYWE3YjVkYThiNzQyN2M5YmM2OThhODVjYzBmYzdjZWQ1MjFmNGE5MmJjMWFkMWRmMSIKICAgIH0KICB9Cn0=");
        teleportDTO = new NpcNameAndSkinPersistentDTO("&#4F2000&lᴘ&#512607&lʀ&#542C0F&lᴢ&#563216&lᴇ&#58381D&lᴡ&#5B3D25&lᴏ&#5D432C&lź&#5F4933&lɴ&#624F3B&lɪ&#645542&lᴋ", "498afcab", "yTyaTlyHbZruHT0/06fCHEfwtf7QRmbQAOFeSmqQ5QFWKikrwTXEFq7+62pBM9aFmDzGVNIZ2b5wNd9ZVxKNhkfjKck9SHl3UiI5AvQNfbp6OXNNPeKArdLYvcgRzcddTbHe2yKVRpFVG4UUysCnl1oyk3frWAKTbUBaUwlWDg9HnAeXIVoaXqlNI6stdPwbrUczW/zh6Nb//+HHCEipbdGbU35EB0O3Vp6AlzeV3jn5wT/j8kItQU/m27TwtPA+6Urx8ypzNuTMmUipEMhfnRayqxgPEnssP82Nk02b7yno5vBtLjOU0O7JWLwwTkC2bE6OKXEu46Ul9Vuqwj0OrJDvC9LYn+t74spINNqV1pWCx5z7/LUO2NAyjyoxUjIbRO3NK/BnhtXAycOoKa0Rx6EThcgPeEGiLDaAOu5WNM/BMqYqmTqc9BNdZzzEwKv0Mbyth2XkRuw6sBdKijN+TnegQh1/PkD26+xEscvuC7dWKK0RLD1FxZKhI6j0dNhvq7aEKYuEV7nAvnZyOhrmPGJ4t3G6hJ8uTwxlRORatQUJ7CHlvw3U/Ee/JfdIBjxhDdEcqkoBEqPIP9Dr6ZNqu/06HRGgroDqES3kdTRQTkBWkrmgLShi23V/YE0HKZUdrKe9TfEFXFekSkmoSHlAjrP73b7Torjrk8kNVf1D++8=","eyJ0aW1lc3RhbXAiOjE1ODcxNzI2NTc2NTcsInByb2ZpbGVJZCI6ImIwZDczMmZlMDBmNzQwN2U5ZTdmNzQ2MzAxY2Q5OGNhIiwicHJvZmlsZU5hbWUiOiJPUHBscyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU4NWRhNjA2Yzc5NTZmNmE4YjI0NzRjYTM5N2UwZTFlMTg1NTc0YjYyMmY5YTJlNzFlZjIzOWI4NDliNDVhYyIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19");
        sorcererDTO = new NpcNameAndSkinPersistentDTO("&#002A6B&lᴢ&#122E6F&lᴀ&#243273&lᴋ&#363677&lʟ&#493B7C&lɪ&#5B3F80&lɴ&#6D4384&lᴀ&#7F4788&lᴄ&#914B8C&lᴢ", "e7a8bb24", "Ob1xZlOiHj/BtWh+Vmpf4nkg0rYMuNw7YpgWzln3uLiYIg78eEgfz05AbzzYUghvVEwh5yXXKUTSgvBDo6MkoaOO7+rnUPXfZ1edhVWbQX5tJQHzNDO1nBZXlO0K7JO3xPdf1DyepryNpYFtl7x+HO+02n1hNXzuKA/RE6PbuVmpWtVDSyfzdCpbXi5fXMSCGoZb3Z4rvPewDk2qjBcurne0mRHyxGlTkYu7HuVITbcIgdDpKSrmCKfjZCLG9QK0+lLMPSJBlIw5fLEINXrFH6AfkjuszEenjugGbrcp2MtaDC568cE62Lwrwb5JepbnD32jkB+k/4eKcbNsBc7dSZ3WpL+r2DpI7WU0uNbqVe0QwBs9XMFJkTaIeWsxm1zKBAuj5wux1aimHRcCJJ3xQP1NOZscJqBZejMdZ5sscXebRGLhBhxNYSoqaKu+f93BqKHLraOBS+nj3YG2w/4/yGaLQEIPfuHoC8VBVMGwezAcEvmV66gQVSS6UmN0edWsmFVUMjPZcHYUAMtK8MZ8d4zaDWI95dIh55MI2JFhqr31qkUmfc2+aTy0dgIJ2G0QmUc23yYXwYqHvfditKIm1Co75ZB4OYqEYmJHtsRyaO0t3SCUdoWiMes+ElYiKQJB68CWV606kPpvAiF1lFDqz198/ClnrPOu99vEPSH51ls=","ewogICJ0aW1lc3RhbXAiIDogMTU4ODE5MjY4MTMwNSwKICAicHJvZmlsZUlkIiA6ICIzZmM3ZmRmOTM5NjM0YzQxOTExOTliYTNmN2NjM2ZlZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJZZWxlaGEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWU0OTE0YzQ4YmU2YTVhNzEyYWIwMWJhZGE2Nzk3Yjc1NjYyNzM4OWNkM2ZmOWJlOTJmMWY4M2I3MGE0ZTVkNiIKICAgIH0KICB9Cn0=");

        bookMakerDTO = new NpcNameAndSkinPersistentDTO("&#806A31&lʙ&#8A7843&lᴜ&#948555&lᴋ&#9E9366&lᴍ&#A8A078&lᴀ&#B1AE8A&lᴄ&#BBBB9C&lʜ&#C5C9AD&lᴇ&#CFD6BF&lʀ", "37667219", "tNlbx/6xJz98zl9hMAaFGE8LRT89ylpJrmvXy7IceDdm6LuJdWIpQNZp/5sDzTY2U90oNLlRD4OH+vJvwyr8/a3PfD+8z7RElFMtzNKKJTjtwshdcKu49TQTHnHFfWtbOoTkkLEh4Rwkear4bZT7VZdLYGNxKJTqQNk3soKT57R26YaZfFvXI6as0lqD6ZK6Xh/+KydVpWoncqsWsSHbxDoaQWgA52Io/OjNs8e+l3Kx+LdhxyS0P8ybdNZZ6YzRRGpJTCKI7frMhB+DsOkANjFaLL70IU4Wfs6R5awjCPcw6dEsDymHBChcDKf/sp+WKnevFPHS3E62i5OFyTxr/jp3unVKD/HFF3B1unSWGUVz+p8pAZlmZ7/BEGyPSzDy6E0shTUkOrDDwP5JOsAFry7v4scyUbt7FjVadWvi+J3+2qiEdXUw4MUwqJjH7IuVI7J0tGuzFx/7yOaAQw+Rv9FdXz2NA1uynVmh3DBcTKCacSqt/pUGKEAslowE4FmGdoQe3u8CrBU7X4FxRVAJDlkrR7cFeKSm/6cYv3z89pRtBe/xt+eCQwWGnC2wVyXjMICLWsppGM8KRVzC3GbW9P/IJ80/y+hXHE1/DavMGOUkd9qJ9oG6DTPCRVnmhq1CubojBqMWzDoxHkYkL5kuqf6ce5JHQxCTvAbLAxGAszI=", "eyJ0aW1lc3RhbXAiOjE1ODU1MjQxMDk4NDQsInByb2ZpbGVJZCI6ImRlNTcxYTEwMmNiODQ4ODA4ZmU3YzlmNDQ5NmVjZGFkIiwicHJvZmlsZU5hbWUiOiJNSEZfTWluZXNraW4iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzFlNjg3MGNlMjk0MDcxM2M1NzQwY2NkODhjMWNlM2YyNWQzYTRjY2NlODVmNGVmNmE1ZDc4Y2U4NmExNWJjMzkifX19");
        blackMarketDealerDTO = new NpcNameAndSkinPersistentDTO("&#4A4A4A&lᴅ&#5C5C5C&lɪ&#6E6E6E&lʟ&#7F7F7F&lᴇ&#919191&lʀ", "64c46b20", "eEQ7IViA3+UtVRQbbGzZ+EP/vguj5dzcP1HUzeJEKgu5vHvj7HFxkSwHQkSWMXFKpxyYoxU0LnlYoZe2Ng15M1TNbxnYB8Y28afXOUEbOBrHlY0X/D+C9/iZ1Rdb1lpdk/QYJtER2NDxyUFJsGESKUVQA5JSvVaeOCGRSl4CfDzkQe7NuWlRpSUbHpDQsWJ9lwxk3dSwYqRF1cbuZtkgotDYvtdQ1FEYEoRaJr5FDXfLeGr6bkXPQEPxnFeVFhBZizVC7ego8yhmP89m3M7z8frempeG2P/kqZY0dPdlgdRd+WdwhL8KX+Y9GLlndEcGwclMHgb0J9E8U5PQ32e6MY8cusxiRSpguEcTdlWvtZTR7x8gRoGA6Lsky7QrCC9HbkhZ2KTRkivcitBmIvxVPNlWmEL72UV9aTiXwmljXD14NCZF9bGYW5fBMTLPoazfZK7a5c7AXtn7B2ZZ1NvYnHA6bMg4lmWgDRMes+Zq26GHWRMRpoETO780sAxqtcZIo4t5tZPm+DfDbtpDrm4VKQyjjZ3s42tyAWU1UL/YidB/rcGO4T7R4pRyzNVaPvmtMKRhudaxaUooPHIOAPKVhx9tfdtPGDb8L7GtHxKpLCbp5+gQJ9WvomAneAcTU2YbM6/IPLxDa0cXcf4I8Z4peALJUFVPt7crRs12FBPm8TY=", "ewogICJ0aW1lc3RhbXAiIDogMTcyODIzNjcwMzEwNiwKICAicHJvZmlsZUlkIiA6ICJjMjVlMWMxZTE1YTQ0N2IwOTQ2Zjg2YzYzYzhjYjZkOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJiaWdpYm9zMzIxIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2VmNWQ5ZjEwYzJhZmIyN2I1YjVmYWIwMzFhZWMyMjcwZGYxMTUyY2NhYzdiZDhjMTA5YTJmZTFkNmIxMzQwMTkiCiAgICB9CiAgfQp9");
        arenaMasterDTO = new NpcNameAndSkinPersistentDTO("&#FA0000&lᴍ&#ED0000&lɪ&#DF0000&lꜱ&#D20000&lᴛ&#C40000&lʀ&#B70000&lᴢ &#9C0000&lᴀ&#8E0000&lʀ&#810000&lᴇ&#730000&lɴ&#660000&lʏ", "338f8012", "e/6VycZ9E6BIJzir553LdxFgnfIo7qSLkk3TJ4q+tfiN0cArWBNbnBehol3WYOHkuQzZCgsul2LMujk7PtsTsEyyRvnMmoWdX22xQqyAKLLoh/MoXNcxkWFbOgfS9209gFjDr+tse1FDmgnXhtpTli4ZZXl9V2wH+HdtTuvUcCOSTMlAfW8bpei4dZU9XTsznZQngdZRFJ4q3XGaiG+hNJGvUYEpVVxr8ASMgukVrO516uO6etdlCZOWOZty0RBysUwBw92jRK43nl1Ns+sc2789Pv16QXqdS/MXIa6LN77fZXkRd/G8+byHzH65IAw8MYxL0Yzokwvb7Lqij3wlDFylXbuZVL3iAuBNhlrkwVuOBVIctqGXhur3u7nEfEFvqnGA/qc58+4xGF+d3bIncE+R7w95gSK1tIJMMQV8WJCbUNME2SVjubf4wxUnzPQp6LGYJFy2a5werDMcw5vU6XAqdAnqzw89TSrB99ksp+tIvy89Er7A+A4W5lcc7zy7euZRPX+p9pLRh4/tJyNQLFoPEIHhtJ51BNfoLvQh2sly52y9XceqYfzeDXadpbwBAgvAGaoFuPa4BpT9ZBwuzZWK0UlopgI2erO1EoXVCJPYKMfbcYWZ8/z8UfRJbuks7n6OZUhpud6yvO6+gRTW98G/uF1S22lnJhMuaAQp3Yw=", "ewogICJ0aW1lc3RhbXAiIDogMTY2OTg3NjM3OTAxMywKICAicHJvZmlsZUlkIiA6ICI3YzI2YTAxY2U4NjU0NDkzOTA3NzA2OGQxZTA5ZjE5MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJodG93ZXI4IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzM2MTRhMjY4NWRjZTRlNTkyMWY2MmM2ZDk2ZjlmMjRlNTk5NjI4YzliNTU4NWUyZTllOGM0YzQ5MTU5NTU0ZmYiCiAgICB9CiAgfQp9");
        spawnArenaNpcDTO = new NpcNameAndSkinPersistentDTO("&#4F2000&lᴘ&#512607&lʀ&#542C0F&lᴢ&#563216&lᴇ&#58381D&lᴡ&#5B3D25&lᴏ&#5D432C&lź&#5F4933&lɴ&#624F3B&lɪ&#645542&lᴋ", "498afcab", "yTyaTlyHbZruHT0/06fCHEfwtf7QRmbQAOFeSmqQ5QFWKikrwTXEFq7+62pBM9aFmDzGVNIZ2b5wNd9ZVxKNhkfjKck9SHl3UiI5AvQNfbp6OXNNPeKArdLYvcgRzcddTbHe2yKVRpFVG4UUysCnl1oyk3frWAKTbUBaUwlWDg9HnAeXIVoaXqlNI6stdPwbrUczW/zh6Nb//+HHCEipbdGbU35EB0O3Vp6AlzeV3jn5wT/j8kItQU/m27TwtPA+6Urx8ypzNuTMmUipEMhfnRayqxgPEnssP82Nk02b7yno5vBtLjOU0O7JWLwwTkC2bE6OKXEu46Ul9Vuqwj0OrJDvC9LYn+t74spINNqV1pWCx5z7/LUO2NAyjyoxUjIbRO3NK/BnhtXAycOoKa0Rx6EThcgPeEGiLDaAOu5WNM/BMqYqmTqc9BNdZzzEwKv0Mbyth2XkRuw6sBdKijN+TnegQh1/PkD26+xEscvuC7dWKK0RLD1FxZKhI6j0dNhvq7aEKYuEV7nAvnZyOhrmPGJ4t3G6hJ8uTwxlRORatQUJ7CHlvw3U/Ee/JfdIBjxhDdEcqkoBEqPIP9Dr6ZNqu/06HRGgroDqES3kdTRQTkBWkrmgLShi23V/YE0HKZUdrKe9TfEFXFekSkmoSHlAjrP73b7Torjrk8kNVf1D++8=","eyJ0aW1lc3RhbXAiOjE1ODcxNzI2NTc2NTcsInByb2ZpbGVJZCI6ImIwZDczMmZlMDBmNzQwN2U5ZTdmNzQ2MzAxY2Q5OGNhIiwicHJvZmlsZU5hbWUiOiJPUHBscyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU4NWRhNjA2Yzc5NTZmNmE4YjI0NzRjYTM5N2UwZTFlMTg1NTc0YjYyMmY5YTJlNzFlZjIzOWI4NDliNDVhYyIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19");
    }

    public void resetNpcsForHideOut(GuildHideout guildHideout) {

        NPCRegistry NPC_REGISTRY = CitizensAPI.getNamedNPCRegistry(guildHideout.getHideoutID() + "hideout_npc_registry");

        if(NPC_REGISTRY == null) {
            NPC_REGISTRY = CitizensAPI.createNamedNPCRegistry(guildHideout.getHideoutID() + "hideout_npc_registry", new MemoryNPCDataStore());
        }

        NPC_REGISTRY.despawnNPCs(DespawnReason.PENDING_RESPAWN);
        NPC_REGISTRY.deregisterAll();

        HashMap<NpcNameAndSkinPersistentDTO, NPC> npcHashMap = new HashMap<>();

        int id = 1;
        for (NpcNameAndSkinPersistentDTO dto : Arrays.asList(traderDTO, witchDTO, ventureDTO, blacksmithDTO, sorcererDTO, teleportDTO, blackMarketDealerDTO, spawnArenaNpcDTO, bookMakerDTO, arenaMasterDTO)) {

            EntityType type = EntityType.PLAYER;
            if(dto.equals(witchDTO)) type = EntityType.WITCH;

            NPC npc = NPC_REGISTRY.createNPC(type, UUID.randomUUID(), id, dto.npcName());

            if(!dto.equals(witchDTO)) npc.getOrAddTrait(SkinTrait.class).setSkinPersistent(dto.skinName(), dto.skinSignature(), dto.skinValue());

            npcHashMap.put(dto, npc);


            npc.data().setPersistent(NPC.Metadata.ALWAYS_USE_NAME_HOLOGRAM, true);
            npc.data().setPersistent(NPC.Metadata.AMBIENT_SOUND, "null");
            npc.data().setPersistent(NPC.Metadata.DEATH_SOUND, "null");
            npc.data().setPersistent(NPC.Metadata.HURT_SOUND, "null");

            npc.getOrAddTrait(LookClose.class).lookClose(true);
            npc.getOrAddTrait(LookClose.class).setRange(35);
            npc.scheduleUpdate(NPC.NPCUpdate.PACKET);
            npc.setUseMinecraftAI(false);
            npc.setProtected(true);

            id++;
        }
        npcHashMap.get(traderDTO).spawn(new Location(Bukkit.getWorld(guildHideout.getHideoutID()), -7.5 , 97, -48.5));
        npcHashMap.get(witchDTO).spawn(new Location(Bukkit.getWorld(guildHideout.getHideoutID()), 6.5 , 97, -49.5));

        if(guildHideout.hasFinishedUpgrade(Upgrade.BLACKSMITH)) {
            npcHashMap.get(blacksmithDTO).spawn(new Location(Bukkit.getWorld(guildHideout.getHideoutID()), 26.5 , 93, -50.5));
        }

        if(guildHideout.hasFinishedUpgrade(Upgrade.VENTURE)) {
            npcHashMap.get(ventureDTO).spawn(new Location(Bukkit.getWorld(guildHideout.getHideoutID()), 34.5 , 93, -38.5));
        }

        if(guildHideout.hasFinishedUpgrade(Upgrade.SORCERER)) {
            npcHashMap.get(sorcererDTO).spawn(new Location(Bukkit.getWorld(guildHideout.getHideoutID()), 25.5 , 93, -37.5));
        }

        npcHashMap.get(teleportDTO).spawn(new Location(Bukkit.getWorld(guildHideout.getHideoutID()), -8.5 , 99, -10.5));

        if(guildHideout.hasFinishedUpgrade(Upgrade.ILLEGAL_BUSINESS_HALL)) {
            npcHashMap.get(blackMarketDealerDTO).spawn(new Location(Bukkit.getWorld(guildHideout.getHideoutID()), 8.5 , 92, -63.5));
            npcHashMap.get(spawnArenaNpcDTO).spawn(new Location(Bukkit.getWorld(guildHideout.getHideoutID()), 7.5 , 91, -76.5));
            npcHashMap.get(bookMakerDTO).spawn(new Location(Bukkit.getWorld(guildHideout.getHideoutID()), -13.5 , 92, -85.5));
            npcHashMap.get(arenaMasterDTO).spawn(new Location(Bukkit.getWorld(guildHideout.getHideoutID()), 11 , 92, -86.5));
        }
    }

    public GuildHideout getHideoutByLocation(Location location) {
        return getHideOutByLocation(location);
    }


    private void setHolograms(@NotNull GuildHideout guildHideout) throws InvalidObjectException {

        final String returnToOutpost = guildHideout.getHideoutID() + RETURN_TO_OUTPOST;
        final String hideoutPanel = guildHideout.getHideoutID() + HIDEOUT_PANEL;
        final String mainStorage = guildHideout.getHideoutID() + MAIN_STORAGE;
        final String hideoutTrader = guildHideout.getHideoutID() + HIDEOUT_TRADER;
        final String stationHall = guildHideout.getHideoutID() + STATION_HALL;
        final String witch = guildHideout.getHideoutID() + HIDEOUT_WITCH;
        final String teleport = guildHideout.getHideoutID() + HIDEOUT_TELEPORT;

        final String arenaUpgrade = guildHideout.getHideoutID() + ARENA_UPGRADE;
        final String blackMarket = guildHideout.getHideoutID() + ARENA_UPGRADE + "_black_market";
        final String bookMaker = guildHideout.getHideoutID() + ARENA_UPGRADE + "_book_maker";
        final String arenaMaster = guildHideout.getHideoutID() + ARENA_UPGRADE + "_arena_master";

        final String boughtVenture = guildHideout.getHideoutID() + HIDEOUT_BOUGHT_VENTURE;
        final String hideoutVenture = guildHideout.getHideoutID() + HIDEOUT_VENTURE;
        final String hideoutBlackSmith = guildHideout.getHideoutID() + HIDEOUT_BLACKSMITH;
        final String hideoutEnchanter = guildHideout.getHideoutID() + HIDEOUT_ENCHANTER;

        DHAPI.removeHologram(returnToOutpost);
        DHAPI.removeHologram(hideoutPanel);
        DHAPI.removeHologram(mainStorage);
        DHAPI.removeHologram(hideoutTrader);
        DHAPI.removeHologram(stationHall);
        DHAPI.removeHologram(witch);
        DHAPI.removeHologram(teleport);

        DHAPI.removeHologram(arenaUpgrade);
        DHAPI.removeHologram(blackMarket);
        DHAPI.removeHologram(bookMaker);
        DHAPI.removeHologram(arenaMaster);

        DHAPI.removeHologram(boughtVenture);
        DHAPI.removeHologram(hideoutVenture);
        DHAPI.removeHologram(hideoutBlackSmith);
        DHAPI.removeHologram(hideoutEnchanter);

        World world = Bukkit.getWorld(guildHideout.getHideoutID());

        if(world == null) throw new InvalidObjectException("World " + guildHideout.getHideoutID() + " does not exist");

        DHAPI.createHologram(returnToOutpost, new Location(world, 0.5, 102.5, 0.5), Arrays.asList(ColorFixer.addColors("#b59651&lᴅᴏ ɢóʀʏ"), ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ, ᴡᴊᴇᴄʜᴀć ᴡɪɴᴅą ɴᴀ ɢóʀᴇ")));
        DHAPI.createHologram(hideoutPanel, new Location(world, -8.5 , 100.5, -24.5), Arrays.asList(ColorFixer.addColors("#b59651&lᴘᴀɴᴇʟ ɢɪʟᴅʏᴊɴʏ"), ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ, ᴀʙʏ ᴜʟᴇᴘꜱᴢʏć ᴋʀʏᴊóᴡᴋę")));
        DHAPI.createHologram(mainStorage, new Location(world, 0.5 , 100, -43.5), Arrays.asList(ColorFixer.addColors("#b59651&lᴍᴀɢᴀᴢʏɴ ɢɪʟᴅʏᴊɴʏ"), ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ, ᴀʙʏ ᴏᴛᴡᴏʀᴢʏᴄ́ ᴍᴀɢᴀᴢʏɴ ɢɪʟᴅʏᴊɴʏ")));
        DHAPI.createHologram(hideoutTrader, new Location(world, -5.5 , 100.5, -47), Arrays.asList(ColorFixer.addColors("#47807c&lʜᴀɴᴅʟᴀʀᴢ ɢɪʟᴅʏᴊɴʏ"), ColorFixer.addColors("&7ᴋʟɪɴɪᴊ, ᴀʙʏ ᴏᴛᴡᴏʀᴢʏć ꜱᴋʟᴇᴘ ɢɪʟᴅʏᴊɴʏ")));
        DHAPI.createHologram(witch, new Location(world, 5.5 , 100.5, -48.5), Arrays.asList(ColorFixer.addColors("#7830ab&lᴡɪᴇᴅźᴍᴀ ɢɪʟᴅʏᴊɴᴀ"), ColorFixer.addColors("&7ᴋʟɪɴɪᴊ, ᴀʙʏ ᴜᴢᴜᴘᴇᴌɴɪć ᴍɪᴋꜱᴛᴜʀʏ")));
        DHAPI.createHologram(teleport, new Location(world, -7.5 , 102.5, -10.0), Arrays.asList(ColorFixer.addColors("#35543b&lᴡᴏźɴɪᴄᴀ ɢɪʟᴅʏᴊɴʏ"), ColorFixer.addColors("&7ᴋʟɪɴɪᴊ, ᴀʙʏ ᴏᴛᴡᴏʀᴢʏć ᴍᴇɴᴜ ᴍᴏżʟɪᴡʏᴄʜ ᴅᴇꜱᴛʏɴᴀᴄᴊɪ")));

        if(!guildHideout.hasBoughtUpgrade(Upgrade.STATION_HALL)) {
            DHAPI.createHologram(stationHall, new Location(world, 12.5 , 99.5, -43.5), Arrays.asList(ColorFixer.addColors("#61edba&lʀᴏᴢʙᴜᴅᴏᴡᴀ ᴋʀʏᴊóᴡᴋɪ #b59651#1"), "&7ᴀʙʏ ᴛᴀᴍ ᴘʀᴢᴇᴊść ᴋᴜᴘ ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴡ ᴘᴀɴᴇʟᴜ"));

        } else {

            if(guildHideout.hasBoughtUpgrade(Upgrade.VENTURE)) {
                DHAPI.createHologram(boughtVenture, new Location(world, 38 , 96, -41), Arrays.asList(ColorFixer.addColors("#b59651&lᴇᴋꜱᴘᴇᴅʏᴄᴊᴀ"), "%advancedguilds_expedition_objective_and_level%", "%advancedguilds_expedition_time_left_literal%", "%advancedguilds_expedition_time_left%", "%advancedguilds_expedition_speed_up%"));
            } else {
                DHAPI.createHologram(hideoutVenture, new Location(world, 22.5 , 95.5, -41.5), Arrays.asList(ColorFixer.addColors("&lEkspedycje"), "&7ᴛᴜ ᴡʏśʟᴇꜱᴢ ᴇᴋꜱᴘᴇᴅʏᴄᴊᴇ ᴘᴏ ꜱᴜʀᴏᴡᴄᴇ"));
            }

            if(!guildHideout.hasBoughtUpgrade(Upgrade.BLACKSMITH)) {
                DHAPI.createHologram(hideoutBlackSmith, new Location(world, 22.5 , 9.5, -45.5), Arrays.asList(ColorFixer.addColors("&lKOWAL"), "&7ᴛᴜ ᴡʏꜱᴛᴀᴡɪꜱᴢ ᴘʀᴢᴇᴅᴍɪᴏᴛʏ ɴᴀ ᴀᴜᴋᴄᴊᴇ"));
            }

            if(!guildHideout.hasBoughtUpgrade(Upgrade.SORCERER)) {
                DHAPI.createHologram(hideoutEnchanter, new Location(world, 22.5 , 95.5, -43.5), Arrays.asList(ColorFixer.addColors("&lZAKLINACZ"), "&7ᴛᴜ ᴡʏꜱᴛᴀᴡɪꜱᴢ ᴘʀᴢᴇᴅᴍɪᴏᴛʏ ɴᴀ ᴀᴜᴋᴄᴊᴇ"));
            }



        }

        if(!guildHideout.hasBoughtUpgrade(Upgrade.ILLEGAL_BUSINESS_HALL)) {
            DHAPI.createHologram(arenaUpgrade, new Location(world, 0.5 , 99.5, -51), Arrays.asList(ColorFixer.addColors("#61edba&lʀᴏᴢʙᴜᴅᴏᴡᴀ ᴋʀʏᴊóᴡᴋɪ #b59651#2"), "&7ᴀʙʏ ᴛᴀᴍ ᴘʀᴢᴇᴊść ᴋᴜᴘ ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴡ ᴘᴀɴᴇʟᴜ"));
        } else {
            DHAPI.createHologram(blackMarket, new Location(world, 6.5 , 95.5, -66.5), Arrays.asList(ColorFixer.addColors("#383734&lᴄᴢᴀʀɴʏ ʀʏɴᴇᴋ"), "&7ᴛᴜ ᴡʏꜱᴛᴀᴡɪꜱᴢ ᴘʀᴢᴇᴅᴍɪᴏᴛʏ ɴᴀ ᴀᴜᴋᴄᴊᴇ"));
            DHAPI.createHologram(arenaMaster, new Location(world, 9.5 , 95.5, -86.5), Arrays.asList(ColorFixer.addColors("#7a001b&lᴍɪꜱᴛʀᴢ ᴀʀᴇɴʏ"), "&7ᴛᴜ ᴍᴏżᴇꜱᴢ ᴢᴀᴘʟᴀɴᴏᴡᴀć ɪ ᴅᴏᴌąᴄᴢʏć ᴅᴏ ᴛᴜʀɴɪᴇᴊᴜ"));
            DHAPI.createHologram(bookMaker, new Location(world, -11.5 , 95.5, -85.5), Arrays.asList(ColorFixer.addColors("#bf8c00&lʙᴜᴋᴍᴀᴄʜᴇʀ"), "&7ᴛᴜ ᴍᴏżᴇꜱᴢ ᴏʙꜱᴛᴀᴡɪć ᴡᴀʟᴋɪ"));
        }

    }

    private void prepareHideOutHologramsAndNpcs(@NotNull GuildHideout guildHideout) throws InvalidObjectException {
        setHolograms(guildHideout);
        resetNpcsForHideOut(guildHideout);
    }


    public void loadHideOuts() {
        List<GuildHideout> guildHideouts = loadAllHideOuts();

        for (GuildHideout guildHideout : guildHideouts) {
            HashMap<Upgrade, UpgradeInfoHolder> hideoutUpgrades = guildHideout.getHideoutUpgrades();

            for (Map.Entry<Upgrade, UpgradeInfoHolder> entry : hideoutUpgrades.entrySet()) {
                Upgrade upgrade = entry.getKey();
                UpgradeInfoHolder upgradeInfoHolder = entry.getValue();

                if(upgradeInfoHolder.isBought() && !upgradeInfoHolder.isFinished()) {
                    scheduleUpgrade(guildHideout, upgrade);
                }
            }

            try {
                prepareHideOutHologramsAndNpcs(guildHideout);
            } catch (InvalidObjectException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void resetHideOutCompletelyOrCreate(String worldName) throws InvalidObjectException, InvalidHideoutWorldNameException {
        World world = Bukkit.getWorld(worldName);

        if(world == null) throw new InvalidObjectException("World " + worldName + " does not exist");

        if(!world.getName().startsWith("guild_")) throw new InvalidHideoutWorldNameException();

        GuildHideout guildHideout = resetOrCreateHideOut(worldName);

        Location loc = new Location(world, 0, 100, 0);

        paste(loc, Upgrade.RESET.getSchemFile());
        prepareHideOutHologramsAndNpcs(guildHideout);
    }


    public void addXPToHideout(GuildHideout guildHideout, int xp) {
        if(guildHideout == null) return;
        guildHideout.addExperience(xp);
        updateHideOut(guildHideout);
    }


    public void upgradeHideoutLevel(GuildHideout guildHideout) {

        if(guildHideout == null) return;

        guildHideout.upgradeHideout();

        updateHideOut(guildHideout);

    }
}
