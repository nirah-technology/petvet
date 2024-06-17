package io.nirahtech.petvet.esp.messages;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class ScanNowMessage extends AbstractMessage {

    private ScanNowMessage(UUID id, InetAddress emitter, boolean isOrchestrator,
            LocalDateTime sentAt) {
        super(id, MessageType.SCAN_NOW, emitter, isOrchestrator, sentAt);
    }

    public static ScanNowMessage create(UUID id, InetAddress emitter, boolean isOrchestrator) {
        return new ScanNowMessage(id, emitter, isOrchestrator,
                LocalDateTime.now());
    }

    public static Optional<ScanNowMessage> parse(String messageAsString) {
        Optional<ScanNowMessage> scanNowMessage = Optional.empty();
        Optional<Message> baseMessage = Message.parse(messageAsString);
        if (baseMessage.isPresent()) {
            final Message message = baseMessage.get();
            if (message.getType() == MessageType.SCAN_NOW) {
                final ScanNowMessage scanMessage = new ScanNowMessage(
                        message.getId(),
                        message.getEmitter(),
                        message.isOrchestrator(),
                        message.sentAt());
                scanNowMessage = Optional.of(scanMessage);
            }
        }
        return scanNowMessage;
    }
}
