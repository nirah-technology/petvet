package io.nirahtech.petvet.esp.monitor;

import java.util.UUID;

public final class ESP32 {
    private final UUID id;
    private final MacAddress mac;
    private final IPV4Address ip;
    private Mode mode;

    public ESP32(UUID id, MacAddress mac, IPV4Address ip, Mode mode) {
        this.id = id;
        this.mac = mac;
        this.ip = ip;
        this.mode = mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public UUID getId() {
        return id;
    }

    public MacAddress getMac() {
        return mac;
    }

    public IPV4Address getIp() {
        return ip;
    }

    public Mode getMode() {
        return mode;
    }
    
}
