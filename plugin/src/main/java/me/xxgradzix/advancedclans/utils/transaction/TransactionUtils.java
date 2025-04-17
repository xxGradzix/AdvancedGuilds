package me.xxgradzix.advancedclans.utils.transaction;

import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import me.xxgradzix.advancedclans.utils.transaction.buyProduct.TransactionActionProduct;
import me.xxgradzix.advancedclans.utils.transaction.buyProduct.TransactionItemsProduct;
import me.xxgradzix.advancedclans.utils.transaction.buyProduct.TransactionProduct;
import me.xxgradzix.advancedclans.utils.transaction.priceType.TransactionCustomCurrencyPriceType;
import me.xxgradzix.advancedclans.utils.transaction.priceType.TransactionEconomyPriceType;
import me.xxgradzix.advancedclans.utils.transaction.priceType.TransactionItemPriceType;
import me.xxgradzix.advancedclans.utils.transaction.priceType.TransactionPriceType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TransactionUtils {

    public enum TransactionResponse {

        SUCCESS(ColorFixer.addColors("&aᴛʀᴀɴᴢᴀᴋᴄᴊᴀ ᴢᴀᴋᴏɴᴄᴢᴏɴᴀ ᴘᴏᴍʏꜱʟɴɪᴇ")),

        NOT_ENOUGH_MONEY(ColorFixer.addColors("&cɴɪᴇ ᴍᴀꜱᴢ ᴡʏꜱᴛᴀʀᴄᴢᴀᴊąᴄᴇᴊ ɪʟᴏśᴄɪ ᴘɪᴇɴɪęᴅᴢʏ")),
        NOT_ENOUGH_KILLS(ColorFixer.addColors("&cɴɪᴇ ᴍᴀꜱᴢ ᴡʏꜱᴛᴀʀᴄᴢᴀᴊąᴄᴇᴊ ɪʟᴏśᴄɪ ᴍᴏɴᴇᴛ ᴢᴀʙóᴊꜱᴛᴡ")),
        NOT_ENOUGH_TIME(ColorFixer.addColors("&cɴɪᴇ ᴍᴀꜱᴢ ᴡʏꜱᴛᴀʀᴄᴢᴀᴊąᴄᴇᴊ ɪʟᴏśᴄɪ ᴍᴏɴᴇᴛ ᴄᴢᴀꜱᴜ")),
        NOT_ENOUGH_ITEMS(ColorFixer.addColors("&cɴɪᴇ ᴍᴀꜱᴢ ᴡʏꜱᴛᴀʀᴄᴢᴀᴊąᴄᴇᴊ ɪʟᴏśᴄɪ ᴡʏᴍᴀɢᴀɴʏᴄʜ ᴘʀᴢᴇᴅᴍɪᴏᴛóᴡ")),
        NOT_ENOUGH_SPACE_IN_INVENTORY(ColorFixer.addColors("&cɴɪᴇ ᴍᴀꜱᴢ ᴡʏꜱᴛᴀʀᴄᴢᴀᴊąᴄᴏ ᴍɪᴇᴊꜱᴄᴀ ᴡ ᴇᴋᴡɪᴘᴜɴᴋᴜ"));

        private final String transactionMessage;

        TransactionResponse(String transactionMessage) {
            this.transactionMessage = transactionMessage;
        }

        public String getTransactionMessage() {
            return transactionMessage;
        }
    }


    public static TransactionResponse purchase(Player player, List<TransactionProduct> transactionProducts, List<TransactionPriceType> transactionPriceTypes) {

        List<ItemStack> itemsToCollect = new ArrayList<>();

        /** INV SPACE CHECK **/

        for (TransactionProduct transactionProduct : transactionProducts) {
            if(transactionProduct instanceof TransactionItemsProduct product) itemsToCollect.addAll(product.getItemsToBuy());
        }

        boolean hasSpaceInInventory = true;

        int size = Arrays.stream(player.getInventory().getStorageContents()).filter((item) -> item == null || item.getType().isAir()).toList().size();

        if(size < itemsToCollect.size()) {
            hasSpaceInInventory = false;
        }

        if(!hasSpaceInInventory) {
            return TransactionResponse.NOT_ENOUGH_SPACE_IN_INVENTORY;
        }


        /** PAYMENT CHECK **/

        if(player.getGameMode() != GameMode.CREATIVE) {

            for (TransactionPriceType transactionPriceType : transactionPriceTypes) {

                if (transactionPriceType instanceof TransactionItemPriceType transactionItemPriceType) {

                    for (ItemStack item : transactionItemPriceType.getItemsToPay()) {
                        if (item != null && !player.getInventory().containsAtLeast(item, 1)) {
                            return TransactionResponse.NOT_ENOUGH_ITEMS;
                        }
                    }
                }

                if(transactionPriceType instanceof TransactionCustomCurrencyPriceType transactionCustomCurrencyPriceType) {
                    if (!TransactionResponse.SUCCESS.equals(transactionCustomCurrencyPriceType.hasHasRequiredCurrency())) {
                        return transactionCustomCurrencyPriceType.hasHasRequiredCurrency();
                    }
                }

                if (transactionPriceType instanceof TransactionEconomyPriceType transactionEconomyPriceType) {
                    if(AdvancedGuilds.getEconomy().getBalance(player) < transactionEconomyPriceType.getAmount()) {
                        return TransactionResponse.NOT_ENOUGH_MONEY;
                    }
                }
            }
        }

        /** PAYMENT **/
        for (TransactionPriceType transactionPriceType : transactionPriceTypes) {

            if (transactionPriceType instanceof TransactionItemPriceType transactionItemPriceType) {

                for (ItemStack item : transactionItemPriceType.getItemsToPay()) {
                    if (item != null) player.getInventory().removeItem(item);
                }
            }

            if (transactionPriceType instanceof TransactionCustomCurrencyPriceType transactionCustomCurrencyPriceType) {
                transactionCustomCurrencyPriceType.chargePlayer();
            }

            if (transactionPriceType instanceof TransactionEconomyPriceType transactionEconomyPriceType) {
                AdvancedGuilds.getEconomy().withdrawPlayer(player, transactionEconomyPriceType.getAmount());
            }
        }

        /** COLLECT ITEMS **/
        for (TransactionProduct transactionProduct : transactionProducts) {

            if(transactionProduct instanceof TransactionItemsProduct buyItemsProduct) {

                for(ItemStack item : buyItemsProduct.getItemsToBuy()) {
                        Bukkit.broadcastMessage("Item: " + item.getType().name() + " x" + item.getAmount());
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(item);
                    } else {
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                    }
                }
            }
            if(transactionProduct instanceof TransactionActionProduct buyActionProduct) {
                buyActionProduct.execute();
            }

        }

        return TransactionResponse.SUCCESS;
    }

}
