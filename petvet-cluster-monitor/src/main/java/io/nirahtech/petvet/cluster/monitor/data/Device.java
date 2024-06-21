package io.nirahtech.petvet.cluster.monitor.data;

import io.nirahtech.petvet.messaging.util.MacAddress;

public final class Device {
    private final MacAddress mac;
    private float signalStrengthInDBm;

    public Device(MacAddress mac, float signalStrengthInDBm) {
        this.mac = mac;
        this.signalStrengthInDBm = signalStrengthInDBm;
    }
    public void setSignalStrengthInDBm(long signalStrengthInDBm) {
        this.signalStrengthInDBm = signalStrengthInDBm;
    }
    public MacAddress getMac() {
        return mac;
    }
    public float getSignalStrengthInDBm() {
        return signalStrengthInDBm;
    }
    public void setSignalStrengthInDBm(float signalStrengthInDBm) {
        this.signalStrengthInDBm = signalStrengthInDBm;
    }
    
}
