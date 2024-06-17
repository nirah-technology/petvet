package io.nirahtech.petvet.esp.messages;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public final class IsOrchestratorAvailableMessage extends AbstractMessage {
    private IsOrchestratorAvailableMessage(UUID id, InetAddress emitter, boolean isOrchestrator,
            LocalDateTime sentAt) {
        super(id, MessageType.IS_ORCHESTRATOR_AVAILABLE, emitter, isOrchestrator, sentAt);
    }

    public static IsOrchestratorAvailableMessage create(InetAddress emitter, boolean isOrchestrator) {
        return new IsOrchestratorAvailableMessage(UUID.randomUUID(), emitter, isOrchestrator,
                LocalDateTime.now());
    }

    public static Optional<IsOrchestratorAvailableMessage> parse(String messageAsString) {
        Optional<IsOrchestratorAvailableMessage> isOrchestratorAvailableMessage = Optional.empty();
        Optional<Message> baseMessage = Message.parse(messageAsString);
        if (baseMessage.isPresent()) {
            final Message message = baseMessage.get();
            if (message.getType() == MessageType.IS_ORCHESTRATOR_AVAILABLE) {
                final IsOrchestratorAvailableMessage scanMessage = new IsOrchestratorAvailableMessage(
                        message.getId(),
                        message.getEmitter(),
                        message.isOrchestrator(),
                        message.sentAt());
                        isOrchestratorAvailableMessage = Optional.of(scanMessage);
            }
        }
        return isOrchestratorAvailableMessage;
    }
}
