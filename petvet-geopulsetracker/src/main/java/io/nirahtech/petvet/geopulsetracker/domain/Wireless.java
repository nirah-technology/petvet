package io.nirahtech.petvet.geopulsetracker.domain;

import java.util.Map;
import java.util.Optional;

import io.nirahtech.petvet.messaging.util.MacAddress;

public interface Wireless {
    void on();
    Map<MacAddress, Signal> scan();
    void off();

    Optional<Signal> emitSignal(MacAddress macAddress);
}
