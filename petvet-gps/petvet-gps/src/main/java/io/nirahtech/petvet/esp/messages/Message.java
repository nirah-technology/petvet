package io.nirahtech.petvet.esp.messages;

import java.io.Serializable;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
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

    public static Map<String, Object> fromStringToMap(String messageAsString) {
        return Arrays.stream(messageAsString.split(","))
                .filter(entry -> entry.contains("="))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(entry -> entry[0].strip(), entry -> entry[1].strip()));
    }
}
