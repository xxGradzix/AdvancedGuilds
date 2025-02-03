package me.xxgradzix.advancedclans.listener.clan;

import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.config.Config;
import me.xxgradzix.advancedclans.data.database.entities.clan.Clan;
import me.xxgradzix.advancedclans.data.database.entities.clan.User;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataService;
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

        User victimUserData = ClanAndUserDataService.getCachedUser(victim.getUniqueId());
        if (victimUserData == null || ClanAndUserDataService.getCachedClan(victimUserData.getClanTag()) == null) {
            return;
        }

        Clan victimClan = ClanAndUserDataService.getCachedClan(victimUserData.getClanTag());

        if (victimClan.isMember(attacker.getUniqueId())) {
            if (!victimClan.isPvpEnable()) {
                event.setCancelled(true);
            }
            return;
        }

        User attackerUserData = ClanAndUserDataService.getCachedUser(attacker.getUniqueId());
        if (attackerUserData == null || ClanAndUserDataService.getCachedClan(attackerUserData.getClanTag()) == null) {
            return;
        }

        Clan attackerClan = ClanAndUserDataService.getCachedClan(attackerUserData.getClanTag());

        if (victimClan.isAlliance(attackerClan.getTag())) {
            if (!Config.pvpAllianceEnabledGlobally) {
                event.setCancelled(true);
            }
        }

    }

}
