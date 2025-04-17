package me.xxgradzix.advancedclans.itemShop.listeners;

import me.xxgradzix.advancedclans.itemShop.data.ShopService;
import me.xxgradzix.advancedclans.itemShop.data.database.enums.ShopType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerKillAddPoint implements Listener {

    private final ShopService shopService;

    public OnPlayerKillAddPoint(ShopService shopService) {
        this.shopService = shopService;
    }

    @EventHandler
    public void onPlayerKill(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if(killer == null) return;
        shopService.addMoneyToPlayer(killer, ShopType.KILLS, 1);
    }

}
