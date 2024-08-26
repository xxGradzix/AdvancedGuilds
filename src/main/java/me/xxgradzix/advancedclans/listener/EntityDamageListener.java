package me.xxgradzix.advancedclans.listener;

import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.config.Config;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.data.database.controllers.clansCOre.UserController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageListener implements Listener {

    private final AdvancedGuilds plugin;

    private final UserController userController;

//    private Config config;
    public EntityDamageListener(AdvancedGuilds plugin, UserController userController)
    {
        this.plugin = plugin;
//        this.config = plugin.getConfigPlugin();
        this.userController = userController;
    }
    @EventHandler()
    public void onDamage(EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        User victimUserData = ClanAndUserDataManager.getCachedUser(victim.getUniqueId());
        if (victimUserData == null || ClanAndUserDataManager.getCachedClan(victimUserData.getClanTag()) == null) {
            return;
        }

        Clan victimClan = ClanAndUserDataManager.getCachedClan(victimUserData.getClanTag());

        if (victimClan.isMember(attacker.getUniqueId())) {
            if (!victimClan.isPvpEnable()) {
                event.setCancelled(true);
            }
            return;
        }

        User attackerUserData = ClanAndUserDataManager.getCachedUser(attacker.getUniqueId());
        if (attackerUserData == null || ClanAndUserDataManager.getCachedClan(attackerUserData.getClanTag()) == null) {
            return;
        }

        Clan attackerClan = ClanAndUserDataManager.getCachedClan(attackerUserData.getClanTag());

        if (victimClan.isAlliance(attackerClan.getTag())) {
            if (!Config.pvpAllianceEnabledGlobally) {
                event.setCancelled(true);
            }
        }

    }

}
