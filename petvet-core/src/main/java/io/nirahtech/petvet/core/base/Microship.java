package io.nirahtech.petvet.core.base;

import java.io.Serializable;

public final class Microship implements Serializable {
    private static final int SIZE = 15;

    private final String code;
    private Microship(final String code) {
        this.code = code;
    }
    public final String getCode() {
        return this.code;
    }

    public static Microship of(final String code) {
        return new Microship(code);
    }
}
