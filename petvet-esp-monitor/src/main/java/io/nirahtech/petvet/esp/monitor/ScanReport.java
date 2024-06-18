package io.nirahtech.petvet.esp.monitor;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.nirahtech.petvet.esp.monitor.messages.ScanReportMessage;

public final class ScanReport {
    private final UUID espId;
    private final boolean isOrchestrator;
    private final LocalDateTime sendedAt;
    private final InetAddress espIp;
    private final Set<Device> detectedDevices;

    public ScanReport(UUID espId, boolean isOrchestrator, LocalDateTime sendedAt, InetAddress espIp,
    Set<Device> detectedDevices) {
        this.espId = espId;
        this.isOrchestrator = isOrchestrator;
        this.sendedAt = sendedAt;
        this.espIp = espIp;
        this.detectedDevices = detectedDevices;
    }
    
    public UUID getEspId() {
        return espId;
    }

    public boolean isOrchestrator() {
        return isOrchestrator;
    }

    public LocalDateTime getSendedAt() {
        return sendedAt;
    }

    public InetAddress getEspIp() {
        return espIp;
    }

    public Set<Device> getDetectedDevices() {
        return detectedDevices;
    }

    public static final ScanReport map(final ScanReportMessage message) {
        return new ScanReport(message.getId(), message.isOrchestrator(), message.sentAt(), message.getEmitter(), new HashSet<>(message.getDetectedDevices()));
    }
}
