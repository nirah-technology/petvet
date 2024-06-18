package io.nirahtech.petvet.esp;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class IPV4Address {
    private final byte[] address = new byte[4];

    private IPV4Address(byte first, byte second, byte third, byte fourth) {
        this.address[0] = first;
        this.address[1] = second;
        this.address[2] = third;
        this.address[3] = fourth;
    }

    public final byte[] getAddress() {
        return this.address;
    }

    public static final IPV4Address of(byte[] address) {
        return new IPV4Address(address[0],
                address[1],
                address[2],
                address[3]);
    }

    public static final IPV4Address of(String address) {
        if (address == null || address.isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }
    
        String[] bytes = address.split("\\.", -1);
        if (bytes.length != 4) {
            throw new IllegalArgumentException("Invalid IP address format: " + address);
        }
    
        byte[] ip = new byte[4];
        for (int i = 0; i < 4; i++) {
            try {
                String segment = bytes[i].replaceAll("[^0-9]", "").trim();
                int value = Integer.parseInt(segment, 10);
                if (value < 0 || value > 255) {
                    throw new IllegalArgumentException("Invalid value for IP address segment: " + segment);
                }
                ip[i] = (byte) value;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid value for IP address segment: " + bytes[i], e);
            }
        }
    
        return of(ip);
    }
    

    public InetAddress toInetAddress() throws UnknownHostException {
        return InetAddress.getByAddress(this.address);
    }
}
