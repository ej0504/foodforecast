package io.edwardjoyce.foodforecast.model;

public final class Ingredient {

    private final String name;
    private final float calories;
    private final float protein;
    private final float fat;
    private final float carbs;

    public Ingredient(final String name,
                      final float calories,
                      final float protein,
                      float fat,
                      float carbs) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
    }

    public String getName() {
        return name;
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

    @Override
    public String toString() {
        return "Ingredient{" +
                "name='" + name + '\'' +
                ", calories=" + calories +
                ", protein=" + protein +
                ", fat=" + fat +
                ", carbs=" + carbs +
                '}';
    }
}
