package io.nirahtech.petvet.geopulsetracker.domain;

import io.nirahtech.petvet.messaging.util.MacAddress;

public final class Device {
    private final MacAddress mac;
    private Signal signal;

    public Device(MacAddress mac, float signalStrengthInDBm) {
        this.mac = mac;
        this.signal = new Signal(signalStrengthInDBm);
    }
    public void setSignal(long signalStrengthInDBm) {
        this.signal = new Signal(signalStrengthInDBm);
    }
    public MacAddress getMac() {
        return mac;
    }
    public Signal getSignalBm() {
        return signal;
    }
    
}
