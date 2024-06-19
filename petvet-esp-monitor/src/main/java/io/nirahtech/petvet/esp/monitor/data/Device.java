package io.nirahtech.petvet.esp.monitor.data;

import java.net.InetAddress;

import io.nirahtech.petvet.messaging.util.MacAddress;

public final class Device {
    private final MacAddress mac;
    private final InetAddress ip;
    private float signalStrengthInDBm;

    public Device(MacAddress mac, InetAddress ip, float signalStrengthInDBm) {
        this.mac = mac;
        this.ip = ip;
        this.signalStrengthInDBm = signalStrengthInDBm;
    }
    public void setSignalStrengthInDBm(long signalStrengthInDBm) {
        this.signalStrengthInDBm = signalStrengthInDBm;
    }
    public MacAddress getMac() {
        return mac;
    }
    public InetAddress getIp() {
        return ip;
    }
    public float getSignalStrengthInDBm() {
        return signalStrengthInDBm;
    }
    public void setSignalStrengthInDBm(float signalStrengthInDBm) {
        this.signalStrengthInDBm = signalStrengthInDBm;
    }
    
}
