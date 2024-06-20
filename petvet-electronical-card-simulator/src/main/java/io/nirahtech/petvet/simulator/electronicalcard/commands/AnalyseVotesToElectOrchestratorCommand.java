package io.nirahtech.petvet.simulator.electronicalcard.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import io.nirahtech.petvet.messaging.brokers.MessagePublisher;
import io.nirahtech.petvet.messaging.messages.OrchestratorAvailableMessage;
import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;

public final class AnalyseVotesToElectOrchestratorCommand extends AbstractCommand {


    private final Map.Entry<Byte, Long> candidacy;
    private final AtomicReference<EmitterMode> mode;
    private final MacAddress mac;
    private final InetAddress ip;
    private final MessagePublisher messageSender;
    private final long uptime;
    private final UUID id;

    AnalyseVotesToElectOrchestratorCommand(final MessagePublisher messageSender, final UUID id, final MacAddress mac, final InetAddress ip, final AtomicReference<EmitterMode> mode, final long uptime, final Map.Entry<Byte, Long> candidacy) {
        this.candidacy = candidacy;
        this.mode = mode;
        this.mac = mac;
        this.ip = ip;
        this.messageSender = messageSender;
        this.id = id;
        this.uptime = uptime;
    }
    
    private final void notifyThatTheNewOrchestratorIsFound() throws IOException {
        final OrchestratorAvailableMessage message = OrchestratorAvailableMessage.create(id, mac, ip, mode.get());
        this.messageSender.send(message);
    }

    @Override
    public void execute() throws IOException {
        super.execute();
        if ((this.candidacy.getValue() == this.uptime) && (this.candidacy.getKey() == this.ip.getAddress()[3])) {
            this.mode.set(EmitterMode.ORCHESTRATOR_NODE);
            this.notifyThatTheNewOrchestratorIsFound();
        } else {
            this.mode.set(EmitterMode.NATIVE_NODE);
        }
    }

}
