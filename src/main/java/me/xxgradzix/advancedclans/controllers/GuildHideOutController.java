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
import me.xxgradzix.advancedclans.data.database.services.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.data.database.services.GuildHideOutDataManager;
import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.fields.UpgradeInfoHolder;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.exceptions.ClanDoesNotExistException;
import me.xxgradzix.advancedclans.exceptions.PlayerDoesNotBelongToClanException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutDoesNotExistException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutUpgradeAlreadyBoughtException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.InvalidHideoutWorldNameException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.UpgradeWasNotBoughtException;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.Countdown;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.BlackSmithUpgradePattern;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.ResetHideoutPattern;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.StationHallUpgradePattern;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.UpgradePattern;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class GuildHideOutController {

    private final UserController userController;

    private final AdvancedGuilds plugin;

    private HashMap<GuildHideout.Upgrade, UpgradePattern> upgradePatterns = new HashMap<>();

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

        }
    }

    public void attemptTeleportByClickedBlock(Player player, Block block) {
        if(block == null) return;

        if(!block.hasMetadata("hideout")) return;

        String hideoutWorldName = block.getMetadata("hideout").get(0).asString();

        try {
            GuildHideOutDataManager.attemptTeleportToHideOut(player, hideoutWorldName);
        } catch (HideOutDoesNotExistException e) {

            MessageManager.sendMessageFormated(player, MessageManager.HIDEOUT_DOES_NOT_EXIST, MessageType.CHAT);
            return;

        } catch (ClanDoesNotExistException | PlayerDoesNotBelongToClanException e) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
            return;
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

    private void prepareHideOutHolograms(@NotNull GuildHideout guildHideout) throws InvalidObjectException {

        final String hideoutPanel = guildHideout.getWorldName() + "hideout_panel";
        final String mainStorage = guildHideout.getWorldName() + "hideout_storage";
        final String hideoutTrader = guildHideout.getWorldName() + "hideout_trader";

        DHAPI.removeHologram(hideoutPanel);
        DHAPI.removeHologram(mainStorage);
        DHAPI.removeHologram(hideoutTrader);

        World world = Bukkit.getWorld(guildHideout.getWorldName());

        if(world == null) throw new InvalidObjectException("World " + guildHideout.getWorldName() + " does not exist");

        DHAPI.createHologram(hideoutPanel, new Location(world, 0 , 100, 0));
        DHAPI.createHologram(mainStorage, new Location(world, 0 , 100, 0));
        DHAPI.createHologram(hideoutTrader, new Location(world, 0 , 100, 0));



    }
}
