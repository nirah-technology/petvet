package io.nirahtech.petvet.core.animalpark;

import java.time.LocalDate;

interface LifeCycle {
    void birth(final LocalDate date);
    void death(final LocalDate date);
}
