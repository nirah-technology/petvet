package io.nirahtech.petvet.esp.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import io.nirahtech.petvet.esp.Mode;
import io.nirahtech.petvet.esp.brokers.MessagePublisher;
import io.nirahtech.petvet.esp.messages.OrchestratorAvailableMessage;

public final class CheckIfOrchestratorIsAvailableCommand extends AbstractCommand {

    private final Mode mode;
    private final InetAddress ipv4Addess;
    private final MessagePublisher messageSender;
    private final UUID id;

    CheckIfOrchestratorIsAvailableCommand(final MessagePublisher messageSender, final UUID id, InetAddress emitter, final Mode mode) {
        this.mode = mode;
        this.id = id;
        this.messageSender = messageSender;
        this.ipv4Addess = emitter;
    }

    private final void notifyThatOrchestratorIsAvailable() throws IOException {
        final OrchestratorAvailableMessage message = OrchestratorAvailableMessage.create(this.id, this.ipv4Addess, this.mode.equals(Mode.ORCHESTRATOR_NODE));
        this.messageSender.send(message);
    }

    /**
     * <p>Check the mode.</p>
     * <p>If the mode is NATIVE_NODE, then, do nothing.</p>
     * <p>If the mode is ORCHESTRATOR_NODE, then, notify that an orchestrator is available broadcasting a ORCHESTRATOR_AVAILABLE message.</p>
     */
    @Override
    public void execute() throws IOException {
        super.execute();
        if (this.mode == Mode.ORCHESTRATOR_NODE) {
            this.notifyThatOrchestratorIsAvailable();
        }
    }
    
}
