package io.nirahtech.petvet.core.pharmacy;

import io.nirahtech.petvet.core.base.Ingredient;
import io.nirahtech.petvet.core.util.Volume;

interface Alchimy {
    void addIngredient(final Ingredient ingredient, final Volume volume);
    void removeIngredient(final Ingredient ingredient);
    void changeVolume(final Ingredient ingredient, final Volume volume);
}
