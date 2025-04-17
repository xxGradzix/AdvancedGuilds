package me.xxgradzix.advancedclans.listener.clan;

import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.config.Config;
import me.xxgradzix.advancedclans.data.database.entities.clan.User;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataService;
import me.xxgradzix.advancedclans.data.database.entities.clans.AntySystemRank;
import me.xxgradzix.advancedclans.events.clan.PointsChangeUserEvent;
import me.xxgradzix.advancedclans.data.database.controllers.clansCore.UserController;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import me.xxgradzix.advancedclans.utils.SystemPoint;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.UUID;


public class PlayerDeathListener implements Listener {

    private final AdvancedGuilds plugin;
    private final UserController userController;
    private HashMap<UUID, AntySystemRank> antySystem = new HashMap<>();

    public PlayerDeathListener(AdvancedGuilds plugin, UserController userController) {
        this.plugin = plugin;
        this.userController = userController;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        User userDeath = ClanAndUserDataService.getCachedUser(player.getUniqueId());

        if(userDeath == null)
            return;

        // increase death
        userDeath.increaseDeath();

        if(killer == null) {
            // message after the death
            if(!Config.shouldSendDeathMessage)
                return;

            MessageManager.broadcastMessageFormated(MessageManager.BROADCAST_DEATH_MESSAGE_NO_KILLER.replace("{victim}", player.getName()), MessageType.CHAT);

            return;
        }

        User userKiller = ClanAndUserDataService.getCachedUser(killer.getUniqueId());
        if(userKiller == null)
            return;


        userKiller.increaseKill();

        if (Config.antiAbuseSystemEnabled) {
            String playerIp = player.getAddress().getAddress().getHostAddress();
            AntySystemRank antySystemRank = antySystem.get(killer.getUniqueId());

            if (antySystemRank != null) {
                if (!antySystemRank.isPlayerKillable(playerIp)) {
                    int second = SystemPoint.roundUpToMinutes(antySystemRank.getRemainingCooldown(playerIp));
                    MessageManager.sendMessageFormated(player, MessageManager.MESSAGE_COOLDOWN_KILL.replace("{time}", String.valueOf(second)), MessageType.CHAT);
                    return;
                }
                antySystemRank.addCooldown(playerIp, Config.antiSystemRankCooldown);
            } else {
                antySystem.put(killer.getUniqueId(), new AntySystemRank(
                        killer.getAddress().getAddress().getHostAddress(),
                        playerIp,
                        Config.antiSystemRankCooldown
                ));
            }
        }

        int newPointDeath = SystemPoint.calculateEloRating(userDeath.getPoints(), userKiller.getPoints(), 0);
        int newPointKiller = SystemPoint.calculateEloRating(userKiller.getPoints(), userDeath.getPoints(), 1);

        int deathPointTake = userDeath.getPoints()-newPointDeath;
        int killerPointAdd = newPointKiller-userKiller.getPoints();

        PointsChangeUserEvent pointsChangeUserEvent = new PointsChangeUserEvent(killer, player, killerPointAdd, deathPointTake);
        Bukkit.getPluginManager().callEvent(pointsChangeUserEvent);
        if(pointsChangeUserEvent.isCancelled())
            return;

        if(newPointDeath>=0)
        {
            userKiller.addPoint(pointsChangeUserEvent.getPointKiller());
            userDeath.takePoint(pointsChangeUserEvent.getPointVictim());
        }

        // message after the death
        if(!Config.shouldSendDeathMessage)
            return;


        MessageManager.broadcastMessageFormated(MessageManager.BROADCAST_DEATH_MESSAGE.replace("{killer-points-change}", String.valueOf(pointsChangeUserEvent.getPointKiller())).replace("{victim-points-change}", String.valueOf(pointsChangeUserEvent.getPointVictim())).replace("{victim}", player.getName()).replace("{killer}", killer.getName()), MessageType.CHAT);
    }

}
