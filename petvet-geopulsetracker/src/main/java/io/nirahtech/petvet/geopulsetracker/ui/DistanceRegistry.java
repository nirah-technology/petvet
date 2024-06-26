package io.nirahtech.petvet.geopulsetracker.ui;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.nirahtech.petvet.geopulsetracker.domain.ESP32;
import io.nirahtech.petvet.geopulsetracker.domain.ElectronicChipBoard;
import io.nirahtech.petvet.messaging.util.MacAddress;

public final class DistanceRegistry {
    private final Set<Distance> distances = new HashSet<>();

    public final Set<Distance> getFrom(final ElectronicChipBoard from) {
        return this.getFrom(((ESP32) from).getWifi().getMacAddress());
    }

    public Optional<Distance> getFor(final ElectronicChipBoard from, final ElectronicChipBoard to) {
        return this.getFor(((ESP32) from).getWifi().getMacAddress(), ((ESP32) to).getWifi().getMacAddress());
    }

    public Distance register(final ElectronicChipBoard from, final ElectronicChipBoard to,
            final float signalStrenghtInDBM) {
        final Distance distance = new Distance(from, to, signalStrenghtInDBM);
        this.distances.add(distance);
        return distance;
    }

    public final Set<Distance> getFrom(final MacAddress from) {
        return this.distances
                .stream()
                .filter(distance -> ((ESP32) distance.getFrom()).getWifi().getMacAddress().equals(from))
                .collect(Collectors.toSet());
    }

    public Optional<Distance> getFor(final MacAddress from, final MacAddress to) {
        return this.distances
                .stream()
                .filter(distance -> ((ESP32) distance.getFrom()).getWifi().getMacAddress().equals(from))
                .filter(distance -> ((ESP32) distance.getTo()).getWifi().getMacAddress().equals(to))
                .findFirst();
    }

}
