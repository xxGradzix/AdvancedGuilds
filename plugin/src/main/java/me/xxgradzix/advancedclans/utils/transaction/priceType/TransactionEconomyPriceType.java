package me.xxgradzix.advancedclans.utils.transaction.priceType;

import lombok.Getter;

public class TransactionEconomyPriceType implements TransactionPriceType {

    @Getter
    private double amount;

    public TransactionEconomyPriceType(double amount) {
        this.amount = amount;
    }
}
