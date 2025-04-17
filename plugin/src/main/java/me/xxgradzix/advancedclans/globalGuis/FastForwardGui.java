package me.xxgradzix.advancedclans.globalGuis;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.utills.HideoutTimeEvent;
import me.xxgradzix.advancedclans.guildshideoutsystem.HideoutGeneralItemManager;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import me.xxgradzix.advancedclans.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FastForwardGui {

    private final Player player;

    private final boolean addDefaultFastForwardAction;

    private final ClickAction clickAction;

    public FastForwardGui(Player player, HideoutTimeEvent hideoutTimeEvent, String whatToFastForwardFormated, ClickAction clickAction, boolean addDefaultFastForwardAction) {
        this.addDefaultFastForwardAction = addDefaultFastForwardAction;

        this.player = player;
        this.clickAction = clickAction;

        fastForward(hideoutTimeEvent, this.player, whatToFastForwardFormated);
    }

    private void fastForward(HideoutTimeEvent hideoutTimeEvent, Player player, String whatToFastForwardFormated) {

        int secondsLeft = hideoutTimeEvent.secondsLeft();

        Gui gui = new Gui(3, "§f七七七七七七七七≨");

        gui.disableAllInteractions();

        for(int i = 0; i < 3; i++) {

            GuiItem expeditionItem = new GuiItem(HideoutGeneralItemManager.getRequiredPremiumCoins(secondsLeft, i == 1));

            expeditionItem.setAction(event -> {

                clickAction.onClick(event.getAction());


                if(addDefaultFastForwardAction) {

                    ItemStack targetItem = HideoutGeneralItemManager.getPremiumGuildCoin();

                    int requiredAmount = secondsLeft / (60 * 30);
                    int currentAmount = ItemUtil.calcItemAmount(player, targetItem);

                    // TODO ALARM graczy (100/200 m)

                    if(currentAmount < requiredAmount) {
                        MessageManager.sendMessageFormated(player, "&7ᴘᴏᴛʀᴢᴇʙᴜᴊᴇꜱᴢ &a" + requiredAmount + " " + targetItem.getItemMeta().getDisplayName() + " &7ᴀʙʏ ᴘʀᴢʏꜱᴘɪᴇꜱᴢʏć " + whatToFastForwardFormated, MessageType.CHAT);
                        return;
                    }

                    ItemUtil.removeItems(player, targetItem, requiredAmount);
                    gui.close(player);

                    hideoutTimeEvent.fastForward();
                }
            });

            gui.setItem(12 + i, expeditionItem);

        }

        Bukkit.getScheduler().runTask(AdvancedGuilds.instance, () -> {

            gui.open(player);

        });
    }

}
