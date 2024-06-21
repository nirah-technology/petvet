package io.nirahtech.petvet.messaging.messages;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;

public class ScanNowMessage extends AbstractMessage {

    private static final String SCAN_ID_KEY = "scanId";

    private final UUID scanId;
    private ScanNowMessage(
        final UUID scanId,
        final UUID emitterID,
        final MacAddress emitterMAC,
        final InetAddress emitterIP, 
        final EmitterMode emitterMode, 
        final LocalDateTime sentAt
    ) {
        super(emitterID, emitterMAC, emitterIP, emitterMode, MessageType.SCAN_NOW, sentAt);
        this.scanId = scanId;
    }

    public UUID getScanId() {
        return scanId;
    }

    public static ScanNowMessage create(final UUID emitterId, final MacAddress emitterMAC, final InetAddress emitterIP, final EmitterMode emitterMode) {
        return new ScanNowMessage(UUID.randomUUID(), emitterId, emitterMAC, emitterIP, emitterMode, LocalDateTime.now());
    }

    public static Optional<ScanNowMessage> parse(String messageAsString) {
        Optional<ScanNowMessage> scanNowMessage = Optional.empty();
        if (messageAsString.contains(":")) {
            final String[] messageParts = messageAsString.split(":", 2);
            final MessageType type = MessageType.valueOf(messageParts[0]);
            if (type.equals(MessageType.SCAN_NOW)) {
                final Map<String, Object> properties = Message.fromStringToMap(messageParts[1]);
                try {
                    final UUID scan = UUID.fromString(sanitize(properties.get(SCAN_ID_KEY).toString()).strip());
                    final UUID id = UUID.fromString(sanitize(properties.get(Message.EMITTER_ID_PROPERTY_NAME).toString()).strip());
                    final InetAddress ip = InetAddress.getByName(sanitize(properties.get(Message.EMITTER_IP_PROPERTY_NAME).toString()).strip());
                    final MacAddress mac = MacAddress.of(sanitize(properties.get(Message.EMITTER_MAC_PROPERTY_NAME).toString()).strip());
                    final EmitterMode mode = EmitterMode.valueOf(sanitize(properties.get(Message.EMITTER_MODE_PROPERTY_NAME).toString()).strip());
                    final LocalDateTime sendedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(sanitize(properties.get(Message.SENDED_AT_PROPERTY_NAME).toString()).strip())), ZoneOffset.UTC);
                    final ScanNowMessage message = new ScanNowMessage(scan, id, mac, ip, mode, sendedAt);
                        scanNowMessage = Optional.of(message);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        return scanNowMessage;
    }
    
    @Override
    public String toString() {
        StringBuilder messageBuilder = new StringBuilder()
                .append(super.toString())
                .append(String.format(",%s=%s", SCAN_ID_KEY, this.scanId));
        return messageBuilder.toString();
    }
}
