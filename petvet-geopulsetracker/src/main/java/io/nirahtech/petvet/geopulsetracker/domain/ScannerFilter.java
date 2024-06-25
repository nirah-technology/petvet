package io.nirahtech.petvet.geopulsetracker.domain;

import java.util.Optional;

import io.nirahtech.petvet.messaging.util.MacAddress;

public interface ScannerFilter {
    Optional<MacAddress> filter(MacAddress macAddress);
}
