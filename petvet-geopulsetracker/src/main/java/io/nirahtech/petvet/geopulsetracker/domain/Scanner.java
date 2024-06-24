package io.nirahtech.petvet.geopulsetracker.domain;

public interface Scanner {
    void scan(MacAddress macAddress, final Signal signal);
}
