package io.edwardjoyce.foodforecast.model;

import java.util.Collections;
import java.util.List;

public class Recipe {

    private final String name;
    private final List<QuantifiedIngredient> quantifiedIngredients;

    public Recipe(final String name,
                  final List<QuantifiedIngredient> quantifiedIngredients) {
        this.name = name;
        this.quantifiedIngredients = Collections.unmodifiableList(quantifiedIngredients);
    }

    public String getName() {
        return name;
    }

    public List<QuantifiedIngredient> getQuantifiedIngredients() {
        return quantifiedIngredients;
    }
}
