package io.nirahtech.petvet.geopulsetracker.domain;

import java.util.Optional;

public interface ScannerFilter {
    Optional<MacAddress> filter(MacAddress macAddress);
}
