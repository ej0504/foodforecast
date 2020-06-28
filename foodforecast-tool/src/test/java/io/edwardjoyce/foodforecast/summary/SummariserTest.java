package io.edwardjoyce.foodforecast.summary;

import com.google.inject.Guice;
import com.google.inject.Inject;
import io.edwardjoyce.foodforecast.NoMacrosForUnitException;
import io.edwardjoyce.foodforecast.model.*;
import io.edwardjoyce.foodforecast.persistence.FoodForecastPersistenceService;
import io.edwardjoyce.foodforecast.summary.model.DaySummary;
import io.edwardjoyce.foodforecast.summary.model.PeriodSummary;
import io.edwardjoyce.foodforecast.summary.model.ShoppingListDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
//        summariser.summariseBetween(start,
//                start.plusDays(7)).forEach(System.out::println);


        // assert:
        // - single day, recipe, ingredient
        // - above, but multiple recipes, with ingredients in multiple quantities
        // - above, but multiple days
    }

    @Test
    void shouldSummarise_noDays() {
        given(foodForecastPersistenceService
                .getDays(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(Collections.emptyList());
        final var start = LocalDate.of(2020, 6, 6);
        final PeriodSummary periodSummary = summariser.summariseBetween(start,
                start.plusDays(7));

        assertThat(periodSummary.getDaySummaries()).isEmpty();
        assertThat(periodSummary.getShoppingList()).isEmpty();
    }

    @Test
    void shouldSummarise_noRecipes() {
        final var start = LocalDate.of(2020, 6, 6);
        given(foodForecastPersistenceService
                .getDays(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(mockDays_noRecipes(start));
        final PeriodSummary periodSummary = summariser.summariseBetween(start,
                start.plusDays(7));

        assertThat(periodSummary.getDaySummaries())
                .usingFieldByFieldElementComparator()
                .containsExactly(
                        DaySummary.builder()
                                .setLocalDate(start)
                                .setTotalCalories(0)
                                .setTotalProtein(0)
                                .setTotalFat(0)
                                .setTotalCarbs(0)
                                .build(),
                        DaySummary.builder()
                                .setLocalDate(start.plusDays(1))
                                .setTotalCalories(0)
                                .setTotalProtein(0)
                                .setTotalFat(0)
                                .setTotalCarbs(0)
                                .build());

        assertThat(periodSummary.getShoppingList()).isEmpty();
    }

    private List<Day> mockDays_noRecipes(final LocalDate localDate) {
        return List.of(
                new Day(
                        localDate,
                        Collections.emptyList()),
                new Day(
                        localDate.plusDays(1),
                        Collections.emptyList()));
    }

    @Test
    void shouldSummarise_noIngredients() {
        final var start = LocalDate.of(2020, 6, 6);
        given(foodForecastPersistenceService
                .getDays(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(mockDays_noIngredients(start));
        final PeriodSummary periodSummary = summariser.summariseBetween(start,
                start.plusDays(7));

        assertThat(periodSummary.getDaySummaries())
                .usingFieldByFieldElementComparator()
                .containsExactly(
                        DaySummary.builder()
                                .setLocalDate(start)
                                .setTotalCalories(0)
                                .setTotalProtein(0)
                                .setTotalFat(0)
                                .setTotalCarbs(0)
                                .build(),
                        DaySummary.builder()
                                .setLocalDate(start.plusDays(1))
                                .setTotalCalories(0)
                                .setTotalProtein(0)
                                .setTotalFat(0)
                                .setTotalCarbs(0)
                                .build());

        assertThat(periodSummary.getShoppingList()).isEmpty();
    }

    private List<Day> mockDays_noIngredients(final LocalDate localDate) {
        final Recipe fajitas = new Recipe("fajitas",
                Collections.emptyList());

        final Recipe stirFry = new Recipe("stirFry",
                Collections.emptyList());

        final Recipe chickenSalad = new Recipe("chickenSalad",
                Collections.emptyList());

        final Recipe eggSandwich = new Recipe("eggSandwich",
                Collections.emptyList());

        return List.of(
                new Day(
                        localDate,
                        List.of(fajitas, stirFry)),
                new Day(
                        localDate.plusDays(1),
                        List.of(chickenSalad, eggSandwich)));
    }

    @Test
    void shouldSummarise_shouldThrowExceptionForNoMatchingUnits() {
        final var start = LocalDate.of(2020, 6, 6);

        given(foodForecastPersistenceService
                .getDays(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(mockDays_noMatchingUnits(start));

        assertThatThrownBy(() -> summariser.summariseBetween(start,
                start.plusDays(7)))
                .hasMessage("Macros not available for the unit GRAMS_100 for chicken")
                .isInstanceOf(NoMacrosForUnitException.class);
    }


    private List<Day> mockDays_noMatchingUnits(final LocalDate localDate) {
        Ingredient chicken = Ingredient.builder()
                .setName("chicken")
                .addMacros(QuantityUnit.SINGLE,
                        new IngredientMacros(100, 30, 20, 10))
                .build();

        final Recipe fajitas = new Recipe("fajitas",
                List.of(new QuantifiedIngredient(
                        chicken,
                        2,
                        QuantityUnit.GRAMS_100)));

        return List.of(
                new Day(
                        localDate,
                        List.of(fajitas)));
    }

    @Test
    void shouldSummarise_multipleDays() {
        final var start = LocalDate.of(2020, 6, 6);
        given(foodForecastPersistenceService
                .getDays(any(LocalDate.class), any(LocalDate.class)))
                .willReturn(mockDays_multipleDays(start));
        final PeriodSummary periodSummary = summariser.summariseBetween(start,
                start.plusDays(7));

        assertThat(periodSummary.getDaySummaries())
                .usingFieldByFieldElementComparator()
                .containsExactly(
                        DaySummary.builder()
                                .setLocalDate(start)
                                .setTotalCalories(1170)
                                .setTotalProtein(174)
                                .setTotalFat(180)
                                .setTotalCarbs(225)
                                .build(),
                        DaySummary.builder()
                                .setLocalDate(start.plusDays(1))
                                .setTotalCalories(270)
                                .setTotalProtein(75)
                                .setTotalFat(50)
                                .setTotalCarbs(30)
                                .build(),
                        DaySummary.builder()
                                .setLocalDate(start.plusDays(2))
                                .setTotalCalories(1500)
                                .setTotalProtein(300)
                                .setTotalFat(200)
                                .setTotalCarbs(100)
                                .build());

        assertThat(periodSummary.getShoppingList())
                .usingFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        new ShoppingListDetail("chicken", 7.5, QuantityUnit.GRAMS_100),
                        new ShoppingListDetail("chicken", 1, QuantityUnit.SINGLE),
                        new ShoppingListDetail("lettuce", 2, QuantityUnit.GRAMS_100),
                        new ShoppingListDetail("wraps", 3, QuantityUnit.SINGLE),
                        new ShoppingListDetail("noodles", 2, QuantityUnit.GRAMS_100)
                );
    }

    private List<Day> mockDays_multipleDays(final LocalDate localDate) {
        Ingredient chicken = Ingredient.builder()
                .setName("chicken")
                .addMacros(QuantityUnit.SINGLE,
                        new IngredientMacros(1500, 300, 200, 100))
                .addMacros(QuantityUnit.GRAMS_100,
                        new IngredientMacros(100, 30, 20, 10))
                .build();

        Ingredient wrap = Ingredient.builder()
                .setName("wraps")
                .addMacros(QuantityUnit.SINGLE,
                        new IngredientMacros(150, 2, 20, 40))
                .build();

        Ingredient lettuce = Ingredient.builder()
                .setName("lettuce")
                .addMacros(QuantityUnit.GRAMS_100,
                        new IngredientMacros(20, 0, 0, 5))
                .build();

        Ingredient noodles = Ingredient.builder()
                .setName("noodles")
                .addMacros(QuantityUnit.GRAMS_100,
                        new IngredientMacros(100, 9, 10, 25))
                .build();

        final Recipe fajitas = new Recipe("fajitas",
                List.of(
                        new QuantifiedIngredient(chicken, 2.5, QuantityUnit.GRAMS_100),
                        new QuantifiedIngredient(lettuce, 1, QuantityUnit.GRAMS_100),
                        new QuantifiedIngredient(wrap, 3, QuantityUnit.SINGLE)));

        final Recipe stirFry = new Recipe("stirFry",
                List.of(
                        new QuantifiedIngredient(chicken, 2.5, QuantityUnit.GRAMS_100),
                        new QuantifiedIngredient(noodles, 2, QuantityUnit.GRAMS_100)));

        final Recipe chickenSalad = new Recipe("chickenSalad",
                List.of(
                        new QuantifiedIngredient(chicken, 2.5, QuantityUnit.GRAMS_100),
                        new QuantifiedIngredient(lettuce, 1, QuantityUnit.GRAMS_100)
                ));

        final Recipe roastChicken = new Recipe("roastChicken",
                List.of(
                        new QuantifiedIngredient(chicken, 1, QuantityUnit.SINGLE)
                ));

        return List.of(
                new Day(
                        localDate,
                        List.of(fajitas, stirFry)),
                new Day(
                        localDate.plusDays(1),
                        List.of(chickenSalad)),
                new Day(
                        localDate.plusDays(2),
                        List.of(roastChicken)));
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