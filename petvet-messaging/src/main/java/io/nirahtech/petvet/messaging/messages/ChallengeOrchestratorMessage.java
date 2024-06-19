package io.nirahtech.petvet.messaging.messages;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;


public class ChallengeOrchestratorMessage extends AbstractMessage {

    private ChallengeOrchestratorMessage(
        final UUID emitterId, 
        final MacAddress emitterMAC,
        final InetAddress emitterIP, 
        final EmitterMode emitterMode, 
        final LocalDateTime sentAt
    ) {
        super(emitterId, emitterMAC, emitterIP, emitterMode, MessageType.CHALLENGE_ORCHESTRATOR, sentAt);
    }

    public static ChallengeOrchestratorMessage create(final UUID emitterId, final MacAddress emitterMAC, final InetAddress emitterIP, final EmitterMode emitterMode) {
        return new ChallengeOrchestratorMessage(emitterId, emitterMAC, emitterIP, emitterMode, LocalDateTime.now());
    }

    public static Optional<ChallengeOrchestratorMessage> parse(String messageAsString) {
        Optional<ChallengeOrchestratorMessage> challengeOrchestratorMessage = Optional.empty();
        if (messageAsString.contains(":")) {
            final String[] messageParts = messageAsString.split(":", 2);
            final MessageType type = MessageType.valueOf(messageParts[0]);
            if (type.equals(MessageType.CHALLENGE_ORCHESTRATOR)) {
                final Map<String, Object> properties = Message.fromStringToMap(messageParts[1]);
                try {
                    final UUID id = UUID.fromString(sanitize(properties.get(Message.EMITTER_ID_PROPERTY_NAME).toString()).strip());
                    final InetAddress ip = InetAddress.getByName(sanitize(properties.get(Message.EMITTER_IP_PROPERTY_NAME).toString()).strip());
                    final MacAddress mac = MacAddress.of(sanitize(properties.get(Message.EMITTER_MAC_PROPERTY_NAME).toString()).strip());
                    final EmitterMode mode = EmitterMode.valueOf(sanitize(properties.get(Message.EMITTER_MODE_PROPERTY_NAME).toString()).strip());
                    final LocalDateTime sendedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(sanitize(properties.get(Message.SENDED_AT_PROPERTY_NAME).toString()).strip())), ZoneOffset.UTC);
                    final ChallengeOrchestratorMessage message = new ChallengeOrchestratorMessage(id, mac, ip, mode, sendedAt);
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
                .append(super.toString());
        return messageBuilder.toString();
    }

}
