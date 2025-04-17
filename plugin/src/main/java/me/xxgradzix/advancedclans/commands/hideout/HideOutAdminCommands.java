package me.xxgradzix.advancedclans.commands.hideout;

import me.xxgradzix.advancedclans.data.database.controllers.hideouts.GuildHideOutController;
import me.xxgradzix.advancedclans.data.database.entities.clan.User;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataService;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutDoesNotExistException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.InvalidHideoutWorldNameException;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.GuildPanel.GideoutGui;
import me.xxgradzix.advancedclans.guildshideoutsystem.HideoutGeneralItemManager;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition.ExpeditionGui;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition.ExpeditionItemManager;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.InvalidObjectException;

public class HideOutAdminCommands implements CommandExecutor {

     private final GuildHideOutController guildHideOutController;

    public HideOutAdminCommands(GuildHideOutController guildHideOutController) {
        this.guildHideOutController = guildHideOutController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        Player player = (Player) commandSender;

        World world = null;

        if(player != null) {
            world = player.getWorld();
        }

        String temp;
        try {
            temp = strings[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            temp = "1";
        }

        switch (temp) {
            case "1" -> {
                try {
                    GuildHideOutController.resetHideOutCompletelyOrCreate(world.getName());
                } catch (InvalidObjectException e) {
                    player.sendMessage("Taki świat nie istnieje");
                    return false;
                } catch (InvalidHideoutWorldNameException e) {
                    player.sendMessage("Kryjowke mozna otworzyc tylko w swiecie o nazwie z prefixem 'guild_'");
                    return false;
                }
            }
            case "2" -> {
                User cachedUser = ClanAndUserDataService.getCachedUser(player.getUniqueId());
                try {
                    GuildHideOutController.occupyHideOut(world.getName(), ClanAndUserDataService.getCachedClan(cachedUser.getClanTag()));
                } catch (HideOutDoesNotExistException e) {
                    player.sendMessage(MessageManager.HIDEOUT_DOES_NOT_EXIST);
                }
            }
            case "3" -> {
                Location location = player.getLocation();
                String worldName = strings[1];
                try {
                    GuildHideOutController.setOperatingLocationForHideout(worldName, location);
                } catch (HideOutDoesNotExistException e) {
                    MessageManager.sendMessageFormated(player, MessageManager.HIDEOUT_DOES_NOT_EXIST, MessageType.CHAT);
                }
            }
            case "5" -> {
                ExpeditionGui.shuffleExpeditions();
            }
            case "6" -> {
                player.getInventory().addItem(ExpeditionItemManager.foodRationTier1);
                player.getInventory().addItem(ExpeditionItemManager.foodRationTier2);
                player.getInventory().addItem(ExpeditionItemManager.foodRationTier3);
                player.getInventory().addItem(ExpeditionItemManager.toolTier1);
                player.getInventory().addItem(ExpeditionItemManager.toolTier2);
                player.getInventory().addItem(ExpeditionItemManager.toolTier3);
                player.getInventory().addItem(HideoutGeneralItemManager.getPremiumGuildCoin());
            }
            case "7" -> {
                ExpeditionGui.openExpeditionGui(player);

            }

            case "8" -> {
                new GideoutGui(player);
            }
            default -> {

                GuildHideout hideout = GuildHideOutController.getHideOut(temp);

                if(hideout == null) {
                    player.sendMessage("Kryjowka o takim ID nie istnieje");
                    return false;
                }


                String action;
                try {
                    action = strings[1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    player.sendMessage("Podaj akcje: dodajxp, dodajpoziom, ulepsz");
                    return false;
                }

                switch (action) {
                    case "dodajxp" -> {
                        int xp;
                        try {
                            xp = Integer.parseInt(strings[2]);
                        } catch (NumberFormatException e) {
                            player.sendMessage("Podaj poprawna liczbe");
                            return false;
                        }
//                        hideout.addExperience(xp);
                        GuildHideOutController.addXPToHideout(hideout.getHideoutID(), xp);
                    }
                    case "dodajpoziom" -> {
                        int level;
                        try {
                            level = Integer.parseInt(strings[2]);
                        } catch (NumberFormatException e) {
                            player.sendMessage("Podaj poprawna liczbe");
                            return false;
                        }
//                        hideout.setHideoutLevel(level);
//                        GuildHideOutController.setLevelForHideout(hideout.getHideoutID(), level);
                    }
                    case "ulepsz" -> {
                        hideout.upgradeHideout();
                        GuildHideOutController.upgradeHideoutLevel(hideout);
                    }
                }



            }
        }
        return false;
    }
}
