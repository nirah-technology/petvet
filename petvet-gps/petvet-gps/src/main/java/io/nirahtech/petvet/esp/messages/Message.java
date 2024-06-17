package io.nirahtech.petvet.esp.messages;

import java.io.Serializable;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public interface Message extends Serializable {
    public static final String ID_PROPERTY_NAME = "id";
    public static final String TYPE_PROPERTY_NAME = "type";
    public static final String EMITTER_PROPERTY_NAME = "emitter";
    public static final String IS_ORCHESTRATOR_PROPERTY_NAME = "isOrchestrator";
    public static final String SENDED_AT_PROPERTY_NAME = "sentAt";

    UUID getId();

    boolean isOrchestrator();

    MessageType getType();

    InetAddress getEmitter();

    LocalDateTime sentAt();

    public static Optional<Message> parse(String messageAsString) {
        Message message = null;
        try {
            final Map<String, Object> messageAsMap = Message.fromStringToMap(messageAsString);
            final UUID id = UUID.fromString((String) messageAsMap.get(ID_PROPERTY_NAME));
            final MessageType type = MessageType.valueOf((String) messageAsMap.get(TYPE_PROPERTY_NAME));
            InetAddress emitter = null;
            final String ip = (String) messageAsMap.get(EMITTER_PROPERTY_NAME);
            emitter = InetAddress.getByName((ip.startsWith("/") ? ip.substring(1) : ip));
            final boolean isOrchestrator = Boolean
                    .parseBoolean((String) messageAsMap.get(IS_ORCHESTRATOR_PROPERTY_NAME));
            final LocalDateTime sentAt = LocalDateTime.parse((String) messageAsMap.get(SENDED_AT_PROPERTY_NAME));

            message = new AbstractMessage(id, type, emitter, isOrchestrator, sentAt) {
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(message);

    }

    public static Map<String, Object> fromStringToMap(String messageAsString) {
        return Arrays.stream(messageAsString.split(","))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
    }

    default Map<String, Object> toMap() {
        final Map<String, Object> messageAsMap = new HashMap<>();
        messageAsMap.put(ID_PROPERTY_NAME, this.getId());
        messageAsMap.put(TYPE_PROPERTY_NAME, this.getType());
        messageAsMap.put(EMITTER_PROPERTY_NAME, this.getEmitter());
        messageAsMap.put(IS_ORCHESTRATOR_PROPERTY_NAME, this.isOrchestrator());
        messageAsMap.put(SENDED_AT_PROPERTY_NAME, this.sentAt());
        return messageAsMap;
    }
}
