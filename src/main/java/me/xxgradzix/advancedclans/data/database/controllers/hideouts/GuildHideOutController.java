package me.xxgradzix.advancedclans.data.database.controllers.hideouts;


import com.fastasyncworldedit.core.Fawe;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.SideEffectSet;
import eu.decentsoftware.holograms.api.DHAPI;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.controllers.clansCOre.UserController;
import me.xxgradzix.advancedclans.data.database.controllers.dtos.SkinPersistentDTO;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.data.database.services.hideout.GuildHideOutDataManager;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.fields.UpgradeInfoHolder;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.exceptions.ClanDoesNotExistException;
import me.xxgradzix.advancedclans.exceptions.PlayerDoesNotBelongToClanException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutDoesNotExistException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.InvalidHideoutWorldNameException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.UpgradeWasNotBoughtException;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.Countdown;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.*;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.InvalidObjectException;
import java.util.*;
import java.util.logging.Level;

public class GuildHideOutController {

    private final UserController userController;

    private final AdvancedGuilds plugin;

    public static final HashMap<GuildHideout.Upgrade, UpgradeBlueprint> upgradePatterns = new HashMap<>();

    public UpgradeBlueprint getUpgradeBlueprint(GuildHideout.Upgrade upgrade) {
        return upgradePatterns.get(upgrade);
    }

    public GuildHideOutController(UserController userController, AdvancedGuilds plugin) {
        this.userController = userController;
        this.plugin = plugin;

        {
            UpgradeBlueprint stationHallUpgrade = new StationHallUpgradeBlueprint(plugin);
            upgradePatterns.put(GuildHideout.Upgrade.STATION_HALL, stationHallUpgrade);

            UpgradeBlueprint blacksmithUpgrade = new BlackSmithUpgradeBlueprint(plugin);
            upgradePatterns.put(GuildHideout.Upgrade.BLACKSMITH, blacksmithUpgrade);

            UpgradeBlueprint resetPattern = new ResetHideoutBlueprint();
            upgradePatterns.put(GuildHideout.Upgrade.RESET, resetPattern);

            UpgradeBlueprint venturePattern = new VentureUpgradeBlueprint(plugin);
            upgradePatterns.put(GuildHideout.Upgrade.VENTURE, venturePattern);

            UpgradeBlueprint sorcererUpgradeBlueprint = new SorcererUpgradeBlueprint(plugin);
            upgradePatterns.put(GuildHideout.Upgrade.SORCERER, sorcererUpgradeBlueprint);

            UpgradeBlueprint outpostPodest = new OutpostPodestBlueprint(plugin);
            upgradePatterns.put(GuildHideout.Upgrade.OUTPOST_PODEST, outpostPodest);

            UpgradeBlueprint outpostHut = new OutpostHutBlueprint(plugin);
            upgradePatterns.put(GuildHideout.Upgrade.OUTPOST_HUT, outpostHut);

            UpgradeBlueprint arenaUpgrade = new ArenaUpgradeBlueprint(plugin);
            upgradePatterns.put(GuildHideout.Upgrade.ARENA, arenaUpgrade);

        }
    }

    public static final String DEFAULT_HIDEOUT_HOLOGRAM_SUFFIX = "_default_hideout_hologram";
    public static final String OCCUPIED_HIDEOUT_HOLOGRAM_SUFFIX = "_occupied_hideout_hologram";

    public void occupyHideout(Player player, GuildHideout guildHideout) throws HideOutDoesNotExistException {

        User user = userController.findUserByPlayer(player).orElseThrow(() -> new RuntimeException("User not found"));

        Clan clan = ClanAndUserDataManager.getCachedClan(user.getClanTag());

        if(clan == null) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_ANY_CLAN, MessageType.CHAT);
            return;
        }

        if(GuildHideOutDataManager.isHideoutOccupied(guildHideout.getWorldName())) {
            MessageManager.sendMessageFormated(player, MessageManager.HIDEOUT_ALREADY_OCCUPIED, MessageType.CHAT);
            return;
        }

        GuildHideOutDataManager.occupyHideOut(guildHideout, clan);
    }

    public void setOperatingLocationForHideout(String guildHideout, Location location) throws HideOutDoesNotExistException {
        GuildHideout guildHideout1 = GuildHideOutDataManager.getHideOut(guildHideout);
        if (guildHideout1 == null) throw new HideOutDoesNotExistException("Hideout " + guildHideout + " does not exist");
        GuildHideOutDataManager.setHideOutOperatingLocation(guildHideout1, location);
    }

    public void attemptTeleportByHologram(Player player, String hologramName) {

        User user = userController.findUserByPlayer(player).orElseThrow(() -> new RuntimeException("User not found"));

        Clan clan = ClanAndUserDataManager.getCachedClan(user.getClanTag());

        if(clan == null) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
            return;
        }

        String guildHideoutName = clan.getHideoutId();

        if(guildHideoutName == null || !hologramName.startsWith(guildHideoutName)) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
            return;
        }

        GuildHideout guildHideout = GuildHideOutDataManager.getHideOut(guildHideoutName);

        try {
            GuildHideOutDataManager.attemptTeleportToHideOut(player, guildHideout);
        } catch (HideOutDoesNotExistException e) {
            MessageManager.sendMessageFormated(player, MessageManager.HIDEOUT_DOES_NOT_EXIST, MessageType.CHAT);
        } catch (ClanDoesNotExistException | PlayerDoesNotBelongToClanException e) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
        }

    }

    public GuildHideout getPlayerHideOut(Player player) {
        Optional<User> optionalUser = userController.findUserByPlayer(player);

        if(optionalUser.isEmpty()) try {
            throw new InvalidObjectException("Dla gracza " + player.getName() + " nie ma usera");
        } catch (InvalidObjectException e) {
            throw new RuntimeException(e);
        }

        User user = optionalUser.get();

        if(!user.hasClan()) return null;

        if(!ClanAndUserDataManager.getCachedClan(user.getClanTag()).hasHideout()) return null;

        return GuildHideOutDataManager.getHideOut(ClanAndUserDataManager.getCachedClan(user.getClanTag()).getHideoutId());

    }

    public void resetHideOutCompletelyOrCreate(String worldName) throws InvalidObjectException, InvalidHideoutWorldNameException {
        World world = Bukkit.getWorld(worldName);

        if(world == null) throw new InvalidObjectException("World " + worldName + " does not exist");

        if(!world.getName().startsWith("guild_")) throw new InvalidHideoutWorldNameException();

        GuildHideout guildHideout = GuildHideOutDataManager.resetOrCreateHideOut(worldName);

        Location loc = new Location(world, 0, 100, 0);

        UpgradeBlueprint pattern = upgradePatterns.get(GuildHideout.Upgrade.RESET);
        paste(loc, pattern.getSchemFile());
        prepareHideOutHologramsAndNpcs(guildHideout);
    }

    public void upgradeHideOut(@NotNull GuildHideout hideout, GuildHideout.Upgrade upgrade) {

        UpgradeInfoHolder upgradeHolder = hideout.getUpgradeHolder(upgrade);

        final UpgradeBlueprint upgradeBlueprint = upgradePatterns.get(upgrade);

        if(upgradeBlueprint == null){
            Bukkit.getLogger().log(Level.SEVERE, "Error: Upgrade pattern not found for upgrade: " + upgrade.name());
            return;
        }

        final int upgradeTimeSeconds = upgradeBlueprint.getCoolDown();

        if(upgradeHolder == null){
            upgradeHolder = new UpgradeInfoHolder( (System.currentTimeMillis() + 1000L * upgradeTimeSeconds), true);
        } else {
            upgradeHolder.setBought(true);
            upgradeHolder.setTimeOfCompletion(System.currentTimeMillis() + 1000L * upgradeTimeSeconds);
        }

        hideout.setUpgradeHolder(upgrade, upgradeHolder);

        GuildHideOutDataManager.updateHideOut(hideout);

        scheduleUpgrade(hideout, upgrade);
    }

    public void scheduleUpgrade(GuildHideout hideout, GuildHideout.Upgrade upgrade) {
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
            hideout.setUpgradeHolder(upgrade, upgradeHolder);
            GuildHideOutDataManager.updateHideOut(hideout);
            return;
        }

        final UpgradeBlueprint upgradeBlueprint = upgradePatterns.get(upgrade);

        if(upgradeBlueprint == null){
            Bukkit.getLogger().log(Level.SEVERE, "Error: Upgrade pattern not found for upgrade: " + upgrade.name());
            return;
        }

        Countdown countdown = new Countdown(plugin, Countdown.HologramType.GUILD_UPGRADE, Math.toIntExact(timeToCompletionSeconds)+1, upgradeBlueprint.getHologramLocation(Bukkit.getWorld(hideout.getWorldName())), () -> {

            Bukkit.broadcastMessage("Zakupiono ulepszenie: " + upgrade.name()); //  TODO CHANGE MESSAGE

            Location loc = new Location(Bukkit.getWorld(hideout.getWorldName()), 0, 100, 0);
            paste(loc, upgradeBlueprint.getSchemFile());

            upgradeHolder.setFinished();

            hideout.setUpgradeHolder(upgrade, upgradeHolder);

            hideout.getUpgradeHolder(upgrade);
            GuildHideOutDataManager.updateHideOut(hideout);

            try {
                prepareHideOutHologramsAndNpcs(hideout);
            } catch (InvalidObjectException e) {
                throw new RuntimeException(e);

            }

        });
        countdown.scheduleTimer();
    }

    public static void paste(Location location, File file) {
        try (EditSession session = createNewEditSession(location.getWorld())) {

            ClipboardFormat format = ClipboardFormats.findByFile(file);

            if(format == null) throw new RuntimeException("Clipboard format not found");

            ClipboardReader reader = format.getReader(new FileInputStream(file));

            Clipboard schematic = reader.read();

            Operation operation = new ClipboardHolder(schematic)
                    .createPaste(session)
                    .to(BukkitAdapter.asBlockVector(location))
                    .build();

            Operations.complete(operation);
        } catch (Throwable ignored) {
        }
    }

    private static EditSession createNewEditSession(World bukkitWorld) {
        final EditSession session = Fawe.instance().getWorldEdit().newEditSession(BukkitAdapter.adapt(bukkitWorld));

        session.setSideEffectApplier(SideEffectSet.defaults());
        return session;
    }

    public void loadHideOuts() {
        List<GuildHideout> guildHideouts = GuildHideOutDataManager.loadAllHideOuts();

        for (GuildHideout guildHideout : guildHideouts) {
            guildHideout.getHideoutUpgrades().forEach((upgrade, upgradeInfoHolder) -> {
                if(upgradeInfoHolder.isBought() && !upgradeInfoHolder.isFinished()) {
                    scheduleUpgrade(guildHideout, upgrade);
                }
            });
            try {
                prepareHideOutHologramsAndNpcs(guildHideout);
            } catch (InvalidObjectException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static final String HIDEOUT_PANEL = "hideout_panel";
    public static final String MAIN_STORAGE = "hideout_storage";
    public static final String HIDEOUT_TRADER = "hideout_trader";
    public static final String STATION_HALL = "hideout_station_hall";
    public static final String HIDEOUT_WITCH = "hideout_witch";
    public static final String HIDEOUT_TELEPORT = "hideout_teleport";

    public static final String ARENA_UPGRADE = "arena_upgrade";

    private void prepareHideOutHologramsAndNpcs(@NotNull GuildHideout guildHideout) throws InvalidObjectException {

        final String hideoutPanel = guildHideout.getWorldName() + HIDEOUT_PANEL;
        final String mainStorage = guildHideout.getWorldName() + MAIN_STORAGE;
        final String hideoutTrader = guildHideout.getWorldName() + HIDEOUT_TRADER;
        final String stationHall = guildHideout.getWorldName() + STATION_HALL;
        final String witch = guildHideout.getWorldName() + HIDEOUT_WITCH;
        final String teleport = guildHideout.getWorldName() + HIDEOUT_TELEPORT;

        final String arenaUpgrade = guildHideout.getWorldName() + ARENA_UPGRADE;
        final String blackMarket = guildHideout.getWorldName() + ARENA_UPGRADE + "_black_market";
        final String bookMaker = guildHideout.getWorldName() + ARENA_UPGRADE + "_book_maker";
        final String arenaMaster = guildHideout.getWorldName() + ARENA_UPGRADE + "_arena_master";


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

        resetNpcsForHideOut(guildHideout);

        World world = Bukkit.getWorld(guildHideout.getWorldName());

        if(world == null) throw new InvalidObjectException("World " + guildHideout.getWorldName() + " does not exist");

        DHAPI.createHologram(hideoutPanel, new Location(world, -8.5 , 100.5, -24.5), Arrays.asList(ColorFixer.addColors("#b59651&lᴘᴀɴᴇʟ ɢɪʟᴅʏᴊɴʏ"), ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ, ᴀʙʏ ᴜʟᴇᴘꜱᴢʏć ᴋʀʏᴊóᴡᴋę")));
        DHAPI.createHologram(mainStorage, new Location(world, 0.5 , 100, -43.5), Arrays.asList(ColorFixer.addColors("#b59651&lᴍᴀɢᴀᴢʏɴ ɢɪʟᴅʏᴊɴʏ"), ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ, ᴀʙʏ ᴏᴛᴡᴏʀᴢʏᴄ́ ᴍᴀɢᴀᴢʏɴ ɢɪʟᴅʏᴊɴʏ")));
        DHAPI.createHologram(hideoutTrader, new Location(world, -5.5 , 100.5, -47), Arrays.asList(ColorFixer.addColors("#47807c&lʜᴀɴᴅʟᴀʀᴢ ɢɪʟᴅʏᴊɴʏ"), ColorFixer.addColors("&7ᴋʟɪɴɪᴊ, ᴀʙʏ ᴏᴛᴡᴏʀᴢʏć ꜱᴋʟᴇᴘ ɢɪʟᴅʏᴊɴʏ")));
        DHAPI.createHologram(witch, new Location(world, 5.5 , 100.5, -48.5), Arrays.asList(ColorFixer.addColors("#7830ab&lᴡɪᴇᴅźᴍᴀ ɢɪʟᴅʏᴊɴᴀ"), ColorFixer.addColors("&7ᴋʟɪɴɪᴊ, ᴀʙʏ ᴜᴢᴜᴘᴇᴌɴɪć ᴍɪᴋꜱᴛᴜʀʏ")));
        DHAPI.createHologram(teleport, new Location(world, -7.5 , 102.5, -10.0), Arrays.asList(ColorFixer.addColors("#35543b&lᴡᴏźɴɪᴄᴀ ɢɪʟᴅʏᴊɴʏ"), ColorFixer.addColors("&7ᴋʟɪɴɪᴊ, ᴀʙʏ ᴏᴛᴡᴏʀᴢʏć ᴍᴇɴᴜ ᴍᴏżʟɪᴡʏᴄʜ ᴅᴇꜱᴛʏɴᴀᴄᴊɪ")));

        if(!guildHideout.hasBoughtUpgrade(GuildHideout.Upgrade.STATION_HALL)) {
            DHAPI.createHologram(stationHall, new Location(world, 12.5 , 99.5, -43.5), Arrays.asList(ColorFixer.addColors("#61edba&lʀᴏᴢʙᴜᴅᴏᴡᴀ ᴋʀʏᴊóᴡᴋɪ #b59651#1"), "&7ᴀʙʏ ᴛᴀᴍ ᴘʀᴢᴇᴊść ᴋᴜᴘ ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴡ ᴘᴀɴᴇʟᴜ"));
        }
        if(!guildHideout.hasBoughtUpgrade(GuildHideout.Upgrade.ARENA)) {
            DHAPI.createHologram(arenaUpgrade, new Location(world, 0.5 , 99.5, -51), Arrays.asList(ColorFixer.addColors("#61edba&lʀᴏᴢʙᴜᴅᴏᴡᴀ ᴋʀʏᴊóᴡᴋɪ #b59651#2"), "&7ᴀʙʏ ᴛᴀᴍ ᴘʀᴢᴇᴊść ᴋᴜᴘ ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴡ ᴘᴀɴᴇʟᴜ"));
        } else {
            DHAPI.createHologram(blackMarket, new Location(world, 6.5 , 95.5, -66.5), Arrays.asList(ColorFixer.addColors("#383734&lᴄᴢᴀʀɴʏ ʀʏɴᴇᴋ"), "&7ᴛᴜ ᴡʏꜱᴛᴀᴡɪꜱᴢ ᴘʀᴢᴇᴅᴍɪᴏᴛʏ ɴᴀ ᴀᴜᴋᴄᴊᴇ"));
            DHAPI.createHologram(arenaMaster, new Location(world, 9.5 , 95.5, -86.5), Arrays.asList(ColorFixer.addColors("#7a001b&lᴍɪꜱᴛʀᴢ ᴀʀᴇɴʏ"), "&7ᴛᴜ ᴍᴏżᴇꜱᴢ ᴢᴀᴘʟᴀɴᴏᴡᴀć ɪ ᴅᴏᴌąᴄᴢʏć ᴅᴏ ᴛᴜʀɴɪᴇᴊᴜ"));
            DHAPI.createHologram(bookMaker, new Location(world, -11.5 , 95.5, -85.5), Arrays.asList(ColorFixer.addColors("#bf8c00&lʙᴜᴋᴍᴀᴄʜᴇʀ"), "&7ᴛᴜ ᴍᴏżᴇꜱᴢ ᴏʙꜱᴛᴀᴡɪć ᴡᴀʟᴋɪ"));
        }
    }


    private static final SkinPersistentDTO traderDTO;
    private static final SkinPersistentDTO teleportDTO;
    private static final SkinPersistentDTO blacksmithDTO;
    private static final SkinPersistentDTO ventureDTO;
    private static final SkinPersistentDTO sorcererDTO;

    private static final SkinPersistentDTO blackMarketDealerDTO;
    private static final SkinPersistentDTO bookMakerDTO;
    private static final SkinPersistentDTO arenaMasterDTO;
    private static final SkinPersistentDTO spawnArenaNpcDTO;


    static {
        traderDTO = new SkinPersistentDTO("ddd87d9e", "irpVrcqKWn35YcwRTt4B1+S/fvXtnUlj121Lu6uCRTK6AdAaPapfHuWkxL5v49RwuP0rqZA/d8HK+bpLtLGL9q20SNff0W2mpJu9gj4xn5hv6v1mFmSuobpfrNuTYHfZcHUla2qiEPYdv56z6fxw/CGMyqwCc9MIj+PCB82BwtR0+t049zeFJjV1cJJcZSpKerA9nN5uzfsIcLpYpbOnL/16VmNZIY6lWnwQ8cTmbuYcwLekBs2F59ackBTUH3SHxOjDlYBCYPTiULvsnZjfZjKeal9O1ckp+gEWvy83Z6cTYuegIAlt/B4tFfzP0CQbTA2qEKip++AWHIEszcvXU3m9uhHFSoKlLdW5ukBGWzIyQWuYl8RCn/hYjZEOr0Lr/tUN6Qcz+6QLNGFYssm15QnA1xMzTA5XoWtQ6ACeoyz0b0x0WZcN1H53gYN3s39Be75QEIKz+QxlQlPl64PMFLPfCT44c4Y6VQnFVEE7GNvnQNDHxIjlt2GY9oQfUK+TVxMK+kbcqBLkl+QyZGT8dz+3fZvL5impI/cRfYmfvpDtg51InxEJahMQN1cc4HxtsIpVbf4uZScpBWu0NM64iPyEMSnVWKDqjx3VlWns4kDScJ79Mv57f7sdbORbzZDIGAU/6B2gsBBs9FL+O43uv/HHJULWx44tPK4e+cHO0PU=","ewogICJ0aW1lc3RhbXAiIDogMTY0MjY5Mzk5OTM2MywKICAicHJvZmlsZUlkIiA6ICJhYTZhNzUwNWVkYmU0NjNiYjk1NWYyMWY0MjNiYTM1NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJub3RhbmR5d2FyaG9sIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2FiMDQxMTQ1NjhkMmI5NzczYjI5OTc5YWQ1NzZkM2M4YmY1Yjg3NDA1NGE0YjQyMjFjOGJiY2M3YTgyMDMwMmIiCiAgICB9CiAgfQp9");
        ventureDTO = new SkinPersistentDTO("524e7255", "Fy84Vb/SljdDgkX3Jh0ZyMyE/9gpkkoM5S/2J1472oRAanKrpFGLeB9SGER2x/hOMFXaTqTePbBnuANWF849PKT9Hp6js61M/GUU2gY8YQ5lvsgRlIUianH0zH/4Zf9eI2VTTfjk020Af4DMnpqXTQWmTMyfiSFs9na8iQi0ECmg0u4e8/cu4fydWJwVh56GdgIL5PXdYpv8DZWWoA6VrnuYHunqKlGaxnWNV1EJfZC5LctjJJky2yhCmeSMBZX1C4aX3woEzaZPCuSRrmwEWRngSf1G7aPD7JxrPIZMLuwk87pr4gyaKaf6qBtvk8ZAlem1TmryvLHskUunXURV7OA6KFWkDgLx7dyigzMaiJ4RlA5u0iZnEMx3PfTnGLaJ7XY/UNM0qm7Glxd+K97R8XJw+RMERswgVgurcNgamhFIFfTpFatjJN+alnjb3tIa2b166NrmxDHQXeTlXmA6Lbm2/ZlMneY59b6LftdASzNZ9Qo1GuMzOwv0RRdilrzn/hIhetOMQixL0RWV5sO+yg8w5zLRfNwy4wiZPUrKVUNo0W3hqDqEgE4fJbEIIxNeGupmpl6rhZxVWTMm/RNyP5rFWY25qsPU/vVoBY+wkDu//UbBVP1l5sVC6sKdOBMDFv9F8aD29ArxEInra1awIAulswSrZ7SDIttAQTKjIpQ=", "eyJ0aW1lc3RhbXAiOjE1ODcyNzU5OTc2OTEsInByb2ZpbGVJZCI6IjdkYTJhYjNhOTNjYTQ4ZWU4MzA0OGFmYzNiODBlNjhlIiwicHJvZmlsZU5hbWUiOiJHb2xkYXBmZWwiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2NiMjY5ZmJlODRjNWEwZDEzMjJjYmUzOWI4ZDgyNWM4ZWUzODMzZDZlNjUwOGJiY2ExZjdlZTdkYzU1NjZiZTkifX19");
        blacksmithDTO = new SkinPersistentDTO("007c59ee", "FgjyTFUENg2Ig1t8Q7Xk5XRT2OjwgtmZ5hYCSCqFDJWA6WSiGLnhsY83/izw/4wvUII/51mgtuPceMM2VkD4Yzl8nAACTUmhMXCMm56Xn5SPUQn8K7lVM0w+IZbAXW30QEC7O9tZ7k4lQmIj+KnEkpduDtGU9pEUSbT+9EQtgeuXXoPZIFvxB6l7Aq6nEs5fcQheUBGvGf8DqjkkP/ZKn281ZFTXc9nTU3BQpOFcqi7V5K0s+At0eNxEm+dT07VBHtsHtnrXFy44vh1af4UBiH2FU74U1hZmJIqFKPVeWQLjc8psrGwIBWyL8FuFuxGwpZWMtbpB1c6VGeePozxNefiimHpBaqVww4iaCF9Hn98YJOXJzz9LxzYAMi/NQ9sncFdBdC3GJvdr745YMqRxL8ASNrzYAas6qd9kqy8O9NqaNBYve+eVYVb6vPZzUkA+MEX4mbWkGWfZyB1tLUMHFioSikK6v3rkWxu6arPHCMOcjb720mb/QT6kjGLr/lmOWw9tuKOYrtwg6POcTCuvLr5zYS/PI0YSUw2n8D4w/2ZjBD6uzOhbn11p6kIdL8JcPV8+gttVi0sulJ9KO+WRE+iK1HyCDmPCAVM27bDNSQpFvD/Zk+vFGZZSpALiq2XktoByhMul0T52Tfbw96x3Vn9225X64qkVRdB+LYmxxyU=", "ewogICJ0aW1lc3RhbXAiIDogMTU5NzQ1MjgwNjU5NiwKICAicHJvZmlsZUlkIiA6ICJkZTU3MWExMDJjYjg0ODgwOGZlN2M5ZjQ0OTZlY2RhZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNSEZfTWluZXNraW4iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU4ZDBkYjQ2MTcxMWYwYWE3YjVkYThiNzQyN2M5YmM2OThhODVjYzBmYzdjZWQ1MjFmNGE5MmJjMWFkMWRmMSIKICAgIH0KICB9Cn0=");
        teleportDTO = new SkinPersistentDTO("498afcab", "yTyaTlyHbZruHT0/06fCHEfwtf7QRmbQAOFeSmqQ5QFWKikrwTXEFq7+62pBM9aFmDzGVNIZ2b5wNd9ZVxKNhkfjKck9SHl3UiI5AvQNfbp6OXNNPeKArdLYvcgRzcddTbHe2yKVRpFVG4UUysCnl1oyk3frWAKTbUBaUwlWDg9HnAeXIVoaXqlNI6stdPwbrUczW/zh6Nb//+HHCEipbdGbU35EB0O3Vp6AlzeV3jn5wT/j8kItQU/m27TwtPA+6Urx8ypzNuTMmUipEMhfnRayqxgPEnssP82Nk02b7yno5vBtLjOU0O7JWLwwTkC2bE6OKXEu46Ul9Vuqwj0OrJDvC9LYn+t74spINNqV1pWCx5z7/LUO2NAyjyoxUjIbRO3NK/BnhtXAycOoKa0Rx6EThcgPeEGiLDaAOu5WNM/BMqYqmTqc9BNdZzzEwKv0Mbyth2XkRuw6sBdKijN+TnegQh1/PkD26+xEscvuC7dWKK0RLD1FxZKhI6j0dNhvq7aEKYuEV7nAvnZyOhrmPGJ4t3G6hJ8uTwxlRORatQUJ7CHlvw3U/Ee/JfdIBjxhDdEcqkoBEqPIP9Dr6ZNqu/06HRGgroDqES3kdTRQTkBWkrmgLShi23V/YE0HKZUdrKe9TfEFXFekSkmoSHlAjrP73b7Torjrk8kNVf1D++8=","eyJ0aW1lc3RhbXAiOjE1ODcxNzI2NTc2NTcsInByb2ZpbGVJZCI6ImIwZDczMmZlMDBmNzQwN2U5ZTdmNzQ2MzAxY2Q5OGNhIiwicHJvZmlsZU5hbWUiOiJPUHBscyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU4NWRhNjA2Yzc5NTZmNmE4YjI0NzRjYTM5N2UwZTFlMTg1NTc0YjYyMmY5YTJlNzFlZjIzOWI4NDliNDVhYyIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19");
        sorcererDTO = new SkinPersistentDTO("e7a8bb24", "Ob1xZlOiHj/BtWh+Vmpf4nkg0rYMuNw7YpgWzln3uLiYIg78eEgfz05AbzzYUghvVEwh5yXXKUTSgvBDo6MkoaOO7+rnUPXfZ1edhVWbQX5tJQHzNDO1nBZXlO0K7JO3xPdf1DyepryNpYFtl7x+HO+02n1hNXzuKA/RE6PbuVmpWtVDSyfzdCpbXi5fXMSCGoZb3Z4rvPewDk2qjBcurne0mRHyxGlTkYu7HuVITbcIgdDpKSrmCKfjZCLG9QK0+lLMPSJBlIw5fLEINXrFH6AfkjuszEenjugGbrcp2MtaDC568cE62Lwrwb5JepbnD32jkB+k/4eKcbNsBc7dSZ3WpL+r2DpI7WU0uNbqVe0QwBs9XMFJkTaIeWsxm1zKBAuj5wux1aimHRcCJJ3xQP1NOZscJqBZejMdZ5sscXebRGLhBhxNYSoqaKu+f93BqKHLraOBS+nj3YG2w/4/yGaLQEIPfuHoC8VBVMGwezAcEvmV66gQVSS6UmN0edWsmFVUMjPZcHYUAMtK8MZ8d4zaDWI95dIh55MI2JFhqr31qkUmfc2+aTy0dgIJ2G0QmUc23yYXwYqHvfditKIm1Co75ZB4OYqEYmJHtsRyaO0t3SCUdoWiMes+ElYiKQJB68CWV606kPpvAiF1lFDqz198/ClnrPOu99vEPSH51ls=","ewogICJ0aW1lc3RhbXAiIDogMTU4ODE5MjY4MTMwNSwKICAicHJvZmlsZUlkIiA6ICIzZmM3ZmRmOTM5NjM0YzQxOTExOTliYTNmN2NjM2ZlZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJZZWxlaGEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWU0OTE0YzQ4YmU2YTVhNzEyYWIwMWJhZGE2Nzk3Yjc1NjYyNzM4OWNkM2ZmOWJlOTJmMWY4M2I3MGE0ZTVkNiIKICAgIH0KICB9Cn0=");

        bookMakerDTO = new SkinPersistentDTO("37667219", "tNlbx/6xJz98zl9hMAaFGE8LRT89ylpJrmvXy7IceDdm6LuJdWIpQNZp/5sDzTY2U90oNLlRD4OH+vJvwyr8/a3PfD+8z7RElFMtzNKKJTjtwshdcKu49TQTHnHFfWtbOoTkkLEh4Rwkear4bZT7VZdLYGNxKJTqQNk3soKT57R26YaZfFvXI6as0lqD6ZK6Xh/+KydVpWoncqsWsSHbxDoaQWgA52Io/OjNs8e+l3Kx+LdhxyS0P8ybdNZZ6YzRRGpJTCKI7frMhB+DsOkANjFaLL70IU4Wfs6R5awjCPcw6dEsDymHBChcDKf/sp+WKnevFPHS3E62i5OFyTxr/jp3unVKD/HFF3B1unSWGUVz+p8pAZlmZ7/BEGyPSzDy6E0shTUkOrDDwP5JOsAFry7v4scyUbt7FjVadWvi+J3+2qiEdXUw4MUwqJjH7IuVI7J0tGuzFx/7yOaAQw+Rv9FdXz2NA1uynVmh3DBcTKCacSqt/pUGKEAslowE4FmGdoQe3u8CrBU7X4FxRVAJDlkrR7cFeKSm/6cYv3z89pRtBe/xt+eCQwWGnC2wVyXjMICLWsppGM8KRVzC3GbW9P/IJ80/y+hXHE1/DavMGOUkd9qJ9oG6DTPCRVnmhq1CubojBqMWzDoxHkYkL5kuqf6ce5JHQxCTvAbLAxGAszI=", "eyJ0aW1lc3RhbXAiOjE1ODU1MjQxMDk4NDQsInByb2ZpbGVJZCI6ImRlNTcxYTEwMmNiODQ4ODA4ZmU3YzlmNDQ5NmVjZGFkIiwicHJvZmlsZU5hbWUiOiJNSEZfTWluZXNraW4iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzFlNjg3MGNlMjk0MDcxM2M1NzQwY2NkODhjMWNlM2YyNWQzYTRjY2NlODVmNGVmNmE1ZDc4Y2U4NmExNWJjMzkifX19");
        blackMarketDealerDTO = new SkinPersistentDTO("64c46b20", "eEQ7IViA3+UtVRQbbGzZ+EP/vguj5dzcP1HUzeJEKgu5vHvj7HFxkSwHQkSWMXFKpxyYoxU0LnlYoZe2Ng15M1TNbxnYB8Y28afXOUEbOBrHlY0X/D+C9/iZ1Rdb1lpdk/QYJtER2NDxyUFJsGESKUVQA5JSvVaeOCGRSl4CfDzkQe7NuWlRpSUbHpDQsWJ9lwxk3dSwYqRF1cbuZtkgotDYvtdQ1FEYEoRaJr5FDXfLeGr6bkXPQEPxnFeVFhBZizVC7ego8yhmP89m3M7z8frempeG2P/kqZY0dPdlgdRd+WdwhL8KX+Y9GLlndEcGwclMHgb0J9E8U5PQ32e6MY8cusxiRSpguEcTdlWvtZTR7x8gRoGA6Lsky7QrCC9HbkhZ2KTRkivcitBmIvxVPNlWmEL72UV9aTiXwmljXD14NCZF9bGYW5fBMTLPoazfZK7a5c7AXtn7B2ZZ1NvYnHA6bMg4lmWgDRMes+Zq26GHWRMRpoETO780sAxqtcZIo4t5tZPm+DfDbtpDrm4VKQyjjZ3s42tyAWU1UL/YidB/rcGO4T7R4pRyzNVaPvmtMKRhudaxaUooPHIOAPKVhx9tfdtPGDb8L7GtHxKpLCbp5+gQJ9WvomAneAcTU2YbM6/IPLxDa0cXcf4I8Z4peALJUFVPt7crRs12FBPm8TY=", "ewogICJ0aW1lc3RhbXAiIDogMTcyODIzNjcwMzEwNiwKICAicHJvZmlsZUlkIiA6ICJjMjVlMWMxZTE1YTQ0N2IwOTQ2Zjg2YzYzYzhjYjZkOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJiaWdpYm9zMzIxIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2VmNWQ5ZjEwYzJhZmIyN2I1YjVmYWIwMzFhZWMyMjcwZGYxMTUyY2NhYzdiZDhjMTA5YTJmZTFkNmIxMzQwMTkiCiAgICB9CiAgfQp9");
        arenaMasterDTO = new SkinPersistentDTO("338f8012", "e/6VycZ9E6BIJzir553LdxFgnfIo7qSLkk3TJ4q+tfiN0cArWBNbnBehol3WYOHkuQzZCgsul2LMujk7PtsTsEyyRvnMmoWdX22xQqyAKLLoh/MoXNcxkWFbOgfS9209gFjDr+tse1FDmgnXhtpTli4ZZXl9V2wH+HdtTuvUcCOSTMlAfW8bpei4dZU9XTsznZQngdZRFJ4q3XGaiG+hNJGvUYEpVVxr8ASMgukVrO516uO6etdlCZOWOZty0RBysUwBw92jRK43nl1Ns+sc2789Pv16QXqdS/MXIa6LN77fZXkRd/G8+byHzH65IAw8MYxL0Yzokwvb7Lqij3wlDFylXbuZVL3iAuBNhlrkwVuOBVIctqGXhur3u7nEfEFvqnGA/qc58+4xGF+d3bIncE+R7w95gSK1tIJMMQV8WJCbUNME2SVjubf4wxUnzPQp6LGYJFy2a5werDMcw5vU6XAqdAnqzw89TSrB99ksp+tIvy89Er7A+A4W5lcc7zy7euZRPX+p9pLRh4/tJyNQLFoPEIHhtJ51BNfoLvQh2sly52y9XceqYfzeDXadpbwBAgvAGaoFuPa4BpT9ZBwuzZWK0UlopgI2erO1EoXVCJPYKMfbcYWZ8/z8UfRJbuks7n6OZUhpud6yvO6+gRTW98G/uF1S22lnJhMuaAQp3Yw=", "ewogICJ0aW1lc3RhbXAiIDogMTY2OTg3NjM3OTAxMywKICAicHJvZmlsZUlkIiA6ICI3YzI2YTAxY2U4NjU0NDkzOTA3NzA2OGQxZTA5ZjE5MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJodG93ZXI4IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzM2MTRhMjY4NWRjZTRlNTkyMWY2MmM2ZDk2ZjlmMjRlNTk5NjI4YzliNTU4NWUyZTllOGM0YzQ5MTU5NTU0ZmYiCiAgICB9CiAgfQp9");
        spawnArenaNpcDTO = new SkinPersistentDTO("498afcab", "yTyaTlyHbZruHT0/06fCHEfwtf7QRmbQAOFeSmqQ5QFWKikrwTXEFq7+62pBM9aFmDzGVNIZ2b5wNd9ZVxKNhkfjKck9SHl3UiI5AvQNfbp6OXNNPeKArdLYvcgRzcddTbHe2yKVRpFVG4UUysCnl1oyk3frWAKTbUBaUwlWDg9HnAeXIVoaXqlNI6stdPwbrUczW/zh6Nb//+HHCEipbdGbU35EB0O3Vp6AlzeV3jn5wT/j8kItQU/m27TwtPA+6Urx8ypzNuTMmUipEMhfnRayqxgPEnssP82Nk02b7yno5vBtLjOU0O7JWLwwTkC2bE6OKXEu46Ul9Vuqwj0OrJDvC9LYn+t74spINNqV1pWCx5z7/LUO2NAyjyoxUjIbRO3NK/BnhtXAycOoKa0Rx6EThcgPeEGiLDaAOu5WNM/BMqYqmTqc9BNdZzzEwKv0Mbyth2XkRuw6sBdKijN+TnegQh1/PkD26+xEscvuC7dWKK0RLD1FxZKhI6j0dNhvq7aEKYuEV7nAvnZyOhrmPGJ4t3G6hJ8uTwxlRORatQUJ7CHlvw3U/Ee/JfdIBjxhDdEcqkoBEqPIP9Dr6ZNqu/06HRGgroDqES3kdTRQTkBWkrmgLShi23V/YE0HKZUdrKe9TfEFXFekSkmoSHlAjrP73b7Torjrk8kNVf1D++8=","eyJ0aW1lc3RhbXAiOjE1ODcxNzI2NTc2NTcsInByb2ZpbGVJZCI6ImIwZDczMmZlMDBmNzQwN2U5ZTdmNzQ2MzAxY2Q5OGNhIiwicHJvZmlsZU5hbWUiOiJPUHBscyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU4NWRhNjA2Yzc5NTZmNmE4YjI0NzRjYTM5N2UwZTFlMTg1NTc0YjYyMmY5YTJlNzFlZjIzOWI4NDliNDVhYyIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19");
    }



    public void resetNpcsForHideOut(GuildHideout guildHideout) {

        NPCRegistry NPC_REGISTRY = CitizensAPI.getNamedNPCRegistry(guildHideout.getWorldName() + "hideout_npc_registry");

        if(NPC_REGISTRY == null) {
            NPC_REGISTRY = CitizensAPI.createNamedNPCRegistry(guildHideout.getWorldName() + "hideout_npc_registry", new MemoryNPCDataStore());
        }

        NPC_REGISTRY.despawnNPCs(DespawnReason.PENDING_RESPAWN);
        NPC_REGISTRY.deregisterAll();

        final String hideoutTrader = ColorFixer.addColors("#4daac9ʜᴀɴᴅʟᴀʀᴢ");
        final String witch = ColorFixer.addColors("#c94db5ᴡɪᴇᴅźᴍᴀ");
        final String teleport = ColorFixer.addColors("#dbc274ᴘʀᴢᴇᴡᴏźɴɪᴋ");

        final String blacksmith = ColorFixer.addColors("#7a7a7aᴋᴏᴡᴀʟ");
        final String sorcerer = ColorFixer.addColors("#9b33d4ᴢᴀᴋʟɪɴᴀᴄᴢ");
        final String venture = ColorFixer.addColors("#366b9cᴋᴀᴘɪᴛᴀɴ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ");

        final String blackMarketDealer = ColorFixer.addColors("#303030ᴅɪʟᴇʀ");
        final String arenaMaster = ColorFixer.addColors("#917143ᴍɪꜱᴛʀᴢ ᴀʀᴇɴʏ");
        final String bookMaker = ColorFixer.addColors("#e6af25ʙᴜᴋᴍᴀᴄʜᴇʀ");
        final String spawnArena = ColorFixer.addColors("#4daac9ᴘʀᴢᴇᴡᴏźɴɪᴋ");

        NPC traderNPC = NPC_REGISTRY.createNPC(EntityType.PLAYER, UUID.randomUUID(), 1, hideoutTrader);
        NPC witchNPC = NPC_REGISTRY.createNPC(EntityType.WITCH, UUID.randomUUID(), 2, witch);
        NPC ventureNPC = NPC_REGISTRY.createNPC(EntityType.PLAYER, UUID.randomUUID(), 3, venture);
        NPC blacksmithNPC = NPC_REGISTRY.createNPC(EntityType.PLAYER, UUID.randomUUID(), 4, blacksmith);
        NPC sorcererNPC = NPC_REGISTRY.createNPC(EntityType.PLAYER, UUID.randomUUID(), 5, sorcerer);
        NPC teleportNPC = NPC_REGISTRY.createNPC(EntityType.PLAYER, UUID.randomUUID(), 6, teleport);
        NPC blackMarketDealerNPC = NPC_REGISTRY.createNPC(EntityType.PLAYER, UUID.randomUUID(), 7, blackMarketDealer);
        NPC spawnArenaNPC = NPC_REGISTRY.createNPC(EntityType.PLAYER, UUID.randomUUID(), 8, spawnArena);
        NPC bookMakerNPC = NPC_REGISTRY.createNPC(EntityType.PLAYER, UUID.randomUUID(), 9, bookMaker);
        NPC arenaMasterNPC = NPC_REGISTRY.createNPC(EntityType.PLAYER, UUID.randomUUID(), 10, arenaMaster);

        for (NPC npc : NPC_REGISTRY) {
            npc.getOrAddTrait(LookClose.class).lookClose(true);
            npc.getOrAddTrait(LookClose.class).setRange(35);
            npc.data().setPersistent(NPC.Metadata.ALWAYS_USE_NAME_HOLOGRAM, true);
            npc.scheduleUpdate(NPC.NPCUpdate.PACKET);
            npc.setUseMinecraftAI(false);
            npc.setProtected(true);
        }

        traderNPC.getOrAddTrait(SkinTrait.class).setSkinPersistent(traderDTO.skinName(), traderDTO.skinSignature(), traderDTO.skinValue());
        ventureNPC.getOrAddTrait(SkinTrait.class).setSkinPersistent(ventureDTO.skinName(), ventureDTO.skinSignature(), ventureDTO.skinValue());
        blacksmithNPC.getOrAddTrait(SkinTrait.class).setSkinPersistent(blacksmithDTO.skinName(), blacksmithDTO.skinSignature(), blacksmithDTO.skinValue());
        sorcererNPC.getOrAddTrait(SkinTrait.class).setSkinPersistent(sorcererDTO.skinName(), sorcererDTO.skinSignature(), sorcererDTO.skinValue());
        teleportNPC.getOrAddTrait(SkinTrait.class).setSkinPersistent(teleportDTO.skinName(), teleportDTO.skinSignature(), teleportDTO.skinValue());

        blackMarketDealerNPC.getOrAddTrait(SkinTrait.class).setSkinPersistent(blackMarketDealerDTO.skinName(), blackMarketDealerDTO.skinSignature(), blackMarketDealerDTO.skinValue());
        spawnArenaNPC.getOrAddTrait(SkinTrait.class).setSkinPersistent(spawnArenaNpcDTO.skinName(), spawnArenaNpcDTO.skinSignature(), spawnArenaNpcDTO.skinValue());
        bookMakerNPC.getOrAddTrait(SkinTrait.class).setSkinPersistent(bookMakerDTO.skinName(), bookMakerDTO.skinSignature(), bookMakerDTO.skinValue());
        arenaMasterNPC.getOrAddTrait(SkinTrait.class).setSkinPersistent(arenaMasterDTO.skinName(), arenaMasterDTO.skinSignature(), arenaMasterDTO.skinValue());

        traderNPC.spawn(new Location(Bukkit.getWorld(guildHideout.getWorldName()), -7.5 , 97, -48.5));
        witchNPC.spawn(new Location(Bukkit.getWorld(guildHideout.getWorldName()), 6.5 , 97, -49.5));

        if(guildHideout.hasFinishedUpgrade(GuildHideout.Upgrade.BLACKSMITH)) {
            blacksmithNPC.spawn(new Location(Bukkit.getWorld(guildHideout.getWorldName()), 26.5 , 93, -50.5));
        }
        if(guildHideout.hasFinishedUpgrade(GuildHideout.Upgrade.VENTURE)) {
            ventureNPC.spawn(new Location(Bukkit.getWorld(guildHideout.getWorldName()), 34.5 , 93, -38.5));
        }
        if(guildHideout.hasFinishedUpgrade(GuildHideout.Upgrade.SORCERER)) {
            sorcererNPC.spawn(new Location(Bukkit.getWorld(guildHideout.getWorldName()), 25.5 , 93, -37.5));
        }
        teleportNPC.spawn(new Location(Bukkit.getWorld(guildHideout.getWorldName()), -8.5 , 99, -10.5));

        if(guildHideout.hasFinishedUpgrade(GuildHideout.Upgrade.ARENA)) {
            blackMarketDealerNPC.spawn(new Location(Bukkit.getWorld(guildHideout.getWorldName()), 8.5 , 92, -63.5));
            spawnArenaNPC.spawn(new Location(Bukkit.getWorld(guildHideout.getWorldName()), 7.5 , 91, -76.5));
            bookMakerNPC.spawn(new Location(Bukkit.getWorld(guildHideout.getWorldName()), -13.5 , 92, -85.5));
            arenaMasterNPC.spawn(new Location(Bukkit.getWorld(guildHideout.getWorldName()), 11 , 92, -86.5));
        }
    }

    public GuildHideout getHideoutByLocation(Location location) {
        return GuildHideOutDataManager.getHideOutByLocation(location);
    }

}
