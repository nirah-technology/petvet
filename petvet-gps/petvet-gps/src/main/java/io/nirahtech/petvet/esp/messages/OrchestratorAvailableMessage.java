package io.nirahtech.petvet.esp.messages;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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
        Optional<Message> baseMessage = Message.parse(messageAsString);
        if (baseMessage.isPresent()) {
            final Message message = baseMessage.get();
            if (message.getType() == MessageType.ORCHESTRATOR_AVAILABLE) {
                final OrchestratorAvailableMessage scanMessage = new OrchestratorAvailableMessage(
                        message.getId(),
                        message.getEmitter(),
                        message.isOrchestrator(),
                        message.sentAt());
                        orchestratorAvailableMessage = Optional.of(scanMessage);
            }
        }
        return orchestratorAvailableMessage;
    }
}
