package me.xxgradzix.advancedclans.listener;

import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.manager.CooldownManager;
import me.xxgradzix.advancedclans.manager.UserManager;
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
    
    private final UserManager userManager;

    public PlayerConnectionListener(AdvancedGuilds plugin, CooldownManager cooldownManager, UserManager userManager) {
        this.plugin = plugin;
        this.cooldownManager = cooldownManager;
        this.userManager = userManager;
    }


    @EventHandler
    public void onJoinPlayer(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        new BukkitRunnable() {

            @Override
            public void run() {
                userManager.loadUser(player);
            }

        }.runTaskAsynchronously(plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        // delete cooldown [PlayerInteractionEntityEvent]
        cooldownManager.delPlayerFromCooldown(player);

        Optional<User> userByPlayer = userManager.findUserByPlayer(player);
        if(userByPlayer.isEmpty())
            return;

        User user = userByPlayer.get();
        if(!user.hasClan())
            return;

        Clan clan = user.getClan();
        boolean owner = clan.isOwner(player.getUniqueId());
        if(owner)
            clan.resetInvite();

    }
}
