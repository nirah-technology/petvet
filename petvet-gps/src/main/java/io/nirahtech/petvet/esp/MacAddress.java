package io.nirahtech.petvet.esp;

public final class MacAddress {
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
}
