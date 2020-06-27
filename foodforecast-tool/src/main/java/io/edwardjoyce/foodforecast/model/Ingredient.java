package io.edwardjoyce.foodforecast.model;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class Ingredient {

    private final String name;
    private final Map<QuantityUnit, IngredientMacros> macrosPerUnitMap;

    public Ingredient(final String name,
                      final Map<QuantityUnit, IngredientMacros> macrosPerUnitMap) {
        this.name = name;
        this.macrosPerUnitMap = ImmutableMap.copyOf(macrosPerUnitMap);
    }

    public String getName() {
        return name;
    }

    public Optional<IngredientMacros> getMacrosForUnit(final QuantityUnit unit) {
        return Optional.ofNullable(macrosPerUnitMap.get(unit));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private final Map<QuantityUnit, IngredientMacros> macrosPerUnitMap
                = new HashMap<>();

        private Builder() { }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder addMacros(final QuantityUnit unit,
                                 final IngredientMacros macros) {
            macrosPerUnitMap.put(unit, macros);
            return this;
        }

        public Ingredient build() {
            return new Ingredient(name, macrosPerUnitMap);
        }

    }
}
