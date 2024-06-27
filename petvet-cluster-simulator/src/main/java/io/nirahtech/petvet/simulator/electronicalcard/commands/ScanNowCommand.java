package io.nirahtech.petvet.simulator.electronicalcard.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import io.nirahtech.petvet.messaging.brokers.MessagePublisher;
import io.nirahtech.petvet.messaging.messages.MessageType;
import io.nirahtech.petvet.messaging.messages.ScanReportMessage;
import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;
import io.nirahtech.petvet.simulator.electronicalcard.scanners.Scanner;

public final class ScanNowCommand extends AbstractCommand {

    private static final Map<String, Float> NEIGHBORS_DISTANCES = new ConcurrentHashMap<>();

    
    public static final float MINIMUM_SIGNAL_STRENGTH_IN_DBM = -100.0F;
    public static final float MAXIMUM_SIGNAL_STRENGTH_IN_DBM = -30.0F;

    private final InetAddress ip;
    private final MessagePublisher messageSender;
    private final EmitterMode mode;
    private final MacAddress mac;
    private final UUID id;
    private final UUID scanId;
    private final Scanner scanner;
    private final Set<MacAddress> neighborsBSSID;
    private final Map<MacAddress, Float> scanResultCache = new HashMap<>();
    private final Consumer<MessageType> eventListerOnSendedMessage;

    ScanNowCommand(final MessagePublisher messageSender, final UUID scanId, final UUID id, final MacAddress mac, InetAddress ip, final EmitterMode mode, final Scanner scanner, Set<MacAddress> neighborsBSSID, final Consumer<MessageType> eventListerOnSendedMessage) {
        this.neighborsBSSID = neighborsBSSID;
        this.id = id;
        this.messageSender = messageSender;
        this.ip = ip;
        this.mac = mac;
        this.mode = mode;
        this.scanner = scanner;
        this.scanId = scanId;
        this.eventListerOnSendedMessage = eventListerOnSendedMessage;
    }

    private void sendScanReport(final Map<MacAddress, Float> detectedDevices) throws IOException {
        final ScanReportMessage message = ScanReportMessage.create(
            scanId, id, mac, ip, mode, detectedDevices
        );
        this.messageSender.send(message);

        if (Objects.nonNull(this.eventListerOnSendedMessage)) {
            this.eventListerOnSendedMessage.accept(message.getType());
        }
    }
    
    private final Map<MacAddress, Float> stubScan() {
        if (this.scanResultCache.isEmpty()) {
            this.neighborsBSSID.forEach(bssid -> {
                if (!bssid.equals(this.mac)) {
                    final String key1 = String.format("%s-%s", this.mac, bssid);
                    final String key2 = String.format("%s-%s", bssid, this.mac);
                    if (NEIGHBORS_DISTANCES.containsKey(key1)) {
                        this.scanResultCache.put(bssid, NEIGHBORS_DISTANCES.get(key1));
                    } else if (NEIGHBORS_DISTANCES.containsKey(key2)) {
                        this.scanResultCache.put(bssid, NEIGHBORS_DISTANCES.get(key2));
                    } else {
                        // final Random random = new Random();
                        // final float strength = (float) (MAXIMUM_SIGNAL_STRENGTH_IN_DBM + (MINIMUM_SIGNAL_STRENGTH_IN_DBM + MAXIMUM_SIGNAL_STRENGTH_IN_DBM) * random.nextDouble());
                        final float strength = (MINIMUM_SIGNAL_STRENGTH_IN_DBM + (new Random().nextFloat() * (MAXIMUM_SIGNAL_STRENGTH_IN_DBM - MINIMUM_SIGNAL_STRENGTH_IN_DBM)));
                        NEIGHBORS_DISTANCES.put(key1, strength);
                        NEIGHBORS_DISTANCES.put(key2, strength);
                        this.scanResultCache.put(bssid, strength);
                    }
                }
            });
        }
        return this.scanResultCache;
    }
    
    /**
     * <p>Execute a scan to retrieve all detactable devices.</p>
     * <p>All detected devices are stored in a map, as key.</p>
     * <p>The detected power signal in dBs of each detected devices is store in the map, as value</p>
     * <p>When the scan is finished, a scan report is broadcasted.</p>
     */
    @Override
    public void execute() throws IOException {
        super.execute();
        final Map<MacAddress, Float> detectedDevicesWithDBs = new HashMap<>();
        try {
            this.scanner.scan().forEach(device -> {
                detectedDevicesWithDBs.put(device.bssid(), device.signalLevel());
            });
        } catch (Exception exception) {
            
        } finally {
            detectedDevicesWithDBs.putAll(this.stubScan());
        }
        this.sendScanReport(detectedDevicesWithDBs);
    }
    
}
