package io.edwardjoyce.foodforecast.summary.model;

import com.google.common.collect.ImmutableMap;
import io.edwardjoyce.foodforecast.model.IngredientMacros;
import io.edwardjoyce.foodforecast.model.QuantifiedIngredient;
import io.edwardjoyce.foodforecast.model.QuantityUnit;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public final class DaySummary {

    private final LocalDate localDate;
    private final double totalCalories;
    private final double totalProtein;
    private final double totalFat;
    private final double totalCarbs;
    private final Map<String, Map<QuantityUnit, Double>> shoppingList;

    public DaySummary(final LocalDate localDate,
                      final double totalCalories,
                      final double totalProtein,
                      final double totalFat,
                      final double totalCarbs,
                      final Map<String, Map<QuantityUnit, Double>> shoppingList) {
        this.localDate = localDate;
        this.totalCalories = totalCalories;
        this.totalProtein = totalProtein;
        this.totalFat = totalFat;
        this.totalCarbs = totalCarbs;
        this.shoppingList = ImmutableMap.copyOf(shoppingList);
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public double getTotalCalories() {
        return totalCalories;
    }

    public double getTotalProtein() {
        return totalProtein;
    }

    public double getTotalFat() {
        return totalFat;
    }

    public double getTotalCarbs() {
        return totalCarbs;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LocalDate localDate;
        private double totalCalories;
        private double totalProtein;
        private double totalFat;
        private double totalCarbs;
        private final Map<String, Map<QuantityUnit, Double>> shoppingList
                = new HashMap<>();

        private Builder() {
        }

        public Builder setLocalDate(LocalDate localDate) {
            this.localDate = localDate;
            return this;
        }

        public Builder setTotalCalories(double totalCalories) {
            this.totalCalories = totalCalories;
            return this;
        }

        public Builder setTotalProtein(double totalProtein) {
            this.totalProtein = totalProtein;
            return this;
        }

        public Builder setTotalFat(double totalFat) {
            this.totalFat = totalFat;
            return this;
        }

        public Builder setTotalCarbs(double totalCarbs) {
            this.totalCarbs = totalCarbs;
            return this;
        }

        public Builder consumeIngredient(
                final QuantifiedIngredient quantifiedIngredient) {
            final IngredientMacros macros = quantifiedIngredient.getMacros();
            this.totalCalories += macros.getCalories();
            this.totalProtein += macros.getProtein();
            this.totalFat += macros.getFat();
            this.totalCarbs += macros.getCarbs();

            this.shoppingList.compute(
                    quantifiedIngredient.getIngredient().getName(),
                    (k, v) -> v == null ?
                            createUnitAmountMap(quantifiedIngredient)
                            : updateUnitAmountMap(v, quantifiedIngredient));
            return this;
        }

        public DaySummary build() {
            return new DaySummary(localDate,
                    totalCalories,
                    totalProtein,
                    totalFat,
                    totalCarbs,
                    shoppingList);
        }
    }

    private static Map<QuantityUnit, Double> createUnitAmountMap(
            final QuantifiedIngredient quantifiedIngredient) {
        Map<QuantityUnit, Double> map = new HashMap<>();

        map.put(quantifiedIngredient.getQuantityUnit(),
                quantifiedIngredient.getAmount());
        return map;
    }

    private static Map<QuantityUnit, Double> updateUnitAmountMap(
            final Map<QuantityUnit, Double> map,
            final QuantifiedIngredient quantifiedIngredient) {
        map.compute(quantifiedIngredient.getQuantityUnit(),
                (k2, v2) -> v2 == null ?
                        quantifiedIngredient.getAmount()
                        : v2 + quantifiedIngredient.getAmount());

        return map;
    }

    @Override
    public String toString() {
        return "DaySummary{" +
                "localDate=" + localDate +
                ", totalCalories=" + totalCalories +
                ", totalProtein=" + totalProtein +
                ", totalFat=" + totalFat +
                ", totalCarbs=" + totalCarbs +
                ", shoppingList=" + shoppingList +
                '}';
    }
}
