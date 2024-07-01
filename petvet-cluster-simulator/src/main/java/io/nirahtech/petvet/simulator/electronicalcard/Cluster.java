package io.nirahtech.petvet.simulator.electronicalcard;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Stream;

import io.nirahtech.petvet.messaging.messages.MessageType;
import io.nirahtech.petvet.messaging.util.MacAddress;

/**
 * Cluster
 */
public final class Cluster {

    private ExecutorService executorService;
    private final Set<MicroController> nodes;
    private final Network network;
    private final List<Inet4Address> availableIP;
    private final List<MacAddress> availableMAC;
    private final Set<MacAddress> neighborsBSSID;
    private final Configuration configuration;
    private final Map<MessageType, Consumer<UUID>> eventListerOnSendedMessages = new HashMap<>();
    private Map<ElectronicCard, Map<ElectronicCard, Float>> neigborsNodeSignals;

    private Cluster(final Network network, List<Inet4Address> availableIP, List<MacAddress> availableMAC, final Set<MicroController> nodes, Set<MacAddress> neighborsBSSID, Map<ElectronicCard, Map<ElectronicCard, Float>> neigborsNodeSignals, Configuration configuration) {
        this.nodes = nodes;
        this.network = network;
        this.availableIP = availableIP;
        this.availableMAC = availableMAC;
        this.neighborsBSSID = neighborsBSSID;
        this.configuration = configuration;
        this.neigborsNodeSignals = neigborsNodeSignals;
    }

    public void setNeigborsNodeSignals(Map<ElectronicCard, Map<ElectronicCard, Float>> neigborsNodeSignals) {
        this.neigborsNodeSignals = neigborsNodeSignals;
        this.nodes.forEach(node -> {
            final PetVetSketch sketch = (PetVetSketch)((ElectronicCard)node).getProcess();
            final Map<ElectronicCard, Float> signalsReceivedByNode = neigborsNodeSignals.get(node);
            sketch.setNeigborsNodeSignals(signalsReceivedByNode);
        });
    }

    public Stream<MicroController> nodes() {
        return this.nodes.stream();
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
        final Map<ElectronicCard, Map<ElectronicCard, Float>> neigborsNodeSignals = new HashMap<>();

        final Set<MacAddress> neighborsBSSID = new HashSet<>();
        for (int i = 0; i < configuration.clusterSize(); i++) {
            final Inet4Address ip = availableIP.get(i);
            availableIP.remove(ip);
            final MacAddress mac = availableMAC.get(i);
            availableMAC.remove(mac);
            neighborsBSSID.add(mac);
            final Map<ElectronicCard, Float> detectedNodeSignals = new HashMap<>();
            final ElectronicCard node = ElectronicCard.newInstance(network.getNetworkInterface(), mac, ip, configuration, neighborsBSSID, 5F, 3F, detectedNodeSignals);
            neigborsNodeSignals.put(node, detectedNodeSignals);
            nodes.add(node);
        }
        return new Cluster(network, availableIP, availableMAC, nodes, neighborsBSSID, neigborsNodeSignals, configuration);
    }

    public ElectronicCard generateNode() throws UnknownHostException {

        final Inet4Address ip = availableIP.get(0);
        availableIP.remove(ip);
        final MacAddress mac = availableMAC.get(0);
        availableMAC.remove(mac);
        neighborsBSSID.add(mac);
        final Map<ElectronicCard, Float> detectedNodeSignals = new HashMap<>();
        final ElectronicCard node = ElectronicCard.newInstance(network.getNetworkInterface(), mac, ip, configuration, neighborsBSSID, 5F, 3F, detectedNodeSignals);
        this.nodes.add(node);
        this.neigborsNodeSignals.put(node, detectedNodeSignals);
        this.refreshEventistenersForNodes();
        return node;
    }


    public void turnOn() {
        this.executorService = Executors.newFixedThreadPool(this.nodes.size());
        for (MicroController node : nodes) {
            executorService.submit(node);
        }
        executorService.shutdown();
    }

    public void turnOff() {
        for (MicroController node : nodes) {
            node.powerOff();
        }
        executorService.shutdownNow();
    }

    public void deleteNode(ElectronicCard node) {
        this.nodes.remove(node);
    }

    private final void refreshEventistenersForNodes() {
        this.nodes.forEach(node -> {
            this.eventListerOnSendedMessages.entrySet().forEach(eventListener -> {
                ((ElectronicCard) node).getProcess().addEventListenerOn(eventListener.getKey(), eventListener.getValue());
            });
        });
    }

    public final void addEventListenerOn(MessageType messageType, Consumer<UUID> callback) {
        this.eventListerOnSendedMessages.put(messageType, callback);
        this.refreshEventistenersForNodes();
    }
}