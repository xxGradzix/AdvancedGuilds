package me.xxgradzix.advancedclans.listener.hideout;

import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.event.HologramClickEvent;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.services.hideout.GuildHideOutDataService;
import me.xxgradzix.advancedclans.globalGuis.FastForwardGui;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.countdown.Countdown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition.ExpeditionGui.openCurrentExpeditionGui;

public class DHAPIHologramClickEvent implements Listener {

    @EventHandler
    public void onHologramClickEvent(HologramClickEvent event) {

        if(!event.getClick().equals(ClickType.RIGHT)) return;

        String id = event.getHologram().getId();

        Countdown countdown = Countdown.countdowns.get(id);

        if(countdown != null && countdown.secondsLeft() > 0) {

            Player player = event.getPlayer();

            new FastForwardGui(player, countdown, "ʙᴜᴅᴏᴡę", event1 -> {
                player.closeInventory();
            }, true);

            return;
        }

        if(id.endsWith(GuildHideOutDataService.HIDEOUT_BOUGHT_VENTURE)) {
            Bukkit.getScheduler().runTask(AdvancedGuilds.instance, () -> {
                openCurrentExpeditionGui(event.getPlayer());
            });
        }
    }



}
