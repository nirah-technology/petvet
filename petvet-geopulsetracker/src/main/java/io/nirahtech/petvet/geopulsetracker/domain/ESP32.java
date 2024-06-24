package io.nirahtech.petvet.geopulsetracker.domain;

import java.util.UUID;

public final class ESP32 extends ElectronicalCard {

    private final UUID id;
    private final WirelessNetworkInterfaceCard wifi;
    private final WirelessNetworkInterfaceCard bluetooth;

    public ESP32(UUID id, WiFi wifi, Bluetooth bluetooth) {
        this.id = id;
        this.wifi = wifi;
        this.bluetooth = bluetooth;
    }
    public ESP32(UUID id, Bluetooth bluetooth) {
        this(id, null, bluetooth);
    }
    public ESP32(UUID id, WiFi wifi) {
        this(id, wifi, null);
    }
    public ESP32(UUID id) {
        this(id, null, null);
    }

    public WirelessNetworkInterfaceCard getBluetooth() {
        return bluetooth;
    }

    public WirelessNetworkInterfaceCard getWifi() {
        return wifi;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    public static final ESP32 generate() {
        final WiFi wifi = new WiFi(MacAddress.generate());
        final Bluetooth bluetooth = new Bluetooth(MacAddress.generate());
        return new ESP32(UUID.randomUUID(), wifi, bluetooth);
    }
}
