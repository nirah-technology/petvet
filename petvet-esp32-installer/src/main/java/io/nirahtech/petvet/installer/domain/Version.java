package io.nirahtech.petvet.installer.domain;

import java.util.Objects;

public record Version(
    int major,
    int minor,
    int patch,
    Tag tag
) implements Comparable<Version> {
    @Override
    public final String toString() {
        return String.format("%s.%s.%s%s", this.major, this.minor, this.patch, (Objects.nonNull(this.tag)) ? "-".concat(this.tag.name()) : "");
    }

    public static Version of(String versionAsString) {
        if (versionAsString == null || versionAsString.isEmpty()) {
            throw new IllegalArgumentException("Version string cannot be null or empty");
        }

        String[] mainParts = versionAsString.split("-");
        String[] versionParts = mainParts[0].split("\\.");

        if (versionParts.length != 3) {
            throw new IllegalArgumentException("Version string must be in the format 'major.minor.patch' or 'major.minor.patch-tag'");
        }

        try {
            int major = Integer.parseInt(versionParts[0]);
            int minor = Integer.parseInt(versionParts[1]);
            int patch = Integer.parseInt(versionParts[2]);
            Tag tag = (mainParts.length == 2) ? Tag.valueOf(mainParts[1].toUpperCase()) : null;

            return new Version(major, minor, patch, tag);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Major, minor, and patch must be integers", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid tag value", e);
        }

    }

    @Override
    public int compareTo(Version other) {
        int majorComparison = Integer.compare(this.major, other.major);
        if (majorComparison != 0) {
            return majorComparison;
        }

        int minorComparison = Integer.compare(this.minor, other.minor);
        if (minorComparison != 0) {
            return minorComparison;
        }

        int patchComparison = Integer.compare(this.patch, other.patch);
        if (patchComparison != 0) {
            return patchComparison;
        }

        if (this.tag == null && other.tag != null) {
            return -1;
        } else if (this.tag != null && other.tag == null) {
            return 1;
        } else if (this.tag == null && other.tag == null) {
            return 0;
        }

        return this.tag.compareTo(other.tag);

    }
}
