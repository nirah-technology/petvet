package io.nirahtech.petvet.simulator.electronicalcard.scanners;

import io.nirahtech.petvet.messaging.util.MacAddress;

public record Device(
    MacAddress bssid,
    String ssid,
    int frequency,
    float signalLevel
    ) {
}
