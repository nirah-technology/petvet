package io.nirahtech.petvet.geopulsetracker.domain;

import io.nirahtech.petvet.messaging.util.MacAddress;

public interface Scanner {
    void scan(MacAddress macAddress, final Signal signal);
}
