package io.edwardjoyce.foodforecast.summary.model;

import io.edwardjoyce.foodforecast.model.QuantityUnit;

public final class ShoppingListDetail {

    private final  String name;
    private final double amount;
    private final QuantityUnit unit;

    public ShoppingListDetail(final String name,
                              final double amount,
                              final QuantityUnit unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public QuantityUnit getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "ShoppingListDetail{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                ", unit=" + unit +
                '}';
    }
}
