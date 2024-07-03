package io.nirahtech.petvet.installer.domain;

public record Software(
    String name,
    Version version
) implements Comparable<Software> {

    @Override
    public int compareTo(Software other) {
        int nameComparison = this.name.compareTo(other.name);
        if (nameComparison != 0) {
            return nameComparison;
        }
        return this.version.compareTo(other.version);
    }
}
