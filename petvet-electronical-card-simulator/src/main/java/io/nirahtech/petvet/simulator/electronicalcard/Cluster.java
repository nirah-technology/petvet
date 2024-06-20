package io.nirahtech.petvet.simulator.electronicalcard;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;

/**
 * Cluster
 */
public final class Cluster implements Runnable {

    private final Set<MicroController> nodes;
    private final ExecutorService executorService;

    private Cluster(final Set<MicroController> nodes) {
        this.nodes = nodes;
        this.executorService = Executors.newFixedThreadPool(this.nodes.size());
    }

    public static final Cluster create(final int totalNodes, final InetAddress ipFilter, final InetAddress multicastGroupAddress, final int multicastGroupPort, EmitterMode mode, Duration scanInterval, Duration orchestratorInterval, Duration heartbeatInterval) throws IOException {
        final Set<MicroController> nodes = new HashSet<>();

        final Optional<Network> networkFound = Network.retrieveNetworkUsingFilter(ipFilter.getAddress()[0], ipFilter.getAddress()[1], ipFilter.getAddress()[2]);
        if (!networkFound.isPresent()) {
            throw new IOException("Unable to found the right network.");
        }
        final Network network = networkFound.get();

        final List<Inet4Address> availableIP = new ArrayList<>(network.getAllAvailableIpAddresses().toList());
        availableIP.remove(0);
        availableIP.remove(availableIP.size()-1);
        availableIP.remove(network.getIp());
        final List<MacAddress> availableMAC = new ArrayList<>(network.getAllAvailableMacAddresses().toList());

        for (int i = 0; i < totalNodes; i++) {
            Inet4Address ip = availableIP.get(i);
            MacAddress mac = availableMAC.get(i);
            MicroController node = ElectronicalCard.newInstance(network.getNetworkInterface(), mac, ip, multicastGroupAddress, multicastGroupPort, mode, scanInterval, orchestratorInterval, heartbeatInterval);
            nodes.add(node);
        }
        final Cluster cluster = new Cluster(nodes);
        return cluster;
    }

    @Override
    public void run() {
        for (MicroController node : nodes) {
            executorService.submit(node);
        }
        executorService.shutdown();
    }
}