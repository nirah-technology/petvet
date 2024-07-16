package io.nirahtech.petvet.simulator.cadastre.domain;

import java.awt.Point;

public record Segment(
        Point from,
        Point to) {
    public double length() {
        return from.distance(to);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Segment segment = (Segment) other;

        return (from.equals(segment.from) && to.equals(segment.to)) ||
               (from.equals(segment.to) && to.equals(segment.from));
    }

    @Override
    public int hashCode() {
        return from.hashCode() + to.hashCode();
    }

}
