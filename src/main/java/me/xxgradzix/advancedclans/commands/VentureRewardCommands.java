package me.xxgradzix.advancedclans.commands;

import me.xxgradzix.advancedclans.controllers.GuildHideOutController;
import me.xxgradzix.advancedclans.controllers.VentureRewardController;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.services.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.data.database.services.GuildHideOutDataManager;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutDoesNotExistException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.InvalidHideoutWorldNameException;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.ExpeditionDto;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.ExpeditionGui;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InvalidObjectException;
import java.util.Arrays;
import java.util.List;

public class VentureRewardCommands implements CommandExecutor, TabCompleter {

    private final VentureRewardController ventureRewardController;

    public VentureRewardCommands(VentureRewardController ventureRewardController) {
        this.ventureRewardController = ventureRewardController;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(commandSender instanceof Player player)) {
            commandSender.sendMessage(MessageManager.ONLY_FOR_PLAYERS);
            return false;
        }

        if(args.length != 4) {
            player.sendMessage("Usage: /venturereward <objective> <level> <minAmount> <maxAmount>");
            return false;
        }

        ExpeditionDto.ExpeditionObjective objective;
        try {
            objective = ExpeditionDto.ExpeditionObjective.valueOf(args[0]);
        } catch (IllegalArgumentException e) {
            player.sendMessage("Invalid objective");
            return false;
        }

        int level;
        try {
            level = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid level");
            return false;
        }
        int minAmount;
        try {
            minAmount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid min amount");
            return false;
        }
        int maxAmount;
        try {
            maxAmount = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid max amount");
            return false;
        }
        ItemStack reward = player.getInventory().getItemInMainHand();
        if(reward == null || reward.getType().isAir()) {
            player.sendMessage("You must hold an item in your hand");
            return false;
        }

        ventureRewardController.addVentureReward(player, objective, level, minAmount, maxAmount, reward);

        ExpeditionGui.refreshExpeditionRewards(objective, level);

        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 1) {
            return Arrays.stream(ExpeditionDto.ExpeditionObjective.values()).map(Enum::name).toList();
        }
        return List.of();
    }
}
