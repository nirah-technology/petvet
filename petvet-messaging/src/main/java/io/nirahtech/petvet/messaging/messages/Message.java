package io.nirahtech.petvet.messaging.messages;

import java.io.Serializable;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;

public interface Message extends Serializable {
    public static final String SENDED_AT_PROPERTY_NAME = "sentedAt";
    public static final String MESSAGE_TYPE_PROPERTY_NAME = "type";
    public static final String EMITTER_ID_PROPERTY_NAME = "emitterID";
    public static final String EMITTER_MAC_PROPERTY_NAME = "emitterMAC";
    public static final String EMITTER_IP_PROPERTY_NAME = "emitterIP";
    public static final String EMITTER_MODE_PROPERTY_NAME = "emitterMode";

    UUID getEmitterID();
    MacAddress getEmitterMAC();
    InetAddress getEmitterIP();
    EmitterMode getEmitterMode();
    MessageType getType();
    LocalDateTime getSentedAt();

    public static Map<String, Object> fromStringToMap(String messageAsString) {
        return Arrays.stream(messageAsString.split(","))
                .filter(entry -> entry.contains("="))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(entry -> entry[0].strip(), entry -> entry[1].strip()));
    }
}
