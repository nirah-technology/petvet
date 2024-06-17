package io.nirahtech.petvet.esp.messages;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class VoteMessage extends AbstractMessage {
    private static final String UPTIME_KEY = "uptime";
    private static final String LAST_IP_BYTE_KEY = "lastIpByte";

    private final long uptime;
    private final byte lastIpByte;

    private VoteMessage(UUID id, InetAddress emitter, boolean isOrchestrator,
            LocalDateTime sentAt, final long uptime,final byte lastIpByte ) {
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

    public static VoteMessage create(final UUID id, InetAddress emitter, final long uptime,final byte lastIpByte) {
        return new VoteMessage(id, emitter, false,
                LocalDateTime.now(), uptime, lastIpByte);
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put(UPTIME_KEY, this.uptime);
        map.put(LAST_IP_BYTE_KEY, this.lastIpByte);
        return map;
    }

    public static Optional<VoteMessage> parse(String messageAsString) {
        Optional<VoteMessage> VoteMessage = Optional.empty();
        Optional<Message> baseMessage = Message.parse(messageAsString);
        if (baseMessage.isPresent()) {
            final Message message = baseMessage.get();
            if (message.getType().equals(MessageType.VOTE)) {
                final Map<String, Object> messageMap = Message.fromStringToMap(messageAsString);
                try {
                    final long uptime = Long.parseLong(messageMap.get(UPTIME_KEY).toString());
                    System.out.println("Uptime: " + uptime);
                    final byte lastIpByte = Byte.parseByte(messageMap.get(LAST_IP_BYTE_KEY).toString());
                    System.out.println("Last IP Byte: " + lastIpByte);
                    final VoteMessage scanMessage = new VoteMessage(
                            message.getId(),
                            message.getEmitter(),
                            message.isOrchestrator(),
                            message.sentAt(),
                            uptime,
                            lastIpByte);
                    VoteMessage = Optional.of(scanMessage);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return VoteMessage;
    }
}
