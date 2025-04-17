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
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.controllers.clansCore.UserController;
import me.xxgradzix.advancedclans.data.database.entities.clan.Clan;
import me.xxgradzix.advancedclans.data.database.entities.hideout.fields.Upgrade;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataService;
import me.xxgradzix.advancedclans.data.database.services.hideout.GuildHideOutDataService;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.clan.User;
import me.xxgradzix.advancedclans.exceptions.clan.ClanDoesNotExistException;
import me.xxgradzix.advancedclans.exceptions.clan.PlayerDoesNotBelongToClanException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutDoesNotExistException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.InvalidHideoutWorldNameException;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InvalidObjectException;
import java.util.*;

public class GuildHideOutController {

    private static UserController userController;

    private static GuildHideOutDataService guildHideOutDataService;
    
    private static final AdvancedGuilds plugin = AdvancedGuilds.instance;

    public GuildHideOutController(UserController userController, GuildHideOutDataService guildHideOutDataService) {
        GuildHideOutController.userController = userController;
        GuildHideOutController.guildHideOutDataService = guildHideOutDataService;
    }

    public static final String DEFAULT_HIDEOUT_HOLOGRAM_SUFFIX = "_default_hideout_hologram";
    public static final String OCCUPIED_HIDEOUT_HOLOGRAM_SUFFIX = "_occupied_hideout_hologram";

    public static void resetOrCreateHideOut(String hideoutId) {
        guildHideOutDataService.resetOrCreateHideOut(hideoutId);
    }

    public static void occupyHideout(Player player, GuildHideout guildHideout) throws HideOutDoesNotExistException {

        User user = userController.findUserByPlayer(player).orElseThrow(() -> new RuntimeException("User not found"));

        Clan clan = ClanAndUserDataService.getCachedClan(user.getClanTag());

        if(clan == null) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_ANY_CLAN, MessageType.CHAT);
            return;
        }

        if(guildHideOutDataService.isHideoutOccupied(guildHideout)) {
            MessageManager.sendMessageFormated(player, MessageManager.HIDEOUT_ALREADY_OCCUPIED, MessageType.CHAT);
            return;
        }

        guildHideOutDataService.occupyHideOut(guildHideout, clan);
    }

    public static void setOperatingLocationForHideout(String guildHideout, Location location) throws HideOutDoesNotExistException {
        GuildHideout guildHideout1 = guildHideOutDataService.getHideOut(guildHideout);
        if (guildHideout1 == null) throw new HideOutDoesNotExistException("Hideout " + guildHideout + " does not exist");
        guildHideOutDataService.setHideOutOperatingLocation(guildHideout1, location);
    }

    public static void attemptTeleportByHologram(Player player, String hideoutName) {

        User user = userController.findUserByPlayer(player).orElseThrow(() -> new RuntimeException("User not found"));

        Clan clan = ClanAndUserDataService.getCachedClan(user.getClanTag());

        if(clan == null && !player.isOp()) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
            return;
        }

        GuildHideout attemptedHideout = guildHideOutDataService.getHideOut(hideoutName);


        String guildHideoutName = clan.getHideoutId();

        if((attemptedHideout == null || guildHideoutName == null || attemptedHideout.getHideoutID().equals(guildHideoutName) && !player.isOp())) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
            return;
        }


        try {
            guildHideOutDataService.attemptTeleportToHideOut(player, attemptedHideout);

            for (int i = 0; i < 4; i++) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, SoundCategory.MASTER, 3000, 0.1f), i * 5);
            }

        } catch (HideOutDoesNotExistException e) {

            MessageManager.sendMessageFormated(player, MessageManager.HIDEOUT_DOES_NOT_EXIST, MessageType.CHAT);
        } catch (ClanDoesNotExistException | PlayerDoesNotBelongToClanException e) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
        }
    }

    public static void attemptTeleportToOutpost(Player player, String hideoutName) {

        User user = userController.findUserByPlayer(player).orElseThrow(() -> new RuntimeException("User not found"));

        Clan clan = ClanAndUserDataService.getCachedClan(user.getClanTag());

        if(clan == null && !player.isOp()) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
            return;
        }


        GuildHideout attemptedHideout = guildHideOutDataService.getHideOut(hideoutName);

        String guildHideoutName = clan.getHideoutId();

        if((attemptedHideout == null || guildHideoutName == null) && !player.isOp()) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
            return;
        }


        try {
            guildHideOutDataService.attemptTeleportToOutpost(player, attemptedHideout);

            for (int i = 0; i < 4; i++) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, SoundCategory.MASTER, 3000, 0.1f), i * 5);
            }

        } catch (HideOutDoesNotExistException e) {

            MessageManager.sendMessageFormated(player, MessageManager.HIDEOUT_DOES_NOT_EXIST, MessageType.CHAT);
        } catch (ClanDoesNotExistException | PlayerDoesNotBelongToClanException e) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
        }

    }

    public static void upgradeHideOut(GuildHideout guildHideout, Upgrade upgrade) {
        guildHideOutDataService.upgradeHideOut(guildHideout, upgrade);
    }

    public static GuildHideout getPlayerHideOut(Player player) {
        Optional<User> optionalUser = userController.findUserByPlayer(player);

        if(optionalUser.isEmpty()) try {
            throw new InvalidObjectException("Dla gracza " + player.getName() + " nie ma usera");
        } catch (InvalidObjectException e) {
            throw new RuntimeException(e);
        }

        User user = optionalUser.get();

        if(!user.hasClan()) return null;

        if(!ClanAndUserDataService.getCachedClan(user.getClanTag()).hasHideout()) return null;

        return guildHideOutDataService.getHideOut(ClanAndUserDataService.getCachedClan(user.getClanTag()).getHideoutId());

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


    public static void loadHideOuts() {
        guildHideOutDataService.loadHideOuts();
    }


    public static GuildHideout getHideOut(@NotNull String name) {
        return guildHideOutDataService.getHideOut(name);
    }

    public static void resetHideOutCompletelyOrCreate(@NotNull String name) throws InvalidObjectException, InvalidHideoutWorldNameException {
        guildHideOutDataService.resetHideOutCompletelyOrCreate(name);
    }

    public static void occupyHideOut(@NotNull String hideoutName, Clan cachedClan) throws HideOutDoesNotExistException {
        GuildHideout guildHideout = guildHideOutDataService.getHideOut(hideoutName);
        if(guildHideout == null) throw new HideOutDoesNotExistException("Hideout " + hideoutName + " does not exist");
        guildHideOutDataService.occupyHideOut(guildHideout, cachedClan);
    }

    public static GuildHideout getHideoutByLocation(@NotNull Location location) {
        return guildHideOutDataService.getHideoutByLocation(location);
    }

    public static void addXPToHideout(String hideoutId, int xp) {
        GuildHideout guildHideout = getHideOut(hideoutId);
        if(guildHideout == null) return;
        guildHideOutDataService.addXPToHideout(guildHideout, xp);
    }
    public static void upgradeHideoutLevel(GuildHideout guildHideout) {
        if(guildHideout == null) return;
        guildHideOutDataService.upgradeHideoutLevel(guildHideout);
    }
}
