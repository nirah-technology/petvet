package io.nirahtech.petvet.esp.commands;

import java.io.IOException;
import java.net.InetAddress;

import io.nirahtech.petvet.esp.MessageType;
import io.nirahtech.petvet.esp.Mode;
import io.nirahtech.petvet.esp.MulticastEmitter;

public final class CheckIfOrchestratorIsAvailableCommand extends AbstractCommand {

    private final Mode mode;

    CheckIfOrchestratorIsAvailableCommand(final InetAddress group, final int port, final Mode mode) {
        super(group, port);
        this.mode = mode;
    }

    private final void notifyThatOrchestratorIsAvailable() throws IOException {
        MulticastEmitter.broadcast(super.group, super.port, MessageType.ORCHESTRATOR_AVAILABLE.name());
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
