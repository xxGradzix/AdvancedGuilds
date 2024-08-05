package me.xxgradzix.advancedclans.manager;

import lombok.Getter;
import lombok.Setter;
import me.xxgradzix.advancedclans.data.DataManager;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import me.xxgradzix.advancedclans.scheduler.TopRankScheduler;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class UserManager {

    private final DataManager dataManager;
    @Getter
    private final HashMap<UUID, User> userData = new HashMap<>();
    @Setter
    private TopRankScheduler topRankScheduler;


    public UserManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    public void loadUser(Player player)
    {
        User user = userData.get(player.getUniqueId());
        if(user==null)
        {
            user = new User(player, 1000); // todo get default points
            // add user to system of ranking
            topRankScheduler.addUser(user);
//             todo add to ranking system

            userData.put(player.getUniqueId(), user);
            dataManager.createUser(user);

        }
    }

    public void resetUser(User user) {
        user.setPoints(1000); // todo get default points
        user.resetKill();
        user.resetDeath();

        dataManager.updateUser(user);
    }
    public void resetPoints(User user) {
        user.setPoints(1000);
        dataManager.updateUser(user);
    } // todo get default points

    public void resetKill(User user) {
        user.resetKill();
        dataManager.updateUser(user);
    }

    public void resetDeath(User user) {
        user.resetDeath();
        dataManager.updateUser(user);
    }


    public void infoPlayer(Player player, User user) {
        // get player object
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(user.getUuid());

        OptionalInt clanRankIndexByTag = topRankScheduler.getUserRankByName(offlinePlayer.getName());
        int index = 9999;
        if(clanRankIndexByTag.isPresent())
            index = clanRankIndexByTag.getAsInt()+1;

        String clan = (user.getClan() == null) ? "config.noneTag" : ColorFixer.addColors("config.formatTag.replace({tag}, user.getClan().getTag())");

        String infoMessage = MessageManager.PLAYER_INFO;

        infoMessage = infoMessage.replace("{name}", Objects.requireNonNull(offlinePlayer.getName()))
                .replace("{kills}", String.valueOf(user.getKills()))
                .replace("{deaths}", String.valueOf(user.getDeath()))
                .replace("{points}", String.valueOf(user.getPoints()))
                .replace("{tag}", clan)
                .replace("{rank}", String.valueOf(index));


        MessageManager.sendMessageFormated(player, infoMessage, MessageType.CHAT);

    }

    public Optional<User> findUserByUUID(UUID uuid) {
        return Optional.ofNullable(userData.get(uuid));
    }

    public Optional<User> findUserByPlayer(Player player) {
        return findUserByUUID(player.getUniqueId());
    }

    public void loadAllUsers() {
        dataManager.getAllUsers().forEach(user -> userData.put(user.getUuid(), user));
    }
}
