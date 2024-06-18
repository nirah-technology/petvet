package io.nirahtech.petvet.esp.monitor.brokers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;

public interface Connectable {
    void connect(final NetworkInterface networkInterface, final InetAddress groupAddress, final int port)  throws IOException;
    boolean isConnected();
    void disconnect();

    InetAddress getInetAddress();
    int getPort();
}
