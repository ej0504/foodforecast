package io.edwardjoyce.foodforecast.summary.model;

import io.edwardjoyce.foodforecast.model.IngredientMacros;
import io.edwardjoyce.foodforecast.model.QuantifiedIngredient;

import java.time.LocalDate;

public final class DaySummary {

    private final LocalDate localDate;
    private final float totalCalories;
    private final float totalProtein;
    private final float totalFat;
    private final float totalCarbs;

    public DaySummary(final LocalDate localDate,
                      final float totalCalories,
                      final float totalProtein,
                      final float totalFat,
                      final float totalCarbs) {
        this.localDate = localDate;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
        this.totalCarbs = totalCarbs;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public float getTotalCalories() {
        return totalCalories;
    }

    public float getTotalProtein() {
        return totalProtein;
    }

    public float getTotalFat() {
        return totalFat;
    }

    public float getTotalCarbs() {
        return totalCarbs;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LocalDate localDate;
        private float totalCalories;
        private float totalProtein;
        private float totalFat;
        private float totalCarbs;

        private Builder() {
        }

        public Builder setLocalDate(LocalDate localDate) {
            this.localDate = localDate;
            return this;
        }

        public Builder setTotalCalories(float totalCalories) {
            this.totalCalories = totalCalories;
            return this;
        }

        public Builder setTotalProtein(float totalProtein) {
            this.totalProtein = totalProtein;
            return this;
        }

        public Builder setTotalFat(float totalFat) {
            this.totalFat = totalFat;
            return this;
        }

        public Builder setTotalCarbs(float totalCarbs) {
            this.totalCarbs = totalCarbs;
            return this;
        }

        public Builder consumeIngredient(final QuantifiedIngredient quantifiedIngredient) {
            final IngredientMacros macros = quantifiedIngredient.getMacros();
            this.totalCalories += macros.getCalories();
            this.totalProtein += macros.getProtein();
            this.totalFat += macros.getFat();
            this.totalCarbs += macros.getCarbs();
            return this;
        }

        public DaySummary build() {
            return new DaySummary(localDate, totalCalories, totalProtein, totalFat, totalCarbs);
        }

    }
}
