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

        Bukkit.broadcastMessage("test 1");
        if(!block.getType().equals(Material.TARGET)) return;
        Bukkit.broadcastMessage("test 2");

        GuildHideout hideoutByLocation = guildHideOutController.getHideoutByLocation(block.getLocation());

        if(hideoutByLocation == null) return;
        Bukkit.broadcastMessage("test 3");

        try {
            guildHideOutController.occupyHideout(player, hideoutByLocation);
            Bukkit.broadcastMessage("test 4");
        } catch (HideOutDoesNotExistException e) {
            throw new RuntimeException(e);
        }


    }

}
