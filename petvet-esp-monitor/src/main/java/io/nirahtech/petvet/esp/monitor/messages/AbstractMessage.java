package io.nirahtech.petvet.esp.monitor.messages;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.UUID;

abstract class AbstractMessage implements Message {
    private final UUID id;
    private final MessageType type;
    private final InetAddress emitter;
    private final LocalDateTime sentAt;
    private final boolean isOrchestrator;

    protected AbstractMessage(UUID id, MessageType type, final InetAddress emitter, final boolean isOrchestrator, LocalDateTime sentAt) {
        this.id = id;
        this.type = type;
        this.emitter = emitter;
        this.isOrchestrator = isOrchestrator;
        this.sentAt = sentAt;
    }

    @Override
    public final UUID getId() {
        return this.id;
    }

    @Override
    public final MessageType getType() {
        return this.type;
    }

    @Override
    public final InetAddress getEmitter() {
        return this.emitter;
    }

    @Override
    public final LocalDateTime sentAt() {
        return this.sentAt;
    }

    @Override
    public final boolean isOrchestrator() {
        return this.isOrchestrator;
    }

}
