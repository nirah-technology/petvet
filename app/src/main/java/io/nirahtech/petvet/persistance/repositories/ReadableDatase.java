package io.nirahtech.petvet.persistance.repositories;

import java.util.stream.Stream;

public interface ReadableDatase<T> {
    Stream<T> find(String[] projection, String selection, String[] selectionArguments, String sortOrder);
    Stream<T> find(String[] projection);
    Stream<T> find(String[] projection, String sortOrder);
    Stream<T> find(String[] projection, String selection, String[] selectionArguments);
}
