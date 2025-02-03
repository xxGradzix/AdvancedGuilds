package me.xxgradzix.advancedclans.listener.hideout;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.event.HologramClickEvent;
import me.xxgradzix.advancedclans.data.database.controllers.hideouts.GuildHideOutController;
import me.xxgradzix.advancedclans.data.database.services.hideout.GuildHideOutDataService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HideoutTeleportBlockClick implements Listener {

    private final GuildHideOutController guildHideOutController;

    public HideoutTeleportBlockClick(GuildHideOutController guildHideOutController) {
        this.guildHideOutController = guildHideOutController;
    }

    @EventHandler
    public void onHologramClickEvent(HologramClickEvent event) {

        Player player = event.getPlayer();
        Hologram hologram = event.getHologram();

        if (hologram.getName().contains(GuildHideOutController.OCCUPIED_HIDEOUT_HOLOGRAM_SUFFIX)){

            String hideoutName = hologram.getName().replace(GuildHideOutController.OCCUPIED_HIDEOUT_HOLOGRAM_SUFFIX, "");

            GuildHideOutController.attemptTeleportByHologram(player, hideoutName);

        } else if(hologram.getName().contains(GuildHideOutDataService.RETURN_TO_OUTPOST)){

            GuildHideOutController.attemptTeleportToOutpost(player, hologram.getName().replace(GuildHideOutDataService.RETURN_TO_OUTPOST, ""));
        }
    }


}
