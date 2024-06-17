package io.nirahtech.petvet.esp.messages;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.nirahtech.petvet.esp.MacAddress;

public class ScanReportMessage extends AbstractMessage {

    private final Map<MacAddress, Float> detectedDevicesByMacWithPowerSignal = new HashMap<>();



    private ScanReportMessage(UUID id, InetAddress emitter, boolean isOrchestrator,
            LocalDateTime sentAt) {
        super(id, MessageType.SCAN_NOW, emitter, isOrchestrator, sentAt);
    }

    public static ScanReportMessage create(UUID id, InetAddress emitter, boolean isOrchestrator) {
        return new ScanReportMessage(id, emitter, isOrchestrator,
                LocalDateTime.now());
    }

    public static Optional<ScanReportMessage> parse(final String message) {
        return Optional.empty();
    }

    
}
