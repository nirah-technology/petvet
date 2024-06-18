package io.nirahtech.petvet.esp.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import io.nirahtech.petvet.esp.Mode;
import io.nirahtech.petvet.esp.brokers.MessagePublisher;
import io.nirahtech.petvet.esp.messages.OrchestratorAvailableMessage;

public final class AnalyseVotesToElectOrchestratorCommand extends AbstractCommand {


    private final Map.Entry<Byte, Long> candidacy;
    private final AtomicReference<Mode> mode;
    private final InetAddress emitter;
    private final MessagePublisher messageSender;
    private final long uptime;
    private final UUID id;

    AnalyseVotesToElectOrchestratorCommand(final MessagePublisher messageSender, final UUID id, final InetAddress emitter, final AtomicReference<Mode> mode, final long uptime, final Map.Entry<Byte, Long> candidacy) {
        this.candidacy = candidacy;
        this.mode = mode;
        this.emitter = emitter;
        this.messageSender = messageSender;
        this.id = id;
        this.uptime = uptime;
    }
    
    private final void notifyThatTheNewOrchestratorIsFound() throws IOException {
        final OrchestratorAvailableMessage message = OrchestratorAvailableMessage.create(
            this.id,
            this.emitter,
            this.mode.get().equals(Mode.ORCHESTRATOR_NODE)
        );
        this.messageSender.send(message);
    }

    @Override
    public void execute() throws IOException {
        super.execute();
        if ((this.candidacy.getValue() == this.uptime) && (this.candidacy.getKey() == this.emitter.getAddress()[3])) {
            this.mode.set(Mode.ORCHESTRATOR_NODE);
            this.notifyThatTheNewOrchestratorIsFound();
        } else {
            this.mode.set(Mode.NATIVE_NODE);
        }
    }

}
