package io.nirahtech.petvet.messaging.messages;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;

abstract class AbstractMessage implements Message {
    private final LocalDateTime sentAt;
    private final UUID emitterId;
    private final MacAddress emitterMAC;
    private final InetAddress emitterIP;
    private final EmitterMode emitterMode;
    private final MessageType type;
    
    protected AbstractMessage(
        final UUID emitterId, 
        final MacAddress emitterMAC,
        final InetAddress emitterIP,
        final EmitterMode emitterMode,
        final MessageType type,
        final LocalDateTime sentAt) {
        this.sentAt = sentAt;
        this.emitterId = emitterId;
        this.emitterIP = emitterIP;
        this.emitterMode = emitterMode;
        this.emitterMAC = emitterMAC;
        this.type = type;
    }

    public LocalDateTime getSentedAt() {
        return sentAt;
    }

    public UUID getEmitterID() {
        return emitterId;
    }

    public InetAddress getEmitterIP() {
        return emitterIP;
    }

    public MacAddress getEmitterMAC() {
        return emitterMAC;
    }

    public MessageType getType() {
        return type;
    }
    public EmitterMode getEmitterMode() {
        return emitterMode;
    }

    protected static final String sanitize(final String data) {
        return data.replaceAll("^[\\p{C}\\s]+|[\\p{C}\\s]+$", "").trim();
    }
    
    @Override
    public String toString() {
        StringBuilder messageBuilder = new StringBuilder()
                .append(this.getType().name())
                .append(":")
                .append(String.format("%s=%s,", Message.MESSAGE_TYPE_PROPERTY_NAME, this.getType().name()))
                .append(String.format("%s=%s,", Message.EMITTER_ID_PROPERTY_NAME, this.getEmitterID().toString()))
                .append(String.format("%s=%s,", Message.EMITTER_MAC_PROPERTY_NAME, this.getEmitterMAC().toString()))
                .append(String.format("%s=%s,", Message.EMITTER_IP_PROPERTY_NAME, this.getEmitterIP().toString().substring(1)))
                .append(String.format("%s=%s,", Message.EMITTER_MODE_PROPERTY_NAME, this.getEmitterMode().name()))
                .append(String.format("%s=%s", Message.SENDED_AT_PROPERTY_NAME, this.getSentedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        return messageBuilder.toString();
    }
}
