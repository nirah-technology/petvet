package io.nirahtech.petvet.installer.domain;

import java.util.Objects;

public final class SoftwareReference {
    private static SoftwareReference instance = null;
    public static SoftwareReference getInstance(final Software software) {
        if (Objects.isNull(instance)) {
            instance = new SoftwareReference(software);
        }
        return instance;
    }

    private final Software software;

    private SoftwareReference(final Software software) {
        this.software = software;
    } 

    public Software getSoftware() {
        return software;
    }
}
