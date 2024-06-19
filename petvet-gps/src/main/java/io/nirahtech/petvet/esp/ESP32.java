package io.nirahtech.petvet.esp;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

import io.nirahtech.petvet.messaging.util.MacAddress;

public final class ESP32 implements Runnable {
    private final Program program;
    private ESP32(final Program program) {
        this.program = program;
    }

    @Override
    public void run() {
        this.program.run();
    }

    public static final ESP32 newInstance(final NetworkInterface networkInterface, final MacAddress mac, final InetAddress ip, final InetAddress group, final int port) throws UnknownHostException {
        final Program program = new Sketch(networkInterface, mac, ip, group, port);
        return new ESP32(program);
    }
}
