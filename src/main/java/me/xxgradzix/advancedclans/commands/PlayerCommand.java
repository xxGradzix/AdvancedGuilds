package me.xxgradzix.advancedclans.commands;

import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.services.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.controllers.UserController;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class PlayerCommand implements CommandExecutor {

    private final UserController userController;

    public PlayerCommand(UserController userController) {
        this.userController = userController;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(!(commandSender instanceof Player player)) {
            commandSender.sendMessage(MessageManager.ONLY_FOR_PLAYERS);
            return false;
        }

        if(strings.length != 1) {
            sendHelpMessage(player);
        }

        String targetName = strings[0];

        Optional<UUID> targetUUIDOptional = getPlayerUUIDByNickname(targetName);

        if(targetUUIDOptional.isEmpty()) {
            MessageManager.sendMessageFormated(player, MessageManager.PLAYER_NOT_FOUND, MessageType.CHAT);
            return false;
        }

        User user = ClanAndUserDataManager.getCachedUser(targetUUIDOptional.get());

        if(user==null) {
            MessageManager.sendMessageFormated(player, MessageManager.PLAYER_NOT_FOUND, MessageType.CHAT);
            return false;
        }

        userController.infoPlayer(player, user);

        return false;
    }

    private void sendHelpMessage(Player player) {
        MessageManager.sendMessageFormated(player, MessageManager.PLAYER_HELP_MESSAGE, MessageType.CHAT);
    }

    private Optional<UUID> getPlayerUUIDByNickname(String nickname) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayerIfCached(nickname);
        if(offlinePlayer!=null) {
            return Optional.of(offlinePlayer.getUniqueId());
        }
        Player player = Bukkit.getPlayer(nickname);
        if(player==null) {
            return Optional.empty();
        }
        return Optional.of(player.getUniqueId());
    }
}
