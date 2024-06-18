package io.nirahtech.petvet.esp.messages;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.nirahtech.petvet.esp.IPV4Address;

public final class IsOrchestratorAvailableMessage extends AbstractMessage {
    private IsOrchestratorAvailableMessage(UUID id, InetAddress emitter, boolean isOrchestrator,
            LocalDateTime sentAt) {
        super(id, MessageType.IS_ORCHESTRATOR_AVAILABLE, emitter, isOrchestrator, sentAt);
    }

    public static IsOrchestratorAvailableMessage create(UUID id, InetAddress emitter, boolean isOrchestrator) {
        return new IsOrchestratorAvailableMessage(id, emitter, isOrchestrator,
                LocalDateTime.now());
    }

    public static Optional<IsOrchestratorAvailableMessage> parse(String messageAsString) {
        Optional<IsOrchestratorAvailableMessage> isOrchestratorAvailableMessage = Optional.empty();
        if (messageAsString.contains(":")) {
            final String[] messageParts = messageAsString.split(":", 2);
            final MessageType type = MessageType.valueOf(messageParts[0]);
            if (type.equals(MessageType.IS_ORCHESTRATOR_AVAILABLE)) {
                final Map<String, Object> properties = Message.fromStringToMap(messageParts[1]);
                try {
                    IsOrchestratorAvailableMessage message = new IsOrchestratorAvailableMessage(
                        UUID.fromString(properties.get(Message.ID_PROPERTY_NAME).toString().strip()),
                        IPV4Address.of(properties.get(Message.EMITTER_PROPERTY_NAME).toString().strip().substring(1)).toInetAddress(), 
                        Boolean.parseBoolean(properties.get(Message.IS_ORCHESTRATOR_PROPERTY_NAME).toString().strip()),
                        LocalDateTime.parse(properties.get(Message.SENDED_AT_PROPERTY_NAME).toString().strip(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")));
                        isOrchestratorAvailableMessage = Optional.of(message);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        return isOrchestratorAvailableMessage;
    }

    
    @Override
    public String toString() {
        StringBuilder messageBuilder = new StringBuilder()
                .append(this.getType().name())
                .append(":")
                .append(String.format("%s=%s,", Message.TYPE_PROPERTY_NAME, this.getType().name()))
                .append(String.format("%s=%s,", Message.IS_ORCHESTRATOR_PROPERTY_NAME, this.isOrchestrator()))
                .append(String.format("%s=%s,", Message.ID_PROPERTY_NAME, this.getId().toString()))
                .append(String.format("%s=%s,", Message.SENDED_AT_PROPERTY_NAME, this.sentAt().toString()))
                .append(String.format("%s=%s", Message.EMITTER_PROPERTY_NAME, this.getEmitter().toString()));
        return messageBuilder.toString();
    }
}
