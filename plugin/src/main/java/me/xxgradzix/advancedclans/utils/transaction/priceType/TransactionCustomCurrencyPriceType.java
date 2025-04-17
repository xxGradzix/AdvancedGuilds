package me.xxgradzix.advancedclans.utils.transaction.priceType;

import me.xxgradzix.advancedclans.utils.transaction.TransactionUtils;

public abstract class TransactionCustomCurrencyPriceType implements TransactionPriceType {

    public abstract TransactionUtils.TransactionResponse hasHasRequiredCurrency();
    public abstract void chargePlayer();

}
