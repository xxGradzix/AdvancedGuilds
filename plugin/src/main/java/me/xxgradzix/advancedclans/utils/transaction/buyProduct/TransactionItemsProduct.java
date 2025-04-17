package me.xxgradzix.advancedclans.utils.transaction.buyProduct;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TransactionItemsProduct implements TransactionProduct {

    @Getter
    List<ItemStack> itemsToBuy;

    public TransactionItemsProduct(List<ItemStack> itemsToBuy) {
        this.itemsToBuy = itemsToBuy;
    }

    public TransactionItemsProduct(ItemStack... itemsToBuy) {
        this.itemsToBuy = List.of(itemsToBuy);
    }


}
