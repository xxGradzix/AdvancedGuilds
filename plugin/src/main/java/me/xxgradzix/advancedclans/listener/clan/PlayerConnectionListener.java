package me.xxgradzix.advancedclans.listener.clan;

import entities.Clan;
import entities.User;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.entities.clan.ClanImpl;
import me.xxgradzix.advancedclans.data.database.entities.clan.UserImpl;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataService;
import me.xxgradzix.advancedclans.manager.CooldownManager;
import me.xxgradzix.advancedclans.data.database.controllers.clansCore.UserController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public class PlayerConnectionListener implements Listener {

    private final AdvancedGuilds plugin;
    private final CooldownManager cooldownManager;
    
    private final UserController userController;

    public PlayerConnectionListener(AdvancedGuilds plugin, CooldownManager cooldownManager, UserController userController) {
        this.plugin = plugin;
        this.cooldownManager = cooldownManager;
        this.userController = userController;
    }


    @EventHandler
    public void onJoinPlayer(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        new BukkitRunnable() {

            @Override
            public void run() {
                userController.loadUser(player);
            }

        }.runTaskAsynchronously(plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        // delete cooldown [PlayerInteractionEntityEvent]
        cooldownManager.delPlayerFromCooldown(player);

        Optional<User> userByPlayer = userController.findUserByPlayer(player);
        if(userByPlayer.isEmpty())
            return;

        User userImpl = userByPlayer.get();
        if(!userImpl.hasClan())
            return;

        Clan clanImpl = ClanAndUserDataService.getCachedClan(userImpl.getClanTag());
        boolean owner = clanImpl.isOwner(player.getUniqueId());
        if(owner)
            clanImpl.resetInvite();

    }
}
