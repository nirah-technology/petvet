package io.nirahtech.petvet.esp.messages;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import io.nirahtech.petvet.esp.IPV4Address;
import io.nirahtech.petvet.esp.scanners.Device;

public class ScanReportMessage extends AbstractMessage {
    private static final String DETECTED_DEVICES_KEY = "detectedDevices";

    private final Collection<Device> detectedDevices;

    private ScanReportMessage(UUID id, InetAddress emitter, boolean isOrchestrator,
            LocalDateTime sentAt, Collection<Device> detectedDevices) {
        super(id, MessageType.SCAN_NOW, emitter, isOrchestrator, sentAt);
        this.detectedDevices = detectedDevices;
    }

    public static ScanReportMessage create(UUID id, InetAddress emitter, boolean isOrchestrator,
            Collection<Device> detectedDevices) {
        return new ScanReportMessage(id, emitter, isOrchestrator,
                LocalDateTime.now(), detectedDevices);
    }

    public static Optional<ScanReportMessage> parse(final String messageAsString) {
        Optional<ScanReportMessage> scanReportMessage = Optional.empty();
        if (messageAsString.contains(":")) {
            final String[] messageParts = messageAsString.split(":");
            final MessageType type = MessageType.valueOf(messageParts[0]);
            if (type.equals(MessageType.SCAN_REPORT)) {
                final Map<String, Object> properties = Message.fromStringToMap(messageParts[1]);
                try {
                    ScanReportMessage message = new ScanReportMessage(
                            UUID.fromString(properties.get(Message.ID_PROPERTY_NAME).toString().strip()),
                            IPV4Address
                                    .of(properties.get(Message.EMITTER_PROPERTY_NAME).toString().strip().substring(1))
                                    .toInetAddress(),
                            Boolean.parseBoolean(
                                    properties.get(Message.IS_ORCHESTRATOR_PROPERTY_NAME).toString().strip()),
                            LocalDateTime.parse(properties.get(Message.SENDED_AT_PROPERTY_NAME).toString().strip(),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")),
                            Set.of());
                    scanReportMessage = Optional.of(message);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        return scanReportMessage;
    }

    @Override
    public String toString() {
        final StringBuilder reportBuilder = new StringBuilder(MessageType.SCAN_REPORT.name())
                .append(":");
        this.detectedDevices.forEach((device) -> reportBuilder.append(device.bssid().toString()).append("=")
                .append(device.signalLevel()).append(";"));
        StringBuilder messageBuilder = new StringBuilder()
                .append(this.getType().name())
                .append(":")
                .append(String.format("%s=%s,", Message.TYPE_PROPERTY_NAME, this.getType().name()))
                .append(String.format("%s=%s,", Message.IS_ORCHESTRATOR_PROPERTY_NAME, this.isOrchestrator()))
                .append(String.format("%s=%s,", Message.ID_PROPERTY_NAME, this.getId().toString()))
                .append(String.format("%s=%s,", Message.SENDED_AT_PROPERTY_NAME, this.sentAt().toString()))
                .append(String.format("%s=%s,", Message.EMITTER_PROPERTY_NAME, this.getEmitter().toString()))
                .append(String.format("%s=%s", DETECTED_DEVICES_KEY, reportBuilder));
        return messageBuilder.toString();
    }

}
