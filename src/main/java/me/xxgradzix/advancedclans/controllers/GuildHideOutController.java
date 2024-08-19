package me.xxgradzix.advancedclans.controllers;

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
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.services.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.data.database.services.GuildHideOutDataManager;
import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InvalidObjectException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class GuildHideOutController {

    private final UserController userController;

    private final AdvancedGuilds plugin;

    private final HashMap<GuildHideout.Upgrade, UpgradePattern> upgradePatterns = new HashMap<>();

    public UpgradePattern getUpgradePattern(GuildHideout.Upgrade upgrade) {
        return upgradePatterns.get(upgrade);
    }

    public GuildHideOutController(UserController userController, AdvancedGuilds plugin) {
        this.userController = userController;
        this.plugin = plugin;

        {
            UpgradePattern stationHallUpgrade = new StationHallUpgradePattern(plugin);
            upgradePatterns.put(GuildHideout.Upgrade.STATION_HALL, stationHallUpgrade);

            UpgradePattern blacksmithUpgrade = new BlackSmithUpgradePattern(plugin);
            upgradePatterns.put(GuildHideout.Upgrade.BLACKSMITH, blacksmithUpgrade);

            UpgradePattern resetPattern = new ResetHideoutPattern();
            upgradePatterns.put(GuildHideout.Upgrade.RESET, resetPattern);

            UpgradePattern venturePattern = new VentureUpgradePattern(plugin);
            upgradePatterns.put(GuildHideout.Upgrade.VENTURE, venturePattern);
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
        setOperatingLocationForHideout(guildHideout.getWorldName(), guildHideout.getTeleportLocation());
    }

    public void setOperatingLocationForHideout(String hideoutName, Location location) throws HideOutDoesNotExistException {

        GuildHideout guildHideout = GuildHideOutDataManager.getHideOut(hideoutName);

        if (guildHideout == null) throw new HideOutDoesNotExistException("Hideout " + hideoutName + " does not exist");

        final String defaultHideoutHologram = hideoutName + DEFAULT_HIDEOUT_HOLOGRAM_SUFFIX;
        final String occupiedHideoutHologram = hideoutName + OCCUPIED_HIDEOUT_HOLOGRAM_SUFFIX;

        DHAPI.removeHologram(defaultHideoutHologram);
        DHAPI.removeHologram(occupiedHideoutHologram);

        if(guildHideout.getClanTag() == null || guildHideout.getClanTag().isEmpty()) {
            DHAPI.createHologram(defaultHideoutHologram, location.clone().add(0, 2, 0), Arrays.asList(ColorFixer.addColors("#b59651&lᴋʀʏᴊóᴡᴋᴀ ɢɪʟᴅʏᴊɴᴀ"), ColorFixer.addColors("&7ᴘᴏᴌóż #47382cᴢᴇꜱᴛᴀᴡ ᴅᴏ ᴛᴡᴏʀᴢᴇɴɪᴀ ɢɪʟᴅɪɪ ᴘᴏɴɪżᴇᴊ &7ᴀʙʏ ᴢᴀᴊąć ᴛą ᴋʀʏᴊóᴡᴋę")));
        } else {
            DHAPI.createHologram(occupiedHideoutHologram, location.clone().add(0, 2, 0), Arrays.asList(ColorFixer.addColors("&7&lᴋʀʏᴊóᴡᴋᴀ ɢɪʟᴅɪɪ #b59651&l" + guildHideout.getClanTag()), ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴢᴇᴊść ᴅᴏ ᴋʀʏᴊóᴡᴋɪ")));
        }
        GuildHideOutDataManager.setHideOutOperatingLocation(hideoutName, location);

    }

    public void attemptTeleportByClickedBlock(Player player, Block block) {

        if(block == null) return;

        if(!block.getType().equals(Material.LODESTONE)) return;

        GuildHideout guildHideout = GuildHideOutDataManager.getHideOutByLocation(block.getLocation());
        if(guildHideout == null) return;

        try {
            GuildHideOutDataManager.attemptTeleportToHideOut(player, guildHideout);
        } catch (HideOutDoesNotExistException e) {

            MessageManager.sendMessageFormated(player, MessageManager.HIDEOUT_DOES_NOT_EXIST, MessageType.CHAT);
        } catch (ClanDoesNotExistException | PlayerDoesNotBelongToClanException e) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
        }
    }

    public void attemptTeleportByHologram(Player player, String hologramName) {

        User user = userController.findUserByPlayer(player).orElseThrow(() -> new RuntimeException("User not found"));

        Clan clan = ClanAndUserDataManager.getCachedClan(user.getClanTag());

        if(clan == null) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
            return;
        }

        String guildHideoutName = clan.getHideoutId();

        if(!hologramName.startsWith(guildHideoutName)) {
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

    public GuildHideout getPlayerHideOut(Player player) throws InvalidObjectException {
        Optional<User> optionalUser = userController.findUserByPlayer(player);

        if(optionalUser.isEmpty()) {
            throw new InvalidObjectException("Dla gracza " + player.getName() + " nie ma usera");
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

        UpgradePattern pattern = upgradePatterns.get(GuildHideout.Upgrade.RESET);
        paste(loc, pattern.getSchemFile());
        prepareHideOutHolograms(guildHideout);
    }

    public void upgradeHideOut(@NotNull GuildHideout hideout, GuildHideout.Upgrade upgrade) {

        UpgradeInfoHolder upgradeHolder = hideout.getUpgradeHolder(upgrade);

        final UpgradePattern upgradePattern = upgradePatterns.get(upgrade);

        if(upgradePattern == null){
            Bukkit.getLogger().log(Level.SEVERE, "Error: Upgrade pattern not found for upgrade: " + upgrade.name());
            return;
        }

        final int upgradeTimeSeconds = upgradePattern.getCoolDown();

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

        final UpgradePattern upgradePattern = upgradePatterns.get(upgrade);

        if(upgradePattern == null){
            Bukkit.getLogger().log(Level.SEVERE, "Error: Upgrade pattern not found for upgrade: " + upgrade.name());
            return;
        }

        Countdown countdown = new Countdown(plugin, Math.toIntExact(timeToCompletionSeconds), upgradePattern.getHologramLocation(Bukkit.getWorld(hideout.getWorldName())), () -> {

            Bukkit.broadcastMessage("Zakupiono ulepszenie: " + upgrade.name()); // TODO CHANGE MESSAGE
            Location loc = new Location(Bukkit.getWorld(hideout.getWorldName()), 0, 100, 0);
            paste(loc, upgradePattern.getSchemFile());
            GuildHideOutDataManager.updateHideOut(hideout);

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
                prepareHideOutHolograms(guildHideout);
            } catch (InvalidObjectException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static final String HIDEOUT_PANEL = "hideout_panel";
    public static final String MAIN_STORAGE = "hideout_storage";
    public static final String HIDEOUT_TRADER = "hideout_trader";
    public static final String STATION_HALL = "hideout_station_hall";


    private void prepareHideOutHolograms(@NotNull GuildHideout guildHideout) throws InvalidObjectException {

        final String hideoutPanel = guildHideout.getWorldName() + HIDEOUT_PANEL;
        final String mainStorage = guildHideout.getWorldName() + MAIN_STORAGE;
        final String hideoutTrader = guildHideout.getWorldName() + HIDEOUT_TRADER;
        final String stationHall = guildHideout.getWorldName() + STATION_HALL;

        DHAPI.removeHologram(hideoutPanel);
        DHAPI.removeHologram(mainStorage);
        DHAPI.removeHologram(hideoutTrader);
        DHAPI.removeHologram(stationHall);

        World world = Bukkit.getWorld(guildHideout.getWorldName());

        if(world == null) throw new InvalidObjectException("World " + guildHideout.getWorldName() + " does not exist");

        DHAPI.createHologram(hideoutPanel, new Location(world, -8.5 , 100.5, -24.5), Arrays.asList(ColorFixer.addColors("#b59651&lᴘᴀɴᴇʟ ɢɪʟᴅʏᴊɴʏ"), ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ, ᴀʙʏ ᴜʟᴇᴘꜱᴢʏć ᴋʀʏᴊóᴡᴋę")));
        DHAPI.createHologram(mainStorage, new Location(world, 0.5 , 100, -43.5), Arrays.asList(ColorFixer.addColors("#b59651&lᴍᴀɢᴀᴢʏɴ ɢɪʟᴅʏᴊɴʏ"), ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ, ᴀʙʏ ᴏᴛᴡᴏʀᴢʏᴄ́ ᴍᴀɢᴀᴢʏɴ ɢɪʟᴅʏᴊɴʏ")));
        DHAPI.createHologram(hideoutTrader, new Location(world, -5.5 , 100.5, -47), Arrays.asList(ColorFixer.addColors("#47807c&lʜᴀɴᴅʟᴀʀᴢ ɢɪʟᴅʏᴊɴʏ"), ColorFixer.addColors("&7ᴋʟɪɴɪᴊ, ᴀʙʏ ᴏᴛᴡᴏʀᴢʏć ꜱᴋʟᴇᴘ ɢɪʟᴅʏᴊɴʏ")));

        if(!guildHideout.hasBoughtUpgrade(GuildHideout.Upgrade.STATION_HALL)) {
            DHAPI.createHologram(stationHall, new Location(world, 12.5 , 99.5, -43.5), Arrays.asList(ColorFixer.addColors("#61edba&lʀᴏᴢʙᴜᴅᴏᴡᴀ ᴋʀʏᴊóᴡᴋɪ #b59651#1"), "&7ᴀʙʏ ᴛᴀᴍ ᴘʀᴢᴇᴊść ᴋᴜᴘ ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴡ ᴘᴀɴᴇʟᴜ"));
        }




    }

    public GuildHideout getHideoutByLocation(Location location) {
        return GuildHideOutDataManager.getHideOutByLocation(location);
    }
}
