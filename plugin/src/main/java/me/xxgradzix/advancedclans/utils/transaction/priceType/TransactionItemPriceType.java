package me.xxgradzix.advancedclans.utils.transaction.priceType;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TransactionItemPriceType implements TransactionPriceType {

    @Getter
    List<ItemStack> itemsToPay;

    public TransactionItemPriceType(List<ItemStack> itemsToPay) {
        this.itemsToPay = itemsToPay;
    }

}
