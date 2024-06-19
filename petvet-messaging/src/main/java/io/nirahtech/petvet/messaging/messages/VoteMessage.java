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

public final class VoteMessage extends AbstractMessage {
    private static final String UPTIME_KEY = "uptime";
    private static final String LAST_IP_BYTE_KEY = "lastIpByte";

    private final long uptime;
    private final byte lastIpByte;


    public final long getUptime() {
        return uptime;
    }

    public final byte getLastIpByte() {
        return lastIpByte;
    }

        private VoteMessage(
            final UUID emitterId,
            final MacAddress emitterMAC,
            final InetAddress emitterIP,
            final EmitterMode emitterMode,
            final LocalDateTime sentAt,
            final long uptime,
            final byte lastIpByte) {
        super(emitterId, emitterMAC, emitterIP, emitterMode, MessageType.VOTE, sentAt);
        this.uptime = uptime;
        this.lastIpByte = lastIpByte;
    }

    public static VoteMessage create(final UUID emitterId, final MacAddress emitterMAC,
            final InetAddress emitterIP, final EmitterMode emitterMode, final long uptime, final byte lastIpByte) {
        return new VoteMessage(emitterId, emitterMAC, emitterIP, emitterMode, LocalDateTime.now(), uptime, lastIpByte);
    }

    public static Optional<VoteMessage> parse(String messageAsString) {
        Optional<VoteMessage> voteMessage = Optional.empty();

        if (messageAsString.contains(":")) {
            final String[] messageParts = messageAsString.split(":", 2);
            final MessageType type = MessageType.valueOf(messageParts[0]);
            if (type.equals(MessageType.VOTE)) {
                final Map<String, Object> properties = Message.fromStringToMap(messageParts[1]);
                try {

                    final UUID id = UUID.fromString(sanitize(properties.get(Message.EMITTER_ID_PROPERTY_NAME).toString()).strip());
                    final InetAddress ip = InetAddress.getByName(sanitize(properties.get(Message.EMITTER_IP_PROPERTY_NAME).toString()).strip());
                    final MacAddress mac = MacAddress.of(sanitize(properties.get(Message.EMITTER_MAC_PROPERTY_NAME).toString()).strip());
                    final EmitterMode mode = EmitterMode.valueOf(sanitize(properties.get(Message.EMITTER_MODE_PROPERTY_NAME).toString()).strip());
                    final LocalDateTime sendedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(sanitize(properties.get(Message.SENDED_AT_PROPERTY_NAME).toString()).strip())), ZoneOffset.UTC);
                    final long uptime = Long.parseLong(sanitize(properties.get(UPTIME_KEY).toString()));
                    final byte lastIpByte = Byte.parseByte(sanitize(properties.get(LAST_IP_BYTE_KEY).toString()));
                    final VoteMessage message = new VoteMessage(id, mac, ip, mode, sendedAt, uptime, lastIpByte);
                    voteMessage = Optional.of(message);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

        return voteMessage;
    }
    
    
    @Override
    public String toString() {
        StringBuilder messageBuilder = new StringBuilder()
                .append(super.toString())
                .append(String.format(",%s=%s,", UPTIME_KEY, this.uptime))
                .append(String.format("%s=%s", LAST_IP_BYTE_KEY, this.lastIpByte));
        return messageBuilder.toString();
    }
}
