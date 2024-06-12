package io.nirahtech.petvet.core.base;

import java.io.Serializable;

import io.nirahtech.petvet.core.util.identifier.Identifier;

public final class Microship implements Serializable {
    private static final int SIZE = 15;

    private Identifier identifier;
    private final String code;
    
    private Microship(final String code) {
        this.code = code;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public final String getCode() {
        return this.code;
    }

    public static Microship of(final String code) {
        return new Microship(code);
    }
}
