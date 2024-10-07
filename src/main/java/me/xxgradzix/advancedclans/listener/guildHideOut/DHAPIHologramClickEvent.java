package me.xxgradzix.advancedclans.listener.guildHideOut;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.event.HologramClickEvent;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.guildshideoutsystem.ItemManager;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.Countdown;
import me.xxgradzix.advancedclans.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class DHAPIHologramClickEvent implements Listener {

    @EventHandler
    public void onHologramClickEvent(HologramClickEvent event) {

        if(!event.getClick().equals(ClickType.RIGHT)) return;

        String id = event.getHologram().getId();

        Countdown countdown = Countdown.countdowns.get(id);
        if(countdown == null) return;

        fastForward(countdown, event.getPlayer());

    }

    // TODO Extract this method

    private void fastForward(Countdown countdown, Player player) {

        ItemStack targetItem = ItemManager.getPremiumGuildCoin();

        Gui gui = new Gui(3, "Przyspiesz");
        gui.disableAllInteractions();

        GuiItem expeditionItem = new GuiItem(ItemManager.getCurrentCooldownItem(countdown.secondsLeft()));

        expeditionItem.setAction(event -> {

            int requiredAmount = countdown.secondsLeft() / (60 * 30);

            int currentAmount = ItemUtil.calcItemAmount(player, targetItem);

            if(currentAmount < requiredAmount) {
                player.sendMessage("You need " + requiredAmount + " " + targetItem.getItemMeta().getDisplayName() + " to fast forward the countdown");
                return;
            }

            ItemUtil.removeItems(player, targetItem, requiredAmount);
            countdown.fastForward();

            gui.close(player);
        });

        gui.setItem(13, expeditionItem);

        Bukkit.getScheduler().runTask(AdvancedGuilds.instance, () -> {
            gui.open(player);

        });
    }

}