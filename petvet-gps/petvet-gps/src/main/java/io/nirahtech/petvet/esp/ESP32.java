package io.nirahtech.petvet.esp;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class ESP32 implements Runnable {
    private final Program program;
    private ESP32(final Program program) {
        this.program = program;
    }

    @Override
    public void run() {
        this.program.run();
    }

    public static final ESP32 newInstance() throws UnknownHostException {
        final InetAddress group = InetAddress.getByName("224.6.6.6");
        final int port = 44666;
        final Program program = new Sketch(group, port);
        return new ESP32(program);
    }
}
