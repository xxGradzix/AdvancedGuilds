package me.xxgradzix.advancedclans.listener.guildHideOut;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.UpgradeInfoHolder;
import me.xxgradzix.advancedclans.guildshideoutsystem.ItemManager;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.UpgradePattern;
import me.xxgradzix.advancedclans.controllers.GuildHideOutController;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import net.kyori.adventure.text.Component;
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

        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;

        Block clickedBlock = event.getClickedBlock();

        if(clickedBlock == null) return;

        if(!clickedBlock.getType().equals(Material.LECTERN)) return;

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

        UpgradeInfoHolder upgradeHolder = playerHideOut.getUpgradeHolder(GuildHideout.Upgrade.STATION_HALL);
        upgradeHolder.getTimeTOCompletionSeconds();

        UpgradePattern upgradePattern = guildHideOutController.getUpgradePattern(GuildHideout.Upgrade.STATION_HALL);
        GuiItem hallItem = new GuiItem(ItemManager.getMainHallUpgradeButton(upgradePattern.getPrice(), upgradeHolder.isBought(), upgradeHolder.isFinished(), upgradeHolder.getTimeTOCompletionSeconds()));
        hallItem.setAction(inventoryClickEvent -> {
            if(upgradeHolder.isBought() && upgradeHolder.isFinished()) {
                openMainHallUpgrades(player, playerHideOut);
                return;
            }

            if(upgradeHolder.isBought() && !upgradeHolder.isFinished()) {
                MessageManager.sendMessageFormated(player, MessageManager.UPGRADE_IS_NOT_FINISHED_YET, MessageType.CHAT);
                return;
            }

            if(!upgradeHolder.isBought()) {
                // TODO buy logic
                guildHideOutController.upgradeHideOut(playerHideOut, GuildHideout.Upgrade.STATION_HALL);
                return;
            }

            upgradeGui.update();
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

            UpgradeInfoHolder upgradeHolder = playerHideOut.getUpgradeHolder(upgrade);
            upgradeHolder.getTimeTOCompletionSeconds();

            UpgradePattern upgradePattern = guildHideOutController.getUpgradePattern(upgrade);

            GuiItem upgradeButton = new GuiItem(ItemManager.getUpgradeButton(upgrade, upgradePattern.getPrice(), upgradeHolder.isBought(), upgradeHolder.isFinished(), upgradeHolder.getTimeTOCompletionSeconds()));
            upgradeButton.setAction(inventoryClickEvent -> {
                if (upgradeHolder.isBought() && upgradeHolder.isFinished()) {
                    MessageManager.sendMessageFormated(player, MessageManager.UPGRADE_IS_BOUGHT, MessageType.CHAT);
                    return;
                }

                if (upgradeHolder.isBought() && !upgradeHolder.isFinished()) {
                    MessageManager.sendMessageFormated(player, MessageManager.UPGRADE_IS_NOT_FINISHED_YET, MessageType.CHAT);
                    return;
                }

                if (!upgradeHolder.isBought()) {
                    // TODO buy logic
                    guildHideOutController.upgradeHideOut(playerHideOut, upgrade);
                    openMainHallUpgrades(player, playerHideOut);
                    return;
                }

                upgradeGui.update();
            });
            upgradeGui.setItem(2,getStationHallUpgradeButtonColumn(upgrade),upgradeButton);
        }
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
