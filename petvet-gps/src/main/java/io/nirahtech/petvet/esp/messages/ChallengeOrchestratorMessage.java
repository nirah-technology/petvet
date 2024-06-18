package io.nirahtech.petvet.esp.messages;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.nirahtech.petvet.esp.IPV4Address;

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
        if (messageAsString.contains(":")) {
            final String[] messageParts = messageAsString.split(":", 2);
            final MessageType type = MessageType.valueOf(messageParts[0]);
            if (type.equals(MessageType.CHALLENGE_ORCHESTRATOR)) {
                final Map<String, Object> properties = Message.fromStringToMap(messageParts[1]);
                try {
                    ChallengeOrchestratorMessage message = new ChallengeOrchestratorMessage(
                        UUID.fromString(properties.get(Message.ID_PROPERTY_NAME).toString().strip()),
                        IPV4Address.of(properties.get(Message.EMITTER_PROPERTY_NAME).toString().strip().substring(1)).toInetAddress(), 
                        Boolean.parseBoolean(properties.get(Message.IS_ORCHESTRATOR_PROPERTY_NAME).toString().strip()),
                            LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(properties.get(Message.SENDED_AT_PROPERTY_NAME).toString().strip())), ZoneId.systemDefault()));
                    challengeOrchestratorMessage = Optional.of(message);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        return challengeOrchestratorMessage;
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
