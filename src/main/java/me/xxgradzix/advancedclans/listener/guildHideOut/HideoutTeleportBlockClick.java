package me.xxgradzix.advancedclans.listener.guildHideOut;

import me.xxgradzix.advancedclans.controllers.GuildHideOutController;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class HideoutTeleportBlockClick implements Listener {

    private final GuildHideOutController guildHideOutController;

    public HideoutTeleportBlockClick(GuildHideOutController guildHideOutController) {
        this.guildHideOutController = guildHideOutController;
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {

        Block clickedBlock = event.getClickedBlock();

        guildHideOutController.attemptTeleportByClickedBlock(event.getPlayer(), clickedBlock);
    }

}
