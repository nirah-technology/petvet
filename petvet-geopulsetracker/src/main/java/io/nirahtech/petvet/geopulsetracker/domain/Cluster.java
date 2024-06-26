package io.nirahtech.petvet.geopulsetracker.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class Cluster<T extends MicroController> {
    private final Set<T> nodes = new HashSet<>();

    public void add(final T microController) {
        if (Objects.nonNull(microController)) {
            this.nodes.add(microController);
        }
    }

    public void remove(final T microController) {
        if (Objects.nonNull(microController)) {
            this.nodes.remove(microController);
        }
    }

    public boolean contains(final T microController) {
        return this.nodes.contains(microController);
    }

    public Stream<T> filter(Predicate<T> searchFilter) {
        return this.nodes.stream().filter(searchFilter);
    }
}
