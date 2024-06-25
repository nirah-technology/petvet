package io.nirahtech.petvet.simulator.electronicalcard.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import io.nirahtech.petvet.messaging.brokers.MessagePublisher;
import io.nirahtech.petvet.messaging.messages.ScanReportMessage;
import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;
import io.nirahtech.petvet.simulator.electronicalcard.scanners.Scanner;

public final class ScanNowCommand extends AbstractCommand {

    
    public static final double MINIMUM_SIGNAL_STRENGTH_IN_DBM = -100.0D;
    public static final double MAXIMUM_SIGNAL_STRENGTH_IN_DBM = -30.0D;

    private final InetAddress ip;
    private final MessagePublisher messageSender;
    private final EmitterMode mode;
    private final MacAddress mac;
    private final UUID id;
    private final UUID scanId;
    private final Scanner scanner;
    private final Set<MacAddress> neighborsBSSID;
    private final Map<MacAddress, Float> scanResultCache = new HashMap<>();

    ScanNowCommand(final MessagePublisher messageSender, final UUID scanId, final UUID id, final MacAddress mac, InetAddress ip, final EmitterMode mode, final Scanner scanner, Set<MacAddress> neighborsBSSID) {
        this.neighborsBSSID = neighborsBSSID;
        this.id = id;
        this.messageSender = messageSender;
        this.ip = ip;
        this.mac = mac;
        this.mode = mode;
        this.scanner = scanner;
        this.scanId = scanId;
    }

    private void sendScanReport(final Map<MacAddress, Float> detectedDevices) throws IOException {
        final ScanReportMessage message = ScanReportMessage.create(
            scanId, id, mac, ip, mode, detectedDevices
        );
        this.messageSender.send(message);
    }
    
    private final Map<MacAddress, Float> stubScan() {
        if (this.scanResultCache.isEmpty()) {
            final Random random = new Random();
            this.neighborsBSSID.forEach(bssid -> {
                if (!bssid.equals(this.mac)) {
                    final double strength = MAXIMUM_SIGNAL_STRENGTH_IN_DBM + (MINIMUM_SIGNAL_STRENGTH_IN_DBM + MAXIMUM_SIGNAL_STRENGTH_IN_DBM) * random.nextDouble();
                    this.scanResultCache.put(bssid, (float)strength);
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
            detectedDevicesWithDBs.putAll(this.stubScan());
        }
        this.sendScanReport(detectedDevicesWithDBs);
    }
    
}
