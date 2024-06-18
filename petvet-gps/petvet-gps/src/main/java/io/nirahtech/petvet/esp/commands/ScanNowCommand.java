package io.nirahtech.petvet.esp.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.nirahtech.petvet.esp.Mode;
import io.nirahtech.petvet.esp.brokers.MessagePublisher;
import io.nirahtech.petvet.esp.messages.ScanReportMessage;
import io.nirahtech.petvet.esp.scanners.Device;
import io.nirahtech.petvet.esp.scanners.Scanner;

public final class ScanNowCommand extends AbstractCommand {


    private final InetAddress ipv4Addess;
    private final MessagePublisher messageSender;
    private final Mode mode;
    private final UUID id;
    private final Scanner scanner;

    ScanNowCommand(final MessagePublisher messageSender, final UUID id, InetAddress emitter, final Mode mode, final Scanner scanner) {
        this.id = id;
        this.messageSender = messageSender;
        this.ipv4Addess = emitter;
        this.mode = mode;
        this.scanner = scanner;
    }

    private void sendScanReport(final Collection<Device> detectedDevices) throws IOException {
        final ScanReportMessage message = ScanReportMessage.create(this.id, this.ipv4Addess, this.mode.equals(Mode.ORCHESTRATOR_NODE), detectedDevices);
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
        final Set<Device> detectedDevicesWithDBs = new HashSet<>();    
        this.scanner.scan().forEach(device -> {
            detectedDevicesWithDBs.add(device);
        });
        this.sendScanReport(detectedDevicesWithDBs);
    }
    
}
