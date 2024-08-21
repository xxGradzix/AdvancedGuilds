package me.xxgradzix.advancedclans.listener.guildHideOut;

import me.xxgradzix.advancedclans.controllers.GuildHideOutController;
import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutDoesNotExistException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class HideoutOccupyBlockPlace implements Listener {

    private final GuildHideOutController guildHideOutController;

    public HideoutOccupyBlockPlace(GuildHideOutController guildHideOutController) {
        this.guildHideOutController = guildHideOutController;
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if(!block.getType().equals(Material.TARGET)) return;

        GuildHideout hideoutByLocation = guildHideOutController.getHideoutByLocation(block.getLocation());

        if(hideoutByLocation == null) return;

        event.setCancelled(true);

        try {
            guildHideOutController.occupyHideout(player, hideoutByLocation);
        } catch (HideOutDoesNotExistException e) {
            throw new RuntimeException(e);
        }


    }

}
