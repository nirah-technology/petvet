package io.nirahtech.petvet.esp.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.nirahtech.petvet.esp.scanners.Scanner;
import io.nirahtech.petvet.messaging.brokers.MessagePublisher;
import io.nirahtech.petvet.messaging.messages.ScanReportMessage;
import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;

public final class ScanNowCommand extends AbstractCommand {


    private final InetAddress ip;
    private final MessagePublisher messageSender;
    private final EmitterMode mode;
    private final MacAddress mac;
    private final UUID id;
    private final Scanner scanner;

    ScanNowCommand(final MessagePublisher messageSender, final UUID id, final MacAddress mac, InetAddress ip, final EmitterMode mode, final Scanner scanner) {
        this.id = id;
        this.messageSender = messageSender;
        this.ip = ip;
        this.mac = mac;
        this.mode = mode;
        this.scanner = scanner;
    }

    private void sendScanReport(final Map<MacAddress, Float> detectedDevices) throws IOException {
        final ScanReportMessage message = ScanReportMessage.create(
            id, mac, ip, mode, detectedDevices
        );
        this.messageSender.send(message);
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
        this.scanner.scan().forEach(device -> {
            detectedDevicesWithDBs.put(device.bssid(), device.signalLevel());
        });
        this.sendScanReport(detectedDevicesWithDBs);
    }
    
}
