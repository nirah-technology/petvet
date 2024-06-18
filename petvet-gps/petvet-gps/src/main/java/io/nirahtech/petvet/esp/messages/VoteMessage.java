package io.nirahtech.petvet.esp.messages;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.nirahtech.petvet.esp.IPV4Address;

public final class VoteMessage extends AbstractMessage {
    private static final String UPTIME_KEY = "uptime";
    private static final String LAST_IP_BYTE_KEY = "lastIpByte";

    private final long uptime;
    private final byte lastIpByte;

    private VoteMessage(UUID id, InetAddress emitter, boolean isOrchestrator,
            LocalDateTime sentAt, final long uptime, final byte lastIpByte) {
        super(id, MessageType.VOTE, emitter, isOrchestrator, sentAt);
        this.uptime = uptime;
        this.lastIpByte = lastIpByte;
    }

    public final long getUptime() {
        return uptime;
    }

    public final byte getLastIpByte() {
        return lastIpByte;
    }

    public static VoteMessage create(final UUID id, InetAddress emitter, final long uptime, final byte lastIpByte) {
        return new VoteMessage(id, emitter, false,
                LocalDateTime.now(), uptime, lastIpByte);
    }

    public static Optional<VoteMessage> parse(String messageAsString) {
        Optional<VoteMessage> voteMessage = Optional.empty();

        if (messageAsString.contains(":")) {
            final String[] messageParts = messageAsString.split(":", 2);
            final MessageType type = MessageType.valueOf(messageParts[0]);
            if (type.equals(MessageType.VOTE)) {
                final Map<String, Object> properties = Message.fromStringToMap(messageParts[1]);
                try {
                    VoteMessage message = new VoteMessage(
                        UUID.fromString(properties.get(Message.ID_PROPERTY_NAME).toString().strip()),
                        IPV4Address.of(properties.get(Message.EMITTER_PROPERTY_NAME).toString().strip().substring(1)).toInetAddress(),
                        Boolean.parseBoolean(properties.get(Message.IS_ORCHESTRATOR_PROPERTY_NAME).toString().strip()),
                        LocalDateTime.parse(properties.get(Message.SENDED_AT_PROPERTY_NAME).toString().strip(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS")),
                            Long.parseLong(properties.get(UPTIME_KEY).toString()),
                            Byte.parseByte(properties.get(LAST_IP_BYTE_KEY).toString()));
                    voteMessage = Optional.of(message);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

        return voteMessage;
    }
}
