package io.nirahtech.petvet.esp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Objects;

public final class MulticastEmitter {

    private MulticastEmitter() { }

    public static final void broadcast(final InetAddress group, final int port, final String message) throws IOException {
        Objects.requireNonNull(group, "Group is required to send message.");
        Objects.requireNonNull(message, "Message is required to send message.");
        final MulticastSocket multicastSocket = new MulticastSocket();
        final DatagramPacket udpPacket = new DatagramPacket(message.getBytes(), message.length(), group, port);
        multicastSocket.send(udpPacket);
        multicastSocket.close();
    }
}
