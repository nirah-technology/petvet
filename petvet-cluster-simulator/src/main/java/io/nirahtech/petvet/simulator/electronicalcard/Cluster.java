package io.nirahtech.petvet.simulator.electronicalcard;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public static final Cluster create(Configuration configuration) throws IOException {
        final Set<MicroController> nodes = new HashSet<>();

        final Optional<Network> networkFound = Network.retrieveNetworkUsingFilter(configuration.ipFilter().getAddress()[0], configuration.ipFilter().getAddress()[1], configuration.ipFilter().getAddress()[2]);
        if (!networkFound.isPresent()) {
            throw new IOException("Unable to found the right network.");
        }
        final Network network = networkFound.get();

        final List<Inet4Address> availableIP = new ArrayList<>(network.getAllAvailableIpAddresses().toList());
        availableIP.remove(0);
        availableIP.remove(availableIP.size()-1);
        availableIP.remove(network.getIp());
        final List<MacAddress> availableMAC = new ArrayList<>(network.getAllAvailableMacAddresses().toList());

        final Set<MacAddress> neighborsBSSID = new HashSet<>();
        for (int i = 0; i < configuration.clusterSize(); i++) {
            final Inet4Address ip = availableIP.get(i);
            final MacAddress mac = availableMAC.get(i);
            neighborsBSSID.add(mac);
            final MicroController node = ElectronicalCard.newInstance(network.getNetworkInterface(), mac, ip, configuration, neighborsBSSID);
            nodes.add(node);
        }
        return new Cluster(nodes);
    }

    @Override
    public void run() {
        for (MicroController node : nodes) {
            executorService.submit(node);
        }
        executorService.shutdown();
    }
}