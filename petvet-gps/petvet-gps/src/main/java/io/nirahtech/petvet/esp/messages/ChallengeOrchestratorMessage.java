package io.nirahtech.petvet.esp.messages;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class ChallengeOrchestratorMessage extends AbstractMessage {

    private ChallengeOrchestratorMessage(UUID id, InetAddress emitter, boolean isOrchestrator,
            LocalDateTime sentAt) {
        super(id, MessageType.CHALLENGE_ORCHESTRATOR, emitter, isOrchestrator, sentAt);
    }

    public static ChallengeOrchestratorMessage create(InetAddress emitter, boolean isOrchestrator) {
        return new ChallengeOrchestratorMessage(UUID.randomUUID(), emitter, isOrchestrator,
                LocalDateTime.now());
    }

    public static Optional<ChallengeOrchestratorMessage> parse(String messageAsString) {
        Optional<ChallengeOrchestratorMessage> challengeOrchestratorMessage = Optional.empty();
        Optional<Message> baseMessage = Message.parse(messageAsString);
        if (baseMessage.isPresent()) {
            final Message message = baseMessage.get();
            if (message.getType() == MessageType.CHALLENGE_ORCHESTRATOR) {
                final ChallengeOrchestratorMessage scanMessage = new ChallengeOrchestratorMessage(
                        message.getId(),
                        message.getEmitter(),
                        message.isOrchestrator(),
                        message.sentAt());
                        challengeOrchestratorMessage = Optional.of(scanMessage);
            }
        }
        return challengeOrchestratorMessage;
    }
}
