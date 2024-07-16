package io.nirahtech.petvet.simulator.cadastre.domain;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

public record Section(
    String identifier,
    Collection<Parcel> parcels
) implements Surface, BuiltSurface {

    @Override
    public double calculateArea() {
        return parcels.stream()
                      .mapToDouble(Parcel::calculateArea)
                      .sum();
    }

    @Override
    public double calculatePerimeter() {
        Set<Segment> uniqueSegments = new HashSet<>();
        Set<Segment> commonSegments = new HashSet<>();

        for (Parcel parcel : parcels) {
            for (Segment segment : parcel.land().sides()) {
                if (!uniqueSegments.add(segment)) {
                    commonSegments.add(segment);
                }
            }
        }

        double totalPerimeter = uniqueSegments.stream()
                                              .mapToDouble(Segment::length)
                                              .sum();
        double commonPerimeter = commonSegments.stream()
                                               .mapToDouble(Segment::length)
                                               .sum();

        return totalPerimeter - commonPerimeter;
    }

    @Override
    public double calculateBuiltSurface() {
        double builtSurface = 0.0;
        for (Parcel parcel : parcels) {
            builtSurface += parcel.calculateBuiltSurface();
        }
        return builtSurface;
    }

    @Override
    public double calculateBuiltPerimeter() {
        double builtPerimeter = 0.0;
        Set<Segment> uniqueSegments = new HashSet<>();
        Set<Segment> commonSegments = new HashSet<>();

        // Collecter les segments uniques et communs entre les parcelles
        for (Parcel parcel : parcels) {
            for (Segment segment : parcel.land().sides()) {
                if (!uniqueSegments.add(segment)) {
                    commonSegments.add(segment);
                }
            }
        }

        // Calculer le périmètre construit en utilisant les segments uniques
        for (Segment segment : uniqueSegments) {
            builtPerimeter += segment.length();
        }

        return builtPerimeter;
    }
}
