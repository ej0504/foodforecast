package io.edwardjoyce.foodforecast.model;

public final class IngredientMacros {

    private final double calories;
    private final double protein;
    private final double fat;
    private final double carbs;

    public IngredientMacros(final double calories,
                      final double protein,
                      final double fat,
                      final double carbs) {
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
    }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getCarbs() {
        return carbs;
    }
}
