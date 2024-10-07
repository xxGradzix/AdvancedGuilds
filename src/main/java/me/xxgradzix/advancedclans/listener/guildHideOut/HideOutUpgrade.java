package me.xxgradzix.advancedclans.listener.guildHideOut;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import eu.decentsoftware.holograms.api.DHAPI;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import me.xxgradzix.advancedclans.exceptions.hideOuts.UpgradeWasNotBoughtException;
import me.xxgradzix.advancedclans.guildshideoutsystem.ItemManager;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.UpgradePattern;
import me.xxgradzix.advancedclans.data.database.controllers.hideouts.GuildHideOutController;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

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

        openUpgradeGui(player, playerHideOut);
    }

    private void openUpgradeGui(Player player, GuildHideout playerHideOut) {
        Gui upgradeGui = Gui.gui()
                .disableAllInteractions()
                .rows(3)
                .title(Component.text("Ulepszenia kryjowki")) //TODO Gui name
                .create();

        UpgradePattern upgradePattern = guildHideOutController.getUpgradePattern(GuildHideout.Upgrade.STATION_HALL);

        /** MAIN HALL UPGRADES **/

        GuildHideout.Upgrade stationHallUpgrade = GuildHideout.Upgrade.STATION_HALL;
        long timeToStationHallUpgradeCompletionSeconds;
        try {
            timeToStationHallUpgradeCompletionSeconds = playerHideOut.getTimeToCompletionSeconds(stationHallUpgrade);
        } catch (UpgradeWasNotBoughtException e) {
            timeToStationHallUpgradeCompletionSeconds = -1;
        }

        GuiItem hallItem = new GuiItem(ItemManager.getMainHallUpgradeButton(upgradePattern.getPrice(), playerHideOut.hasBoughtUpgrade(stationHallUpgrade), playerHideOut.hasBoughtUpgrade(stationHallUpgrade), timeToStationHallUpgradeCompletionSeconds));
        hallItem.setAction(inventoryClickEvent -> {
            if(playerHideOut.hasBoughtUpgrade(stationHallUpgrade) && playerHideOut.hasFinishedUpgrade(stationHallUpgrade)) {
                openMainHallUpgrades(player, playerHideOut);
                return;
            } else if(playerHideOut.hasBoughtUpgrade(stationHallUpgrade) && !playerHideOut.hasFinishedUpgrade(stationHallUpgrade)) {
                MessageManager.sendMessageFormated(player, MessageManager.UPGRADE_IS_NOT_FINISHED_YET, MessageType.CHAT);
            } else if(!playerHideOut.hasBoughtUpgrade(stationHallUpgrade)) {
                // TODO buy logic
                guildHideOutController.upgradeHideOut(playerHideOut, stationHallUpgrade);
                DHAPI.removeHologram(playerHideOut.getWorldName() + GuildHideOutController.STATION_HALL);
            }
            openUpgradeGui(player, playerHideOut);
        });

        /** ARENA UPGRADES **/

        GuildHideout.Upgrade arenaUpgrade = GuildHideout.Upgrade.ARENA;

        long timeToArenaUpgradeCompletionSeconds;

        try {
            timeToArenaUpgradeCompletionSeconds = playerHideOut.getTimeToCompletionSeconds(arenaUpgrade);
        } catch (UpgradeWasNotBoughtException e) {
            timeToArenaUpgradeCompletionSeconds = -1;
        }
        GuiItem arenaItem = new GuiItem(ItemManager.getArenaUpgradeButton(upgradePattern.getPrice(), playerHideOut.hasBoughtUpgrade(arenaUpgrade), playerHideOut.hasBoughtUpgrade(arenaUpgrade), timeToArenaUpgradeCompletionSeconds));
        arenaItem.setAction(inventoryClickEvent -> {
            //TODO ARENA MANAGEMENT LOGIC
            if(playerHideOut.hasBoughtUpgrade(arenaUpgrade) && !playerHideOut.hasFinishedUpgrade(arenaUpgrade)) {
                MessageManager.sendMessageFormated(player, MessageManager.UPGRADE_IS_NOT_FINISHED_YET, MessageType.CHAT);
            } else if(!playerHideOut.hasBoughtUpgrade(arenaUpgrade)) {
                // TODO buy logic
                guildHideOutController.upgradeHideOut(playerHideOut, arenaUpgrade);
                DHAPI.removeHologram(playerHideOut.getWorldName() + GuildHideOutController.ARENA_UPGRADE);
            }
            openUpgradeGui(player, playerHideOut);
        });

        upgradeGui.setItem(2, 4, hallItem);
        upgradeGui.setItem(2, 6, arenaItem);

        upgradeGui.open(player);
    }


    public void openMainHallUpgrades(Player player, GuildHideout playerHideOut) {
        Gui upgradeGui = Gui.gui()
                .disableAllInteractions()
                .rows(3)
                .title(Component.text("Ulepszenia stacji rzemieślniczych")) //TODO Gui name
                .create();

        for(GuildHideout.Upgrade upgrade : List.of(GuildHideout.Upgrade.BLACKSMITH, GuildHideout.Upgrade.VENTURE, GuildHideout.Upgrade.SORCERER)) {

            UpgradePattern upgradePattern = guildHideOutController.getUpgradePattern(upgrade);

            if(upgradePattern == null) {
                Bukkit.getLogger().severe("Error: Upgrade pattern not found for upgrade: " + upgrade.name());
                continue;
            }

            long timeToCompletionSeconds;
            try {
                timeToCompletionSeconds = playerHideOut.getTimeToCompletionSeconds(upgrade);
            } catch (UpgradeWasNotBoughtException e) {
                timeToCompletionSeconds = -1;
            }

            GuiItem upgradeButton = new GuiItem(ItemManager.getUpgradeButton(upgrade, upgradePattern.getPrice(), playerHideOut.hasBoughtUpgrade(upgrade), playerHideOut.hasFinishedUpgrade(upgrade), timeToCompletionSeconds));

            upgradeButton.setAction(inventoryClickEvent -> {

                if (playerHideOut.hasBoughtUpgrade(upgrade) && playerHideOut.hasFinishedUpgrade(upgrade)) {
                    MessageManager.sendMessageFormated(player, MessageManager.UPGRADE_IS_BOUGHT, MessageType.CHAT);
                } else if (playerHideOut.hasBoughtUpgrade(upgrade) && !playerHideOut.hasFinishedUpgrade(upgrade)) {
                    MessageManager.sendMessageFormated(player, MessageManager.UPGRADE_IS_NOT_FINISHED_YET, MessageType.CHAT);
                } else if (!playerHideOut.hasBoughtUpgrade(upgrade)) {
                    // TODO buy logic
                    guildHideOutController.upgradeHideOut(playerHideOut, upgrade);
                }

                openMainHallUpgrades(player, playerHideOut);
            });
            upgradeGui.setItem(2,getStationHallUpgradeButtonColumn(upgrade),upgradeButton);
        }
        upgradeGui.open(player);
    }

    private int getStationHallUpgradeButtonColumn(GuildHideout.Upgrade upgrade) {
        switch (upgrade) {
            case BLACKSMITH:
                return 4;
            case SORCERER:
                return 2;
            case VENTURE:
                return 6;
            default:
                throw new IllegalArgumentException("Unknown upgrade: " + upgrade.name());
        }
    }

}
