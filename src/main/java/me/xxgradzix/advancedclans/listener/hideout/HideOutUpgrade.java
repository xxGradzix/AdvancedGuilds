package me.xxgradzix.advancedclans.listener.hideout;

import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.GuildPanel.GideoutGui;
import me.xxgradzix.advancedclans.data.database.controllers.hideouts.GuildHideOutController;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class HideOutUpgrade implements Listener {

    private final GuildHideOutController guildHideOutController;

    public HideOutUpgrade(GuildHideOutController guildHideOutController) {
        this.guildHideOutController = guildHideOutController;
    }

    @EventHandler
    public void onLecternCLick(PlayerInteractEvent event) {

        Block clickedBlock = event.getClickedBlock();

        if(clickedBlock == null) return;
        if(!clickedBlock.getType().equals(Material.LECTERN)) return;
        if(event.getHand() == null) return;
        if(event.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
        if(clickedBlock.getLocation().getBlockX() != -9 || clickedBlock.getLocation().getBlockZ() != -25) return;

        event.setCancelled(true);

        Player player = event.getPlayer();

        GuildHideout playerHideOut = guildHideOutController.getPlayerHideOut(player);

        if(playerHideOut == null) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
            return;
        }

        new GideoutGui(player);

    }


}
