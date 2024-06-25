package io.nirahtech.petvet.messaging.messages;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;

public class ScanReportMessage extends AbstractMessage {
    private static final String SCAN_ID_KEY = "scanId";
    private static final String DETECTED_DEVICES_KEY = "report";

    private final Map<MacAddress, Float> scanReportResults;
    private final UUID scanId;

    private ScanReportMessage(
            final UUID scanId,
            final UUID emitterId,
            final MacAddress emitterMAC,
            final InetAddress emitterIP,
            final EmitterMode emitterMode,
            final LocalDateTime sentAt,
            final Map<MacAddress, Float> scanReportResults) {
        super(emitterId, emitterMAC, emitterIP, emitterMode, MessageType.SCAN_REPORT, sentAt);
        this.scanId = scanId;
        this.scanReportResults = scanReportResults;
    }

    public final Map<MacAddress, Float> getScanReportResults() {
        return Collections.unmodifiableMap(this.scanReportResults);
    }

    public UUID getScanId() {
        return scanId;
    }

    public static ScanReportMessage create(final UUID scanId, final UUID emitterId, final MacAddress emitterMAC,
            final InetAddress emitterIP, final EmitterMode emitterMode, final Map<MacAddress, Float> scanReportResults) {
        return new ScanReportMessage(scanId, emitterId, emitterMAC, emitterIP, emitterMode, LocalDateTime.now(),
                scanReportResults);
    }

    public static Optional<ScanReportMessage> parse(final String messageAsString) {
        Optional<ScanReportMessage> scanReportMessage = Optional.empty();
        if (messageAsString.contains(":")) {
            final String[] messageParts = messageAsString.split(":", 2);
            final MessageType type = MessageType.valueOf(messageParts[0]);
            if (type.equals(MessageType.SCAN_REPORT)) {
                final Map<String, Object> properties = Message.fromStringToMap(messageParts[1]);
                try {
                    final UUID scan = UUID.fromString(sanitize(properties.get(SCAN_ID_KEY).toString()).strip());
                    final UUID id = UUID.fromString(sanitize(properties.get(Message.EMITTER_ID_PROPERTY_NAME).toString()).strip());
                    final InetAddress ip = InetAddress.getByName(sanitize(properties.get(Message.EMITTER_IP_PROPERTY_NAME).toString()).strip());
                    final MacAddress mac = MacAddress.of(sanitize(properties.get(Message.EMITTER_MAC_PROPERTY_NAME).toString()).strip());
                    final EmitterMode mode = EmitterMode.valueOf(sanitize(properties.get(Message.EMITTER_MODE_PROPERTY_NAME).toString()).strip());
                    final LocalDateTime sendedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(sanitize(properties.get(Message.SENDED_AT_PROPERTY_NAME).toString()).strip())), ZoneOffset.UTC);
                    final String rawReportData = sanitize(properties.get(DETECTED_DEVICES_KEY).toString()).strip();
                    if (rawReportData.contains("|")) {
                        Map<MacAddress, Float> reports = new HashMap<>();
                        final String[] rawReports = rawReportData.split("\\|");
                        for (String rawReport : rawReports) {
                            if (rawReport.contains(">")) {
                                String[] reportDetail = rawReport.split(">");
                                reports.put(MacAddress.of(reportDetail[0]), Float.parseFloat(reportDetail[1]));
                            }
                        }
                        final ScanReportMessage message = new ScanReportMessage(scan, id, mac, ip, mode, sendedAt, reports);
                        scanReportMessage = Optional.of(message);
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        return scanReportMessage;
    }

    @Override
    public String toString() {
        final String report = this.scanReportResults.entrySet()
                .stream()
                .map(entry -> entry.getKey().toString() + ">" + entry.getValue())
                .collect(Collectors.joining("|"));


        StringBuilder messageBuilder = new StringBuilder()
                .append(super.toString())
                .append(String.format(",%s=%s,", SCAN_ID_KEY, this.scanId))
                .append(String.format("%s=%s", DETECTED_DEVICES_KEY, report));
        return messageBuilder.toString();
    }

}
