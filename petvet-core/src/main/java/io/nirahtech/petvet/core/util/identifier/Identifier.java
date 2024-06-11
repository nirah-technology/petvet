package io.nirahtech.petvet.core.util.identifier;

import java.io.Serializable;

@FunctionalInterface
public interface Identifier extends Serializable {
    long getId();
}
