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
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.services.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.data.database.services.GuildHideOutDataManager;
import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.fields.UpgradeInfoHolder;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutUpgradeAlreadyBoughtException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.UpgradeWasNotBoughtException;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.Countdown;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.BlackSmithUpgradePattern;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.StationHallUpgradePattern;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.UpgradePattern;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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

        }
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

    public void loadHideOuts() {
        List<GuildHideout> guildHideouts = GuildHideOutDataManager.loadAllHideOuts();

        for (GuildHideout guildHideout : guildHideouts) {
            guildHideout.getHideoutUpgrades().forEach((upgrade, upgradeInfoHolder) -> {
                if(upgradeInfoHolder.isBought() && !upgradeInfoHolder.isFinished()) {
                    scheduleUpgrade(guildHideout, upgrade);
                }
            });
        }

    }
}
