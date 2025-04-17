package me.xxgradzix.advancedclans.itemShop.listeners;

import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.itemShop.data.ShopService;
import me.xxgradzix.advancedclans.itemShop.data.database.enums.ShopType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class OnPlayerJoinCreateBalanceEntity implements Listener {

    private final ShopService shopService;
    private final BukkitScheduler scheduler = Bukkit.getScheduler();
    private final AdvancedGuilds plugin;

    public OnPlayerJoinCreateBalanceEntity(ShopService shopService, AdvancedGuilds plugin) {
        this.shopService = shopService;
        this.plugin = plugin;
        startAddingPoints();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        shopService.createPlayerBalanceEntityIfNotExists(player);

    }
    private void startAddingPoints() {

            scheduler.runTaskTimer(plugin, () -> {

                for(Player player : Bukkit.getOnlinePlayers()) {
                    shopService.addMoneyToPlayer(player, ShopType.TIME, 1);
                }

            }, 0, 20L * 60L * 15); // 15 minutes duration

    }
}
