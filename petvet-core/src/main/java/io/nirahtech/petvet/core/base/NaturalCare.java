package io.nirahtech.petvet.core.base;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import io.nirahtech.petvet.core.util.Volume;

public class NaturalCare {
    private final Map<Ingredient, Volume> ingredients;
    private final Duration duration;
    private final LocalDate beginDate;

    public NaturalCare(
        final Map<Ingredient, Volume> ingredients,
        final Duration duration,
        final LocalDate beginDate
    ) {
        this.ingredients = Collections.unmodifiableMap(Objects.requireNonNull(ingredients, "Ingredients for natural care is required."));
        this.duration = Objects.requireNonNull(duration, "Duration for natural care is required.");
        this.beginDate = Objects.requireNonNull(beginDate, "Begin date for natural care is required.");
    }

    /**
     * @return the beginDate
     */
    public final LocalDate getBeginDate() {
        return this.beginDate;
    }
    /**
     * @return the duration
     */
    public final Duration getDuration() {
        return this.duration;
    }
    /**
     * @return the ingredients
     */
    public final Map<Ingredient, Volume> getIngredients() {
        return this.ingredients;
    }
}
