package io.edwardjoyce.foodforecast.summary;

import com.google.inject.Guice;
import com.google.inject.Inject;
import io.edwardjoyce.foodforecast.model.*;
import io.edwardjoyce.foodforecast.persistence.FoodForecastPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class SummariserTest {

    @Inject
    private Summariser summariser;

    @Inject
    private FoodForecastPersistenceService foodForecastPersistenceService;

    @BeforeEach
    void setup() {
        Guice.createInjector(binder -> {
            binder.bind(FoodForecastPersistenceService.class).
                    toInstance(mock(FoodForecastPersistenceService.class));
        }).injectMembers(this);
    }

    @Test
    void shouldSummarise() {
        given(foodForecastPersistenceService
                .getDays(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(mockDays());
        final var start = LocalDate.of(2020, 6, 6);
        summariser.summariseBetween(start,
                start.plusDays(7)).forEach(System.out::println);
    }

    private List<Day> mockDays() {

        Ingredient chicken = Ingredient.builder()
                .setName("chicken")
                .addMacros(QuantityUnit.GRAMS_100,
                        new IngredientMacros(100, 30, 20, 40))
                .build();

        Recipe fajitas = new Recipe("fajitas",
                List.of(new QuantifiedIngredient(chicken, 1.5, QuantityUnit.GRAMS_100)));

        return List.of(new Day(
                LocalDate.of(2020, 6, 6),
                List.of(fajitas)));
    }
}