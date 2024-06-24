package io.nirahtech.petvet.geopulsetracker.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class WirelessNetworkInterfaceCard implements NetworkInterfaceCard, Wireless {
    private final MacAddress macAddress;
    private boolean isRunning = false;
    private final Map<MacAddress, Signal> lastScanResult;
    protected WirelessNetworkInterfaceCard(final MacAddress macAddress) {
        this.macAddress = macAddress;
        this.lastScanResult = new HashMap<>();
    }

    @Override
    public void on() {
        this.isRunning = true;
    }

    @Override
    public Map<MacAddress, Signal> scan() {
        Map<MacAddress, Signal> scanResult = new HashMap<>();
        if (this.isRunning) {
            
        }
        this.lastScanResult.clear();
        this.lastScanResult.putAll(scanResult);
        return scanResult;
    }

    @Override
    public void off() {
        this.isRunning = false;
    }

    @Override
    public MacAddress getMacAddress() {
        return this.macAddress;
    }

    @Override
    public Optional<Signal> emitSignal(MacAddress macAddress) {
        Signal signal = null;
        if (this.isRunning) {
            if (this.lastScanResult.containsKey(macAddress)) {
                signal = this.lastScanResult.get(macAddress);
            } else {
                signal = new Signal();
                this.lastScanResult.put(macAddress, signal);
            }

            final float chance = 0.02f;
            final double newValue = 0.1D;
            if (Math.random() < chance) {
                signal.increaseStrength(newValue);
            } else if (Math.random() < chance) {
                signal.decreaseStrength(newValue);
            }
        }
        return Optional.ofNullable(signal);
    }

}
