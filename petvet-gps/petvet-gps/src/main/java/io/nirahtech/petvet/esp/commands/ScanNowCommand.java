package io.nirahtech.petvet.esp.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.nirahtech.petvet.esp.MessageSender;
import io.nirahtech.petvet.esp.MessageTypeOld;
import io.nirahtech.petvet.esp.Mode;
import io.nirahtech.petvet.esp.messages.ScanNowMessage;

public final class ScanNowCommand extends AbstractCommand {


    private final InetAddress ipv4Addess;
    private final MessageSender messageSender;
    private final Mode mode;
    private final UUID id;

    ScanNowCommand(final MessageSender messageSender, final UUID id, InetAddress emitter, final Mode mode) {
        this.id = id;
        this.messageSender = messageSender;
        this.ipv4Addess = emitter;
        this.mode = mode;
    }

    private void sendScanReport(final Map<String, Double> detectedDevicesWithDBs) throws IOException {
        final StringBuilder reportBuilder = new StringBuilder(MessageTypeOld.SCAN_REPORT.name())
            .append(":");
        detectedDevicesWithDBs.forEach((device, db) -> reportBuilder.append(device).append("=").append(db).append(";"));
        final String report = reportBuilder.toString();
        final ScanNowMessage message = ScanNowMessage.create(this.id, this.ipv4Addess, this.mode.equals(Mode.ORCHESTRATOR_NODE));
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
        System.out.println("Scan using Bluetooth...");
        final Map<String, Double> detectedDevicesWithDBs = new HashMap<>();
    
        // On imagine qu'on a scanner avec le bluetooth ici !!
    
        this.sendScanReport(detectedDevicesWithDBs);
    }
    
}
