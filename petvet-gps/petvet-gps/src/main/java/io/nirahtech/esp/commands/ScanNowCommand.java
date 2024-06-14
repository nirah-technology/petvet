package io.nirahtech.esp.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import io.nirahtech.esp.MessageType;
import io.nirahtech.esp.MulticastEmitter;

public final class ScanNowCommand extends AbstractCommand {

    ScanNowCommand(final InetAddress group, final int port) {
        super(group, port);
    }

    private void sendScanReport(final Map<String, Double> detectedDevicesWithDBs) throws IOException {
        final StringBuilder reportBuilder = new StringBuilder(MessageType.SCAN_REPORT.name())
            .append(":");
        detectedDevicesWithDBs.forEach((device, db) -> reportBuilder.append(device).append("=").append(db).append(";"));
        final String report = reportBuilder.toString();
        MulticastEmitter.broadcast(super.group, super.port, report);
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
        System.out.println("Scan using Bluetooth...");
        final Map<String, Double> detectedDevicesWithDBs = new HashMap<>();
    
        // On imagine qu'on a scanner avec le bluetooth ici !!
    
        this.sendScanReport(detectedDevicesWithDBs);
    }
    
}
