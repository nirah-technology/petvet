package io.nirahtech.petvet.simulator.electronicalcard;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

import io.nirahtech.petvet.messaging.util.MacAddress;

public final class ElectronicalCard implements MicroController {
    private final Program program;
    private ElectronicalCard(final Program program) {
        this.program = program;
    }

    @Override
    public void run() {
        this.program.run();
    }

    public static final ElectronicalCard newInstance(final NetworkInterface networkInterface, final MacAddress mac, final InetAddress ip, final Configuration configuration) throws UnknownHostException {
        final Program program = new Sketch(networkInterface, mac, ip, configuration);
        return new ElectronicalCard(program);
    }
}
