package io.nirahtech.petvet.core.clinic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import io.nirahtech.petvet.core.base.NaturalCare;

public class Treatment {
    private final Set<NaturalCare> cares;

    public Treatment(final Set<NaturalCare> cares) {
        this.cares = new HashSet<>(Objects.requireNonNull(cares));
    }

    /**
     * @return the cares
     */
    public final Set<NaturalCare> getCares() {
        return Collections.unmodifiableSet(this.cares);
    }
}
