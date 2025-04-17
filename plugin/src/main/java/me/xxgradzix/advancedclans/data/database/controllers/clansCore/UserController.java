package me.xxgradzix.advancedclans.data.database.controllers.clansCore;

import entities.User;
import lombok.Setter;
import me.xxgradzix.advancedclans.config.Config;
import me.xxgradzix.advancedclans.data.database.entities.clan.UserImpl;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataService;
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
        User userImpl = ClanAndUserDataService.getCachedUser(player.getUniqueId());
        if(userImpl ==null)
        {
            userImpl = new UserImpl(player, Config.defaultPoints);
            topRankScheduler.addUser(userImpl);
            ClanAndUserDataService.updateUser(userImpl);
        }
    }

    public void resetUser(User userImpl) {
        userImpl.setPoints(Config.defaultPoints);
        userImpl.resetKill();
        userImpl.resetDeath();

        ClanAndUserDataService.updateUser(userImpl);
    }
    public void resetPoints(User userImpl) {
        userImpl.setPoints(Config.defaultPoints);
        ClanAndUserDataService.updateUser(userImpl);
    }

    public void resetKill(User userImpl) {
        userImpl.resetKill();
        ClanAndUserDataService.updateUser(userImpl);
    }

    public void resetDeath(User userImpl) {
        userImpl.resetDeath();
        ClanAndUserDataService.updateUser(userImpl);
    }


    public void infoPlayer(Player player, User userImpl) {
        // get player object
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(userImpl.getUuid());

        OptionalInt clanRankIndexByTag = topRankScheduler.getUserRankByName(offlinePlayer.getName());
        int index = 9999;
        if(clanRankIndexByTag.isPresent())
            index = clanRankIndexByTag.getAsInt()+1;

        String clan = (!userImpl.hasClan()) ? "config.noneTag" : ColorFixer.addColors("config.formatTag.replace({tag}, " + userImpl.getClanTag());

        String infoMessage = MessageManager.PLAYER_INFO;

        infoMessage = infoMessage.replace("{player}", Objects.requireNonNull(offlinePlayer.getName()))
                .replace("{kills}", String.valueOf(userImpl.getKills()))
                .replace("{deaths}", String.valueOf(userImpl.getDeath()))
                .replace("{points}", String.valueOf(userImpl.getPoints()))
                .replace("{tag}", clan)
                .replace("{rank}", String.valueOf(index));


        MessageManager.sendMessageFormated(player, infoMessage, MessageType.CHAT);

    }

    public static Optional<User> findUserByUUID(UUID uuid) {
        return Optional.ofNullable(ClanAndUserDataService.getCachedUser(uuid));
    }
    public static void updateUser(User userImpl) {
        ClanAndUserDataService.updateUser(userImpl);
    }

    public Optional<User> findUserByPlayer(Player player) {
        return findUserByUUID(player.getUniqueId());
    }

    public void loadAllUsers() {
        ClanAndUserDataService.loadAllUsers();
    }
}
