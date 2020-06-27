package io.edwardjoyce.foodforecast.model;

import io.edwardjoyce.foodforecast.NoMacrosForUnitException;

public final class QuantifiedIngredient {

    private final Ingredient ingredient;
    private final double amount;
    private final QuantityUnit quantityUnit;

    public QuantifiedIngredient(final Ingredient ingredient,
                                final double amount,
                                final QuantityUnit quantityUnit) {
        this.ingredient = ingredient;
        this.amount = amount;
        this.quantityUnit = quantityUnit;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public double getAmount() {
        return amount;
    }

    public QuantityUnit getQuantityUnit() {
        return quantityUnit;
    }

    public IngredientMacros getMacros() {
        return ingredient
                .getMacrosForUnit(quantityUnit)
                .map(this::multiplyMacros)
                .orElseThrow(() -> new NoMacrosForUnitException(
                        String.format("Macros not available for the unit %s for %s",
                                quantityUnit, ingredient.getName())));
    }

    private IngredientMacros multiplyMacros(final IngredientMacros macros) {
        return new IngredientMacros(
                macros.getCalories() * amount,
                macros.getProtein() * amount,
                macros.getFat() * amount,
                macros.getCarbs() * amount
        );
    }
}
