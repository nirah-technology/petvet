package io.nirahtech.petvet.esp.monitor.messages;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.nirahtech.petvet.esp.monitor.IPV4Address;

public final class OrchestratorAvailableMessage extends AbstractMessage {

    
    private OrchestratorAvailableMessage(UUID id, InetAddress emitter, boolean isOrchestrator,
            LocalDateTime sentAt) {
        super(id, MessageType.ORCHESTRATOR_AVAILABLE, emitter, isOrchestrator, sentAt);
    }

    public static OrchestratorAvailableMessage create(UUID id, InetAddress emitter, boolean isOrchestrator) {
        return new OrchestratorAvailableMessage(id, emitter, isOrchestrator,
                LocalDateTime.now());
    }

    public static Optional<OrchestratorAvailableMessage> parse(String messageAsString) {
        Optional<OrchestratorAvailableMessage> orchestratorAvailableMessage = Optional.empty();
        if (messageAsString.contains(":")) {
            final String[] messageParts = messageAsString.split(":", 2);
            final MessageType type = MessageType.valueOf(messageParts[0]);
            if (type.equals(MessageType.ORCHESTRATOR_AVAILABLE)) {
                final Map<String, Object> properties = Message.fromStringToMap(messageParts[1]);
                try {
                    OrchestratorAvailableMessage message = new OrchestratorAvailableMessage(
                        UUID.fromString(properties.get(Message.ID_PROPERTY_NAME).toString().strip()),
                        IPV4Address.of(properties.get(Message.EMITTER_PROPERTY_NAME).toString().strip().substring(1)).toInetAddress(), 
                        Boolean.parseBoolean(properties.get(Message.IS_ORCHESTRATOR_PROPERTY_NAME).toString().strip()),
                            LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(properties.get(Message.SENDED_AT_PROPERTY_NAME).toString().strip())), ZoneId.systemDefault()));
                        orchestratorAvailableMessage = Optional.of(message);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        return orchestratorAvailableMessage;
    }
    
    
    @Override
    public String toString() {
        StringBuilder messageBuilder = new StringBuilder()
                .append(this.getType().name())
                .append(":")
                .append(String.format("%s=%s,", Message.TYPE_PROPERTY_NAME, this.getType().name()))
                .append(String.format("%s=%s,", Message.IS_ORCHESTRATOR_PROPERTY_NAME, this.isOrchestrator()))
                .append(String.format("%s=%s,", Message.ID_PROPERTY_NAME, this.getId().toString()))
                .append(String.format("%s=%s,", Message.SENDED_AT_PROPERTY_NAME, this.sentAt().toInstant(ZoneOffset.UTC).getNano()))
                .append(String.format("%s=%s", Message.EMITTER_PROPERTY_NAME, this.getEmitter().toString()));
        return messageBuilder.toString();
    }
}
