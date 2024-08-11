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
import me.xxgradzix.advancedclans.data.database.services.GuildHideOutDataManager;
import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.UpgradeInfoHolder;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.Countdown;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.BlackSMithUpgrade;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.StationHallUpgrade;
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

    private final GuildHideOutDataManager guildHideOutDataManager;
    private final UserController userController;

    private final AdvancedGuilds plugin;

    private HashMap<GuildHideout.Upgrade, UpgradePattern> upgradePatterns = new HashMap<>();

    public UpgradePattern getUpgradePattern(GuildHideout.Upgrade upgrade) {
        return upgradePatterns.get(upgrade);
    }

    public GuildHideOutController(GuildHideOutDataManager guildHideOutDataManager, UserController userController, AdvancedGuilds plugin) {
        this.guildHideOutDataManager = guildHideOutDataManager;
        this.userController = userController;
        this.plugin = plugin;

        {

            UpgradePattern stationHallUpgrade = new StationHallUpgrade(plugin);
            upgradePatterns.put(GuildHideout.Upgrade.STATION_HALL, stationHallUpgrade);

            UpgradePattern blacksmithUpgrade = new BlackSMithUpgrade(plugin);
            upgradePatterns.put(GuildHideout.Upgrade.BLACKSMITH, blacksmithUpgrade);

        }
    }

    public void upgradeHideOut(@NotNull GuildHideout hideout, GuildHideout.Upgrade upgrade) {

        if(hideout.hasBoughtUpgrade(upgrade)){
            // TODO thorw exception
            return;
        }

        UpgradeInfoHolder upgradeHolder = hideout.getUpgradeHolder(upgrade);

        final UpgradePattern upgradePattern = upgradePatterns.get(upgrade);

        if(upgradePattern == null){
            Bukkit.getLogger().log(Level.SEVERE, "Error: Upgrade pattern not found for upgrade: " + upgrade.name());
            return;
        }

        final int upgradeTimeSeconds = upgradePattern.getCoolDown();

        if(upgradeHolder == null){
            upgradeHolder = new UpgradeInfoHolder( (System.currentTimeMillis() + 1000 * upgradeTimeSeconds), true);
        } else {
            upgradeHolder.setBought(true);
            upgradeHolder.setTimeOfCompletion(System.currentTimeMillis() + 1000 * upgradeTimeSeconds);
        }

        hideout.setUpgradeHolder(upgrade, upgradeHolder);

        // TODO update hideout in database
        guildHideOutDataManager.updateHideOut(hideout);

        scheduleUpgrade(hideout, upgrade);
    }

    public void scheduleUpgrade(GuildHideout hideout, GuildHideout.Upgrade upgrade) {
        UpgradeInfoHolder upgradeHolder = hideout.getUpgradeHolder(upgrade);

        final UpgradePattern upgradePattern = upgradePatterns.get(upgrade);

        if(upgradePattern == null){
            Bukkit.getLogger().log(Level.SEVERE, "Error: Upgrade pattern not found for upgrade: " + upgrade.name());
            return;
        }

        Countdown countdown = new Countdown(plugin, Math.toIntExact(upgradeHolder.getTimeTOCompletionSeconds()), upgradePattern.getHologramLocation(Bukkit.getWorld(hideout.getWorldName())), () -> {

            Bukkit.broadcastMessage("Zakupiono ulepszenie: " + upgrade.name()); // TODO CHANGE MESSAGE
            Location loc = new Location(Bukkit.getWorld(hideout.getWorldName()), 0, 100, 0);
            paste(loc, upgradePattern.getSchemFile());
            guildHideOutDataManager.updateHideOut(hideout);

        });
        countdown.scheduleTimer();
    }

//    public void upgradeHideOut(Player player, GuildHideout.Upgrade upgrade) {
//
//        Optional<User> optionalUser = userManager.findUserByPlayer(player);
//
//        if(optionalUser.isEmpty()){
//            player.sendMessage("Wystąpił błąd z Twoim kontem, zgłoś to do administracji.");
//            Bukkit.getLogger().log(Level.SEVERE, "Error: User not found for player: " + player.getName());
//            return;
//        }
//
//        User user = optionalUser.get();
//
//        if(!user.hasClan()) {
//            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_ANY_CLAN, MessageType.CHAT);
//            return;
//        }
//
//        Clan clan = user.getClan();
//
//        GuildHideout hideout = clan.getHideout();
//
//        if(hideout == null) {
//            MessageManager.sendMessageFormated(player, MessageManager.YOUR_CLAN_DOES_NOT_HAVE_HIDEOUT, MessageType.CHAT);
//            return;
//        }
//
//        if(hideout.hasBoughtUpgrade(upgrade)){
//            MessageManager.sendMessageFormated(player, MessageManager.HIDEOUT_ALREADY_HAVE_THIS_UPGRADE, MessageType.CHAT);
//            return;
//        }
//
//        UpgradeInfoHolder upgradeHolder = hideout.getUpgradeHolder(upgrade);
//
//        final UpgradePattern upgradePattern = upgradePatterns.get(upgrade);
//
//        if(upgradePattern == null){
//            Bukkit.getLogger().log(Level.SEVERE, "Error: Upgrade pattern not found for upgrade: " + upgrade.name());
//            return;
//        }
//
//        final int upgradeTimeSeconds = upgradePattern.getCoolDown();
//
//        if(upgradeHolder == null){
//            upgradeHolder = new UpgradeInfoHolder( (System.currentTimeMillis() + 1000 * upgradeTimeSeconds), true);
//        } else {
//            upgradeHolder.setBought(true);
//            upgradeHolder.setTimeOfCompletion(System.currentTimeMillis() + 1000 * upgradeTimeSeconds);
//        }
//
//        hideout.setUpgradeHolder(upgrade, upgradeHolder);
//
//        World world = Bukkit.getWorld(hideout.getWorldName());
//
//        Countdown countdown = new Countdown(plugin, upgradeTimeSeconds, upgradePattern.getHologramLocation(world), () -> {
//
//            Bukkit.broadcastMessage("Zakupiono ulepszenie: " + upgrade.name()); // TODO CHANGE MESSAGE
//            Location loc = new Location(world, 0, 100, 0);
//            paste(loc, upgradePattern.getSchemFile());
//            guildHideOutDataManager.updateHideOut(hideout);
//
//        });
//
//        countdown.scheduleTimer();
//    }

    private static void paste(Location location, File file) {
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

        if(user.getClan().hasHideOut()) return null;

        return user.getClan().getHideout();

    }

    public void loadHideOuts() {
        List<GuildHideout> guildHideouts = guildHideOutDataManager.loadAllHideOuts();

        for (GuildHideout guildHideout : guildHideouts) {
            guildHideout.getHideoutUpgrades().forEach((upgrade, upgradeInfoHolder) -> {
                if(upgradeInfoHolder.isBought() && !upgradeInfoHolder.isFinished()) {
                    scheduleUpgrade(guildHideout, upgrade);
                }
            });
        }

    }
}
