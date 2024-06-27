package io.nirahtech.petvet.geopulsetracker.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import io.nirahtech.petvet.messaging.util.MacAddress;

public final class DistanceRegistry {
    private final Set<Distance> distances = new HashSet<>();

    public final Set<Distance> getFrom(final ElectronicChipBoard from) {
        return this.getFrom(((ESP32) from).getWifi().getMacAddress());
    }

    public final Set<Distance> getTo(final ElectronicChipBoard to) {
        return this.getTo(((ESP32) to).getWifi().getMacAddress());
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

    public final Set<Distance> getTo(final MacAddress to) {
        return this.distances
                .stream()
                .filter(distance -> ((ESP32) distance.getTo()).getWifi().getMacAddress().equals(to))
                .collect(Collectors.toSet());
    }

    public final Set<Distance> getFromOrTo(final ElectronicChipBoard fromOrTo) {
        return this.getFromOrTo(((ESP32) fromOrTo).getWifi().getMacAddress());
    }

    public final Set<Distance> getFromOrTo(final MacAddress fromOrTo) {
        return this.distances
        .stream()
        .filter(distance -> ((ESP32) distance.getFrom()).getWifi().getMacAddress().equals(fromOrTo) || ((ESP32) distance.getTo()).getWifi().getMacAddress().equals(fromOrTo))
        .collect(Collectors.toSet());
    }

    public Optional<Distance> getFor(final MacAddress from, final MacAddress to) {
        return this.distances
                .stream()
                .filter(distance -> ((ESP32) distance.getFrom()).getWifi().getMacAddress().equals(from))
                .filter(distance -> ((ESP32) distance.getTo()).getWifi().getMacAddress().equals(to))
                .findFirst();
    }

    public List<Distance> list() {
        return Collections.unmodifiableList(new ArrayList<>(this.distances));
    }

    public List<Distance> listUnique() {
        Set<Set<MacAddress>> uniquePairs = new HashSet<>();
        Set<Distance> uniqueDistances = new HashSet<>();

        for (Distance distance : distances) {
            MacAddress from = ((ESP32) distance.getFrom()).getWifi().getMacAddress();
            MacAddress to = ((ESP32) distance.getTo()).getWifi().getMacAddress();

            // Créer un set contenant les deux adresses Mac pour garantir l'unicité bidirectionnelle
            Set<MacAddress> pair = new HashSet<>(Arrays.asList(from, to));

            // Ajouter la distance si la paire n'est pas déjà présente dans le set
            if (!uniquePairs.contains(pair)) {
                uniquePairs.add(pair);
                uniqueDistances.add(distance);
            }
        }

        // Retourner la liste non modifiable des distances uniques
        return Collections.unmodifiableList(new ArrayList<>(uniqueDistances));
    }

}
