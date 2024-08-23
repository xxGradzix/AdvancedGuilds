package me.xxgradzix.advancedclans.commands;

import me.xxgradzix.advancedclans.controllers.GuildHideOutController;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.services.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.data.database.services.GuildHideOutDataManager;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutDoesNotExistException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.InvalidHideoutWorldNameException;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.ExpeditionGui;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import org.bukkit.Bukkit;
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
//        if(!(commandSender instanceof Player player)) {
//            commandSender.sendMessage(MessageManager.ONLY_FOR_PLAYERS);
//            return false;
//        }

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
                    guildHideOutController.resetHideOutCompletelyOrCreate(world.getName());
                } catch (InvalidObjectException e) {
                    player.sendMessage("Taki świat nie istnieje");
                    return false;
                } catch (InvalidHideoutWorldNameException e) {
                    player.sendMessage("Kryjowke mozna otworzyc tylko w swiecie o nazwie z prefixem 'guild_'");
                    return false;
                }
            }
            case "2" -> {
                User cachedUser = ClanAndUserDataManager.getCachedUser(player.getUniqueId());
                try {
                    GuildHideOutDataManager.occupyHideOut(world.getName(), ClanAndUserDataManager.getCachedClan(cachedUser.getClanTag()));
                } catch (HideOutDoesNotExistException e) {
                    player.sendMessage(MessageManager.HIDEOUT_DOES_NOT_EXIST);
                }
            }
            case "3" -> {
                Location location = player.getLocation();
                String worldName = strings[1];
                try {
                    guildHideOutController.setOperatingLocationForHideout(worldName, location);
                } catch (HideOutDoesNotExistException e) {
                    MessageManager.sendMessageFormated(player, MessageManager.HIDEOUT_DOES_NOT_EXIST, MessageType.CHAT);
                }
            }
            case "4" -> {
                String playername = strings[1];

                Player player1 = Bukkit.getPlayer(playername);
                ExpeditionGui.openExpeditionGui(player1);
            }
            case "5" -> {
                ExpeditionGui.shuffleExpeditions();
            }
        }
        return false;
    }
}
