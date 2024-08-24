package me.xxgradzix.advancedclans.listener;

import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.config.Config;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.services.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.entities.AntySystemRank;
import me.xxgradzix.advancedclans.events.PointsChangeUserEvent;
import me.xxgradzix.advancedclans.controllers.UserController;
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
//    private LangMessage lang;
//    private Config config;

    private final UserController userController;
    private HashMap<UUID, AntySystemRank> antySystem = new HashMap<>();
    public PlayerDeathListener(AdvancedGuilds plugin, UserController userController)
    {
        this.plugin = plugin;
//        this.config = plugin.getConfigPlugin();
//        this.lang = plugin.lang;
        this.userController = userController;
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        User userDeath = ClanAndUserDataManager.getCachedUser(player.getUniqueId());

        if(userDeath == null)
            return;

        // increase death
        userDeath.increaseDeath();

        if(killer == null) {
            // message after the death
            if(!Config.shouldSendDeathMessage)
                return;

            // TODO send message to all players about death
//            MessageUtil.broadcast(
//                    ColorFixer.addColors(
//                            lang.langBroadcastDeathNoVictimInfo
//                                    .replace("{victim}", player.getName())
//                    )
//            );
            return;
        }

        User userKiller = ClanAndUserDataManager.getCachedUser(killer.getUniqueId());
        if(userKiller == null)
            return;


        userKiller.increaseKill();

        if (Config.antiAbuseSystemEnabled) {
            String playerIp = player.getAddress().getAddress().getHostAddress();
            AntySystemRank antySystemRank = antySystem.get(killer.getUniqueId());

            if (antySystemRank != null) {
                if (!antySystemRank.isPlayerKillable(playerIp)) {
                    int second = SystemPoint.roundUpToMinutes(antySystemRank.getRemainingCooldown(playerIp));
//                    MessageUtil.sendMessage(killer, lang.cooldownKill.replace("{time}", String.valueOf(second)));
                    // TODO SEND message to killer about cooldown to kill
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

        // TODO send message to all players about death and points change
//        MessageUtil.broadcast(
//                ColorFixer.addColors(
//                        lang.langBroadcastDeathInfo
//                                .replace("{victim}", player.getName())
//                                .replace("{killer}", killer.getName())
//                                .replace("{victim-points}", String.valueOf(pointsChangeUserEvent.getPointVictim()))
//                                .replace("{killer-points}", String.valueOf(pointsChangeUserEvent.getPointKiller()))
//                )
//        );
    }

}
