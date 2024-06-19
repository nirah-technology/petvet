package io.nirahtech.petvet.esp.scanners;

import io.nirahtech.petvet.messaging.util.MacAddress;

public record Device(
    MacAddress bssid,
    String ssid,
    int frequency,
    float signalLevel
    ) {
}
