package io.edwardjoyce.foodforecast.model;

public final class QuantifiedIngredient {

    private final Ingredient ingredient;
    private final float amount;
    private final QuantityUnit quantityUnit;

    public QuantifiedIngredient(final Ingredient ingredient,
                                final float amount,
                                final QuantityUnit quantityUnit) {
        this.ingredient = ingredient;
        this.amount = amount;
        this.quantityUnit = quantityUnit;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public float getAmount() {
        return amount;
    }

    public QuantityUnit getQuantityUnit() {
        return quantityUnit;
    }
}
