package io.edwardjoyce.foodforecast.summary;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.edwardjoyce.foodforecast.model.Day;
import io.edwardjoyce.foodforecast.model.Recipe;
import io.edwardjoyce.foodforecast.persistence.FoodForecastPersistenceService;
import io.edwardjoyce.foodforecast.summary.model.DaySummary;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class Summariser {

    private final FoodForecastPersistenceService foodForecastPersistenceService;

    @Inject
    public Summariser(final FoodForecastPersistenceService foodForecastPersistenceService) {
        this.foodForecastPersistenceService = foodForecastPersistenceService;
    }

    public List<DaySummary> summariseBetween(final LocalDate fromInclusive, final LocalDate toExclusive) {
        return foodForecastPersistenceService.getDays(fromInclusive, toExclusive)
                .stream()
                .map(this::summariseDay)
                .collect(Collectors.toList());
    }

    private DaySummary summariseDay(final Day day) {
        final DaySummary.Builder builder = DaySummary.builder();
        day.getRecipes().stream()
                .map(Recipe::getQuantifiedIngredients)
                .flatMap(Collection::stream)
//                .sorted() //TODO use comparator to sort
                .forEach(builder::consumeIngredient);
        return builder.build();
    }
}
