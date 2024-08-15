package me.xxgradzix.advancedclans.listener;

import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.services.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.manager.CooldownManager;
import me.xxgradzix.advancedclans.controllers.UserController;
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

        User user = userByPlayer.get();
        if(!user.hasClan())
            return;

        Clan clan = ClanAndUserDataManager.getCachedClan(user.getClanTag());
        boolean owner = clan.isOwner(player.getUniqueId());
        if(owner)
            clan.resetInvite();

    }
}
