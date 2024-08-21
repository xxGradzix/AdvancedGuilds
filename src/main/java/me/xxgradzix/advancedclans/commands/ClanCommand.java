package me.xxgradzix.advancedclans.commands;

import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.services.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.controllers.ClanController;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class ClanCommand implements CommandExecutor, TabCompleter {

    private final ClanController clanController;

    public ClanCommand(ClanController clanController) {
        this.clanController = clanController;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 1) {
            return Arrays.stream(SubCommands.values()).map(SubCommands::name).map(String::toLowerCase).toList();
        }
        if(strings.length == 2) {
            if(strings[0].equalsIgnoreCase(SubCommands.INFO.name().toLowerCase()) || strings[0].equalsIgnoreCase(SubCommands.USUN.name().toLowerCase())) {
                return ClanAndUserDataManager.getAllCachedClans().stream().map(Clan::getTag).toList();
            }
        }
        return null;
    }

    private enum SubCommands {
        INFO,
        USUN,
        STWORZ,
        OPUSC,
        USTAWLIDERA,
        USTAWZASTEPCE,
        ZAPROS,
        WYRZUC,
        DOLACZ,
        PVP
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {

        if(!(sender instanceof Player player)) {
            sender.sendMessage(MessageManager.ONLY_FOR_PLAYERS);
            return false;
        }

        if(args.length == 0) {
            sendHelpMessage(player);
            return false;
        }

        SubCommands subCommand;
        try {
            subCommand = SubCommands.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            sendHelpMessage(player);
            return false;
        }

        switch (subCommand) {
            case INFO: {
                    if (args.length != 2) {
                        sendHelpMessage(player);
                        return false;
                    }
                    Clan clan = clanController.getClan(args[1]);
                    if (clan == null) {
                        MessageManager.sendMessageFormated(player, MessageManager.CLAN_NOT_FOUND.replace("{clan}", args[1]), MessageType.CHAT);
                        return false;
                    }
                    clanController.infoClan(player, clan);
                }
                break;
            case USUN: {
                    if (args.length != 1) {
                        sendHelpMessage(player);
                        return false;
                    }
                    clanController.deleteClan(player);
                }
                break;
            case STWORZ: {
                    if (args.length != 2) {
                        sendHelpMessage(player);
                        return false;
                    }
                    clanController.createClan(player, args[1]);
                }
                break;
            case OPUSC: {
                    if (args.length != 1) {
                        sendHelpMessage(player);
                        return false;
                    }
                    clanController.leaveClan(player);
                }
                break;
            case USTAWLIDERA: {
                    if (args.length != 2) {
                        sendHelpMessage(player);
                        return false;
                    }
                    Player target = player.getServer().getPlayer(args[1]);
                    if (target == null) {
                        MessageManager.sendMessageFormated(player, MessageManager.PLAYER_NOT_FOUND, MessageType.CHAT);
                        return false;
                    }
                    clanController.setOwner(player, target);
                }
                break;
            case USTAWZASTEPCE: {
                    if (args.length != 2) {
                        sendHelpMessage(player);
                        return false;
                    }
                    Player target = player.getServer().getPlayer(args[1]);
                    if (target == null) {
                        MessageManager.sendMessageFormated(player, MessageManager.PLAYER_NOT_FOUND, MessageType.CHAT);
                        return false;
                    }
                    clanController.setDeputy(player, target);
                }
                break;
            case ZAPROS: {
                    if (args.length != 2) {
                        sendHelpMessage(player);
                        return false;
                    }
                    Player target = player.getServer().getPlayer(args[1]);
                    if (target == null) {
                        MessageManager.sendMessageFormated(player, MessageManager.PLAYER_NOT_FOUND, MessageType.CHAT);
                        return false;
                    }
                    clanController.inviteUser(player, target);
                }
                break;
            case WYRZUC: {
                    if (args.length != 2) {
                        sendHelpMessage(player);
                        return false;
                    }
                    clanController.kickUser(player, args[1]);
                }
                break;
            case PVP: {
                    if (args.length != 1) {
                        sendHelpMessage(player);
                        return false;
                    }
                    clanController.changePvpStatus(player);
                }
                break;
            case DOLACZ: {
                if (args.length != 2) {
                    sendHelpMessage(player);
                    return false;
                }
                Clan clan = clanController.getClan(args[1]);
                if (clan == null) {
                    MessageManager.sendMessageFormated(player, MessageManager.CLAN_NOT_FOUND.replace("{clan}", args[1]), MessageType.CHAT);
                    return false;
                }
                clanController.joinClan(player, clan);
                }
                break;
        }
        return true;
    }

    private void sendHelpMessage(Player player) {
        MessageManager.sendMessageFormated(player, MessageManager.HELP_MESSAGE, MessageType.CHAT);
    }

}
