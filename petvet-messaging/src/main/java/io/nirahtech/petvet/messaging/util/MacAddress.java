package io.nirahtech.petvet.messaging.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.StringJoiner;

public final class MacAddress implements Serializable, Comparator<MacAddress> {
    private final byte[] address = new byte[6];
    private MacAddress(byte first, byte second, byte third, byte fourth, byte fifth, byte sixth) {
        this.address[0] = first;
        this.address[1] = second;
        this.address[2] = third;
        this.address[3] = fourth;
        this.address[4] = fifth;
        this.address[5] = sixth;
    }

    public final byte[] getAddress() {
        return this.address;
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

    public static final MacAddress of(byte[] address) {
        return new MacAddress(address[0], 
        address[1], 
        address[2], 
        address[3], 
        address[4], 
        address[5]);
    }

    public static final MacAddress of(String address) {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }
    
        String[] bytes = address.split(":", -1);
        if (bytes.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address format: " + address);
        }
    
        byte[] mac = new byte[6];
        for (int i = 0; i < 6; i++) {
            try {
                String segment = bytes[i].replaceAll("[^0-9A-Fa-f]", "").trim();
                int value = Integer.parseInt(segment, 16);
                if (value < 0 || value > 255) {
                    throw new IllegalArgumentException("Invalid value for MAC address segment: " + segment);
                }
                mac[i] = (byte) value;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid value for MAC address segment: " + bytes[i], e);
            }
        }
    
        return of(mac);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(address);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MacAddress other = (MacAddress) obj;
        if (!Arrays.equals(address, other.address))
            return false;
        return true;
    }

    @Override
    public int compare(MacAddress mac1, MacAddress mac2) {
        for (int i = 0; i < address.length; i++) {
            int byteComparison = Byte.compare(mac1.address[i], mac2.address[i]);
            if (byteComparison != 0) {
                return byteComparison;
            }
        }
        return 0;
    }

    public static final MacAddress generate() {
        final byte[] address = new byte[6];
        for (int i = 0; i < address.length; i++) {
            address[i] = (byte) (Math.random() * 256);
        }
        return of(address);
    }
    
    @Override
    public String toString() {
        final StringJoiner joiner = new StringJoiner(":");
        for (byte b : address) {
            final String hex = String.format("%02X", b & 0xFF);
            joiner.add(hex);
        }
        return joiner.toString();
    }
}
