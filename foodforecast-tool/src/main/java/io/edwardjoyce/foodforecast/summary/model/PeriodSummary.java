package io.edwardjoyce.foodforecast.summary.model;

import io.edwardjoyce.foodforecast.model.QuantifiedIngredient;
import io.edwardjoyce.foodforecast.model.QuantityUnit;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TODO simplify this Map of Maps logic- getting into and out of v. unreadable
public class PeriodSummary {

    private final List<DaySummary> daySummaries;
    private final List<ShoppingListDetail> shoppingList;

    public PeriodSummary(final List<DaySummary> daySummaries,
                         final List<ShoppingListDetail> shoppingList) {
        this.daySummaries = daySummaries;
        this.shoppingList = shoppingList;
    }

    public List<DaySummary> getDaySummaries() {
        return daySummaries;
    }

    public List<ShoppingListDetail> getShoppingList() {
        return shoppingList;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<DaySummary> daySummaries;

        private final Map<String, Map<QuantityUnit, Double>> shoppingList
                = new HashMap<>();

        private Builder() {
        }

        public Builder setDaySummaries(List<DaySummary> daySummaries) {
            this.daySummaries = daySummaries;
            return this;
        }

        public Builder consumeIngredient(final QuantifiedIngredient quantifiedIngredient) {
            this.shoppingList.compute(
                    quantifiedIngredient.getIngredient().getName(),
                    (k, v) -> v == null ?
                            createUnitAmountMap(quantifiedIngredient)
                            : updateUnitAmountMap(v, quantifiedIngredient));
            return this;
        }

        private static Map<QuantityUnit, Double> createUnitAmountMap(
                final QuantifiedIngredient quantifiedIngredient) {
            final Map<QuantityUnit, Double> map = new HashMap<>();

            map.put(quantifiedIngredient.getQuantityUnit(),
                    quantifiedIngredient.getAmount());
            return map;
        }

        private static Map<QuantityUnit, Double> updateUnitAmountMap(
                final Map<QuantityUnit, Double> map,
                final QuantifiedIngredient quantifiedIngredient) {
            map.compute(quantifiedIngredient.getQuantityUnit(),
                    (k, v) -> v == null ?
                            quantifiedIngredient.getAmount()
                            : v + quantifiedIngredient.getAmount());

            return map;
        }

        public PeriodSummary build() {
            final List<ShoppingListDetail> ingredients
                    = shoppingList.entrySet().stream()
                    .flatMap(this::mapEntryToShoppingListDetailStream)
                    .collect(Collectors.toList());

            return new PeriodSummary(daySummaries, ingredients);
        }

        private Stream<ShoppingListDetail> mapEntryToShoppingListDetailStream(
                final Map.Entry<String, Map<QuantityUnit, Double>> entry) {
            return entry.getValue().entrySet().stream()
                    .map(e -> new ShoppingListDetail(entry.getKey(), e.getValue(), e.getKey()));
        }
    }
}