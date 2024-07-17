package io.nirahtech.petvet.simulator.cadastre.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class Section implements Surface, BuiltSurface {
    private final String identifier;
    private final Collection<Parcel> parcels;

    public Section(final String identifier, final Parcel defaultParcel, final Parcel... othersParcels) {
        this.identifier = identifier;
        this.parcels = new HashSet<>();
        Objects.requireNonNull(defaultParcel);
        this.parcels.add(defaultParcel);
        if (Objects.nonNull(othersParcels)) {
            for (Parcel parcel : othersParcels) {
                this.parcels.add(parcel);
            }
        }
    }

    public String identifier() {
        return this.identifier;
    }

    public void addParcel(final Parcel parcel) {
        if (Objects.nonNull(parcel)) {
            this.parcels.add(parcel);
        }
    }

    public Parcel createNewParcel() {
        final Parcel parcel = new Parcel(0, new Land());
        this.addParcel(parcel);
        return parcel;
    }

    public void removeParcel(final Parcel parcel) {
        if (Objects.nonNull(parcel)) {
            this.parcels.remove(parcel);
        }
    }

    public Collection<Parcel> getParcels() {
        return Collections.unmodifiableCollection(this.parcels);
    }

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
            for (Segment segment : parcel.land().getSides()) {
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
            for (Segment segment : parcel.land().getSides()) {
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

    public boolean has(Parcel parcel) {
        return this.parcels.contains(parcel);
    }
}
