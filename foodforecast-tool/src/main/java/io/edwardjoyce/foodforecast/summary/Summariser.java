package io.edwardjoyce.foodforecast.summary;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.edwardjoyce.foodforecast.model.Day;
import io.edwardjoyce.foodforecast.model.Recipe;
import io.edwardjoyce.foodforecast.persistence.FoodForecastPersistenceService;
import io.edwardjoyce.foodforecast.summary.model.DaySummary;
import io.edwardjoyce.foodforecast.summary.model.PeriodSummary;

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

    public PeriodSummary summariseBetween(final LocalDate fromInclusive, final LocalDate toExclusive) {

        final List<Day> days = foodForecastPersistenceService.getDays(fromInclusive, toExclusive);

        final var periodSummaryBuilder = PeriodSummary.builder();
        days.stream()
                .map(Day::getRecipes)
                .flatMap(Collection::stream)
                .map(Recipe::getQuantifiedIngredients)
                .flatMap(Collection::stream)
                .forEach(periodSummaryBuilder::consumeIngredient);

        final List<DaySummary> daySummaries = days.stream()
                .map(this::summariseDay)
                .collect(Collectors.toUnmodifiableList());

        return periodSummaryBuilder.setDaySummaries(daySummaries).build();
    }

    private DaySummary summariseDay(final Day day) {
        final DaySummary.Builder builder = DaySummary.builder()
                .setLocalDate(day.getLocalDate());

        day.getRecipes().stream()
                .map(Recipe::getQuantifiedIngredients)
                .flatMap(Collection::stream)
                .forEach(builder::consumeIngredient);
        return builder.build();
    }
}
