package io.edwardjoyce.foodforecast.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class Day {

    private final LocalDate localDate;
    private final List<Recipe> recipes;

    public Day(final LocalDate localDate, final List<Recipe> recipes) {
        this.localDate = localDate;
        this.recipes = Collections.unmodifiableList(recipes);
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
