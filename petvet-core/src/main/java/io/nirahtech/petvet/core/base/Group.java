package io.nirahtech.petvet.core.base;

import java.io.Serializable;
import java.util.stream.Stream;

public interface Group<T> extends Serializable {
    void add(T elementToAdd);
    void remove(T elementToRemove);
    Stream<T> listAll();
    boolean contains(T elementToCheck);
}
