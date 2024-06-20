package io.nirahtech.petvet.simulator.electronicalcard;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import io.nirahtech.petvet.messaging.util.MacAddress;

public class Network {
    private final NetworkInterface networkInterface;
    private final MacAddress mac;
    private final InetAddress ip;
    private final short prefixLenght;

    private Network(final NetworkInterface networkInterface, final MacAddress mac, InetAddress ip, final short prefixLenght) {
        this.networkInterface = networkInterface;
        this.ip = ip;
        this.prefixLenght = prefixLenght;
        this.mac = mac;
    }
    

    public NetworkInterface getNetworkInterface() {
        return networkInterface;
    }


    public MacAddress getMac() {
        return mac;
    }


    public InetAddress getIp() {
        return ip;
    }


    public short getPrefixLenght() {
        return prefixLenght;
    }


    public Stream<Inet4Address> getAllAvailableIpAddresses() {
        int totalHosts = (int) Math.pow(2, (32 - prefixLenght)) - 2;  // Number of available host addresses
        byte[] ipBytes = ip.getAddress();
        int baseAddress = ((ipBytes[0] & 0xFF) << 24) | ((ipBytes[1] & 0xFF) << 16) | ((ipBytes[2] & 0xFF) << 8) | (ipBytes[3] & 0xFF);
        int networkAddress = baseAddress & (0xFFFFFFFF << (32 - prefixLenght));
        
        List<Inet4Address> addresses = new ArrayList<>();
        
        for (int i = 1; i <= totalHosts; i++) {
            int hostAddress = networkAddress + i;
            byte[] addressBytes = new byte[] {
                (byte) ((hostAddress >> 24) & 0xFF),
                (byte) ((hostAddress >> 16) & 0xFF),
                (byte) ((hostAddress >> 8) & 0xFF),
                (byte) (hostAddress & 0xFF)
            };
            try {
                Inet4Address inetAddress = (Inet4Address) InetAddress.getByAddress(addressBytes);
                addresses.add(inetAddress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return addresses.stream();
    }

    public Stream<MacAddress> getAllAvailableMacAddresses() {
        byte[] baseMac = mac.getAddress();
        List<MacAddress> macAddresses = new ArrayList<>();
        
        for (int i = 0; i < 256; i++) {
            byte[] newMac = baseMac.clone();
            newMac[5] = (byte) i;
            macAddresses.add(MacAddress.of(newMac));
        }
        
        return macAddresses.stream();
    }

    public static final Optional<Network> retrieveNetworkUsingFilter(byte firstByteOfIp, byte secondByteOfIp,
            byte thirdByteOfIp) {
        Optional<Network> networkFound = Optional.empty();
        try {
            NICS_LOOP: for (NetworkInterface networkInterface : NetworkInterface.networkInterfaces().toList()) {
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress inetAddress = interfaceAddress.getAddress();
                    byte[] ip = inetAddress.getAddress();
                    if (ip.length == 4) {
                        if ((ip[0] == firstByteOfIp) && (ip[1] == secondByteOfIp) && (ip[2] == thirdByteOfIp)) {
                            final Network network = new Network(networkInterface, MacAddress.of(networkInterface.getHardwareAddress()), inetAddress, interfaceAddress.getNetworkPrefixLength());
                            networkFound = Optional.of(network);
                            break NICS_LOOP;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            
        }
        return networkFound;
    }
}
