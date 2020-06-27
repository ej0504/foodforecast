package io.edwardjoyce.foodforecast.persistence;

import io.edwardjoyce.foodforecast.model.Day;

import java.time.LocalDate;
import java.util.List;

public interface FoodForecastPersistenceService {

    List<Day> getDays(LocalDate fromInclusive, LocalDate toExclusive);
}
