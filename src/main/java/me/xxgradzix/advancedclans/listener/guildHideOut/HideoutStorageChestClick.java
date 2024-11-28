package me.xxgradzix.advancedclans.listener.guildHideOut;

import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.storage.HideoutStorage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class HideoutStorageChestClick implements Listener {

    @EventHandler
    public void onStorageChestClick(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();

        if(clickedBlock == null) return;

        if(!clickedBlock.getType().equals(Material.CHEST)) return;

        if(clickedBlock.getLocation().getBlockX() != 0 || clickedBlock.getLocation().getBlockZ() != -44) return;

        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        event.setCancelled(true);

        HideoutStorage.openGuildStoragesGui(event.getPlayer());


    }

}
