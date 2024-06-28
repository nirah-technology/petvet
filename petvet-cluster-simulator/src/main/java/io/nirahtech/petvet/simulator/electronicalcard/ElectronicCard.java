package io.nirahtech.petvet.simulator.electronicalcard;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;

import io.nirahtech.petvet.messaging.util.MacAddress;

public class ElectronicCard implements MicroController {
    private final Program program;

    private final float width;
    private final float height;

    private float zoom = 2.0F;

    private ElectronicCard(final float width, final float height, final Program program) {
        this.width = width;
        this.height = height;
        this.program = program;
    }

    /**
     * @return the height
     */
    public float getHeight() {
        return height * zoom;
    }

    /**
     * @return the width
     */
    public float getWidth() {
        return width * zoom;
    }

    @Override
    public void run() {
        this.program.run();
    }

    @Override
    public void powerOff() {
        this.program.kill();
    }

    public final PetVetProcess getProcess() {
        return (PetVetProcess) this.program;
    }

    public static final ElectronicCard newInstance(final NetworkInterface networkInterface, final MacAddress mac, final InetAddress ip, final Configuration configuration, Set<MacAddress> neighbors, final float width, final float height, Map<ElectronicCard, Float> neigborsNodeSignals) throws UnknownHostException {
        final Program program = new PetVetSketch(networkInterface, mac, ip, configuration, neighbors, neigborsNodeSignals);
        return new ElectronicCard(width, height, program);
    }

    @Override
    public boolean isRunning() {
        return this.program.isRunning();
    }
}
