package io.nirahtech.petvet.installer.domain;

import java.util.Objects;

public record Version(
    int major,
    int minor,
    int patch,
    Tag tag
) {
    @Override
    public final String toString() {
        return String.format("%s.%s.%s%s", this.major, this.minor, this.patch, (Objects.nonNull(this.tag)) ? "-".concat(this.tag.name()) : "");
    }
}
