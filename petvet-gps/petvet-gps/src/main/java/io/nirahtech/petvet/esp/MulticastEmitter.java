package io.nirahtech.petvet.esp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Objects;

public final class MulticastEmitter {
    public static final String WIFI_NETWORK_INTERFACE_NAME = "wlo1";

    private MulticastEmitter() { }

    public static final void broadcast(final InetAddress multicastGroupAddress, final int port, final String message) throws IOException {
        Objects.requireNonNull(multicastGroupAddress, "Group is required to send message.");
        Objects.requireNonNull(message, "Message is required to send message.");
        InetSocketAddress group = new InetSocketAddress(multicastGroupAddress, port);
        NetworkInterface networkInterface = NetworkInterface.getByName(WIFI_NETWORK_INTERFACE_NAME);
        try (MulticastSocket multicastSocket = new MulticastSocket(group.getPort())) {
            multicastSocket.joinGroup(new InetSocketAddress(multicastGroupAddress, 0), networkInterface);
            final DatagramPacket udpPacket = new DatagramPacket(message.getBytes(), message.length(), group);
            multicastSocket.send(udpPacket);
        }
    }
}
