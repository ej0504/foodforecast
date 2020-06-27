package io.edwardjoyce.foodforecast.model;

public final class IngredientMacros {

    private final float calories;
    private final float protein;
    private final float fat;
    private final float carbs;

    public IngredientMacros(final float calories,
                      final float protein,
                      float fat,
                      float carbs) {
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
    }

    public float getCalories() {
        return calories;
    }

    public float getProtein() {
        return protein;
    }

    public float getFat() {
        return fat;
    }

    public float getCarbs() {
        return carbs;
    }
}
