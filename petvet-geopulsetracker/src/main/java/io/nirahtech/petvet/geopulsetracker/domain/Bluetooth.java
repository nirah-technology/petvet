package io.nirahtech.petvet.geopulsetracker.domain;

import io.nirahtech.petvet.messaging.util.MacAddress;

public final class Bluetooth extends WirelessNetworkInterfaceCard {

    public Bluetooth(MacAddress macAddress) {
        super(macAddress);
    }
    
}
