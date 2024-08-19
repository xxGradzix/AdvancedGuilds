package me.xxgradzix.advancedclans.listener.guildHideOut;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.event.HologramClickEvent;
import me.xxgradzix.advancedclans.controllers.GuildHideOutController;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HideoutTeleportBlockClick implements Listener {

    private final GuildHideOutController guildHideOutController;

    public HideoutTeleportBlockClick(GuildHideOutController guildHideOutController) {
        this.guildHideOutController = guildHideOutController;
    }

    @EventHandler
    public void onEntityInteractEvent(HologramClickEvent event) {

        Player player = event.getPlayer();
        Hologram hologram = event.getHologram();


        if (hologram.getName().contains(GuildHideOutController.OCCUPIED_HIDEOUT_HOLOGRAM_SUFFIX)){

            guildHideOutController.attemptTeleportByHologram(player, hologram.getName());
        }

    }
}
