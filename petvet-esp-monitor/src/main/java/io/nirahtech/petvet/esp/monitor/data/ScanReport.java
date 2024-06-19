package io.nirahtech.petvet.esp.monitor.data;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import io.nirahtech.petvet.messaging.messages.ScanReportMessage;
import io.nirahtech.petvet.messaging.util.MacAddress;

public final class ScanReport {
    private final UUID espId;
    private final boolean isOrchestrator;
    private final LocalDateTime sendedAt;
    private final InetAddress espIp;
    private final Map<MacAddress, Float> detectedDevices;

    public ScanReport(UUID espId, boolean isOrchestrator, LocalDateTime sendedAt, InetAddress espIp,
    Map<MacAddress, Float> detectedDevices) {
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

    public Map<MacAddress, Float> getDetectedDevices() {
        return detectedDevices;
    }

    public static final ScanReport map(final ScanReportMessage message) {
        return new ScanReport(message.getEmitterID(), message.getEmitterMode().isOrchestratorMode(), message.getSentedAt(), message.getEmitterIP(), message.getScanReportResults());
    }
}
