package me.xxgradzix.advancedclans.controllers;

import lombok.Setter;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.services.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import me.xxgradzix.advancedclans.scheduler.TopRankScheduler;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

@Setter
public class UserController {

    private TopRankScheduler topRankScheduler;

    public void loadUser(Player player)
    {
        User user = ClanAndUserDataManager.getCachedUser(player.getUniqueId());
        if(user==null)
        {
            user = new User(player, 1000); // todo get default points
            topRankScheduler.addUser(user);
            ClanAndUserDataManager.updateUser(user);
        }
    }

    public void resetUser(User user) {
        user.setPoints(1000); // todo get default points
        user.resetKill();
        user.resetDeath();

        ClanAndUserDataManager.updateUser(user);
    }
    public void resetPoints(User user) {
        user.setPoints(1000); // todo get default points
        ClanAndUserDataManager.updateUser(user);
    }

    public void resetKill(User user) {
        user.resetKill();
        ClanAndUserDataManager.updateUser(user);
    }

    public void resetDeath(User user) {
        user.resetDeath();
        ClanAndUserDataManager.updateUser(user);
    }


    public void infoPlayer(Player player, User user) {
        // get player object
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(user.getUuid());

        OptionalInt clanRankIndexByTag = topRankScheduler.getUserRankByName(offlinePlayer.getName());
        int index = 9999;
        if(clanRankIndexByTag.isPresent())
            index = clanRankIndexByTag.getAsInt()+1;

        String clan = (!user.hasClan()) ? "config.noneTag" : ColorFixer.addColors("config.formatTag.replace({tag}, user.getClan().getTag())");

        String infoMessage = MessageManager.PLAYER_INFO;

        infoMessage = infoMessage.replace("{name}", Objects.requireNonNull(offlinePlayer.getName()))
                .replace("{kills}", String.valueOf(user.getKills()))
                .replace("{deaths}", String.valueOf(user.getDeath()))
                .replace("{points}", String.valueOf(user.getPoints()))
                .replace("{tag}", clan)
                .replace("{rank}", String.valueOf(index));


        MessageManager.sendMessageFormated(player, infoMessage, MessageType.CHAT);

    }

    public static Optional<User> findUserByUUID(UUID uuid) {
        return Optional.ofNullable(ClanAndUserDataManager.getCachedUser(uuid));
    }
    public static void updateUser(User user) {
        ClanAndUserDataManager.updateUser(user);
    }

    public Optional<User> findUserByPlayer(Player player) {
        return findUserByUUID(player.getUniqueId());
    }

    public void loadAllUsers() {
        ClanAndUserDataManager.loadAllUsers();
    }
}
