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
}
