package me.xxgradzix.advancedclans.listener.guildHideOut;

import me.xxgradzix.advancedclans.controllers.GuildHideOutController;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class HideoutTeleportBlockClick implements Listener {

    private final GuildHideOutController guildHideOutController;

    public HideoutTeleportBlockClick(GuildHideOutController guildHideOutController) {
        this.guildHideOutController = guildHideOutController;
    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) {

        if(event.getHand() == null) return;

        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;

        Block clickedBlock = event.getClickedBlock();

        guildHideOutController.attemptTeleportByClickedBlock(event.getPlayer(), clickedBlock);
    }

}
