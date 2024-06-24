package io.nirahtech.petvet.geopulsetracker.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class MacAddress implements Serializable {
    private static final Set<MacAddress> KNOWN_ADDRESSES = new HashSet<>();
    private final byte[] address;

    private MacAddress(byte[] address) {
        if (address == null || address.length != 6) {
            throw new IllegalArgumentException("MAC address must be 6 bytes long");
        }
        this.address = Arrays.copyOf(address, address.length); // Ensure immutability
    }

    public byte[] getAddress() {
        return Arrays.copyOf(address, address.length); // Return a copy for immutability
    }

    public byte getFirstByte() {
        return address[0];
    }

    public byte getSecondByte() {
        return address[1];
    }

    public byte getThirdByte() {
        return address[2];
    }

    public byte getFourthByte() {
        return address[3];
    }

    public byte getFifthByte() {
        return address[4];
    }

    public byte getSixthByte() {
        return address[5];
    }

    @Override
    public String toString() {
        return IntStream.range(0, address.length)
                .mapToObj(i -> String.format("%02X", address[i]))
                .collect(Collectors.joining(":"));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        MacAddress that = (MacAddress) obj;
        return Arrays.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(address);
    }

    public static final MacAddress generate() {
        final byte[] address = new byte[6];
        for (int i = 0; i < address.length; i++) {
            address[i] = (byte) (Math.random() * 256);
        }
        return of(address);
    }

    public static final MacAddress of(byte[] address) {
        final MacAddress macAddress = new MacAddress(address);
        return KNOWN_ADDRESSES.stream()
                .filter(mac -> mac.equals(macAddress))
                .findFirst()
                .orElseGet(() -> {
                    KNOWN_ADDRESSES.add(macAddress);
                    return macAddress;
                });
    }

}
