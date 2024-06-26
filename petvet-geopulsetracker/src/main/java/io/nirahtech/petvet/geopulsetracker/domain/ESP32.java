package io.nirahtech.petvet.geopulsetracker.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.nirahtech.petvet.messaging.util.MacAddress;

public final class ESP32 extends ElectronicChipBoard {

    private static final Map<MacAddress, ESP32> INSTANCES = new HashMap<>();

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
    public static final ESP32 getOrCreateWithWiFiMacAddress(final MacAddress wifiBSSID) {
        ESP32 esp;
        if (INSTANCES.containsKey(wifiBSSID)) {
            esp = INSTANCES.get(wifiBSSID);
        } else {
            final WiFi wifi = new WiFi(wifiBSSID);
            final Bluetooth bluetooth = new Bluetooth(MacAddress.generate());
            esp = new ESP32(UUID.randomUUID(), wifi, bluetooth);
            INSTANCES.put(wifiBSSID, esp);
            INSTANCES.put(bluetooth.getMacAddress(), esp);
        }
        return esp;
    }
    public static final ESP32 getOrCreateWithBluetoothMacAddress(final MacAddress bluetoothBSSID) {
        ESP32 esp;
        if (INSTANCES.containsKey(bluetoothBSSID)) {
            esp = INSTANCES.get(bluetoothBSSID);
        } else {
            final WiFi wifi = new WiFi(MacAddress.generate());
            final Bluetooth bluetooth = new Bluetooth(bluetoothBSSID);
            esp = new ESP32(UUID.randomUUID(), wifi, bluetooth);
            INSTANCES.put(bluetoothBSSID, esp);
            INSTANCES.put(wifi.getMacAddress(), esp);
        }
        return esp;
    }
    public static final ESP32 createWithBluetoothAndWiFiMacAddresses(final MacAddress wifiBSSID, final MacAddress bluetoothBSSID) {
        final WiFi wifi = new WiFi(wifiBSSID);
        final Bluetooth bluetooth = new Bluetooth(bluetoothBSSID);
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
