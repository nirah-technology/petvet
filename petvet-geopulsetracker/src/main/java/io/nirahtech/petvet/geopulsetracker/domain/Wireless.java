package io.nirahtech.petvet.geopulsetracker.domain;

import java.util.Map;
import java.util.Optional;

public interface Wireless {
    void on();
    Map<MacAddress, Signal> scan();
    void off();

    Optional<Signal> emitSignal(MacAddress macAddress);
}
