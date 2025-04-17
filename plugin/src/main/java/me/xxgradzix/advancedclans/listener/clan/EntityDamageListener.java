package me.xxgradzix.advancedclans.listener.clan;

import entities.Clan;
import entities.User;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.config.Config;
import me.xxgradzix.advancedclans.data.database.entities.clan.ClanImpl;
import me.xxgradzix.advancedclans.data.database.entities.clan.UserImpl;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataService;
import me.xxgradzix.advancedclans.data.database.controllers.clansCore.UserController;
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

        User victimUserImplData = ClanAndUserDataService.getCachedUser(victim.getUniqueId());
        if (victimUserImplData == null || ClanAndUserDataService.getCachedClan(victimUserImplData.getClanTag()) == null) {
            return;
        }

        Clan victimClanImpl = ClanAndUserDataService.getCachedClan(victimUserImplData.getClanTag());

        if (victimClanImpl.isMember(attacker.getUniqueId())) {
            if (!victimClanImpl.isPvpEnable()) {
                event.setCancelled(true);
            }
            return;
        }

        User attackerUserImplData = ClanAndUserDataService.getCachedUser(attacker.getUniqueId());
        if (attackerUserImplData == null || ClanAndUserDataService.getCachedClan(attackerUserImplData.getClanTag()) == null) {
            return;
        }

        Clan attackerClanImpl = ClanAndUserDataService.getCachedClan(attackerUserImplData.getClanTag());

        if (victimClanImpl.isAlliance(attackerClanImpl.getTag())) {
            if (!Config.pvpAllianceEnabledGlobally) {
                event.setCancelled(true);
            }
        }

    }

}
