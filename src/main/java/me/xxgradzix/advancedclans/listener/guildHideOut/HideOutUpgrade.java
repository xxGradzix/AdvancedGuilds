package me.xxgradzix.advancedclans.listener.guildHideOut;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.fields.UpgradeInfoHolder;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.services.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.data.database.services.GuildHideOutDataManager;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutUpgradeAlreadyBoughtException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.UpgradeWasNotBoughtException;
import me.xxgradzix.advancedclans.guildshideoutsystem.ItemManager;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.UpgradePattern;
import me.xxgradzix.advancedclans.controllers.GuildHideOutController;
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

import java.io.InvalidObjectException;
import java.util.List;

public class HideOutUpgrade implements Listener {

    private final GuildHideOutController guildHideOutController;

    public HideOutUpgrade(GuildHideOutController guildHideOutController) {
        this.guildHideOutController = guildHideOutController;
    }

    @EventHandler
    public void onLecternCLick(PlayerInteractEvent event) {


        if(event.getAction().equals(Action.RIGHT_CLICK_AIR)) return;

        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Block clickedBlock = event.getClickedBlock();

        if(clickedBlock == null) return;

        if(!clickedBlock.getType().equals(Material.LECTERN)) return;

        Bukkit.broadcastMessage("4un test");

        Player player = event.getPlayer();

        GuildHideout playerHideOut;
        try {
            playerHideOut = guildHideOutController.getPlayerHideOut(player);
        } catch (InvalidObjectException e) {
            throw new RuntimeException(e);
        }

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

        GuildHideout.Upgrade stationHallUpgrade = GuildHideout.Upgrade.STATION_HALL;

        long timeToCompletionSeconds;

        try {
            timeToCompletionSeconds = playerHideOut.getTimeToCompletionSeconds(stationHallUpgrade);
        } catch (UpgradeWasNotBoughtException e) {
            timeToCompletionSeconds = -1;
        }

        GuiItem hallItem = new GuiItem(ItemManager.getMainHallUpgradeButton(upgradePattern.getPrice(), playerHideOut.hasBoughtUpgrade(stationHallUpgrade), playerHideOut.hasBoughtUpgrade(stationHallUpgrade), timeToCompletionSeconds));

        hallItem.setAction(inventoryClickEvent -> {

            if(playerHideOut.hasBoughtUpgrade(stationHallUpgrade) && playerHideOut.hasFinishedUpgrade(stationHallUpgrade)) {

                openMainHallUpgrades(player, playerHideOut);
                return;

            } else if(playerHideOut.hasBoughtUpgrade(stationHallUpgrade) && !playerHideOut.hasFinishedUpgrade(stationHallUpgrade)) {

                MessageManager.sendMessageFormated(player, MessageManager.UPGRADE_IS_NOT_FINISHED_YET, MessageType.CHAT);

            } else if(!playerHideOut.hasBoughtUpgrade(stationHallUpgrade)) {

                // TODO buy logic

                guildHideOutController.upgradeHideOut(playerHideOut, stationHallUpgrade);

            }

            openUpgradeGui(player, playerHideOut);
        });

        upgradeGui.setItem(2, 5, hallItem);

        upgradeGui.open(player);
    }


    public void openMainHallUpgrades(Player player, GuildHideout playerHideOut) {
        Gui upgradeGui = Gui.gui()
                .disableAllInteractions()
                .rows(3)
                .title(Component.text("Ulepszenia stacji rzemieślniczych")) //TODO Gui name
                .create();

        for(GuildHideout.Upgrade upgrade : List.of(GuildHideout.Upgrade.BLACKSMITH, GuildHideout.Upgrade.SORCERER)) {

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
            default:
                throw new IllegalArgumentException("Unknown upgrade: " + upgrade.name());
        }
    }

}
