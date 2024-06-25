package io.nirahtech.petvet.geopulsetracker.domain;

import java.util.UUID;

public final class ESP32 extends ElectronicChipBoard {

    private final UUID id;
    private final WirelessNetworkInterfaceCard wifi;
    private final WirelessNetworkInterfaceCard bluetooth;

    private final double minTemperature = -40.0D;
    private final double maxTemperature = 85.0D;

    public ESP32(UUID id, WiFi wifi, Bluetooth bluetooth) {
        super(5, 3);
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

    @Override
    public void powerOn() {
        super.powerOn();
        this.wifi.on();
        this.bluetooth.on();
    }

    @Override
    public void powerOff() {
        super.powerOff();
        this.wifi.off();
        this.bluetooth.off();
    }
}
