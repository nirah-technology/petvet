package io.nirahtech.petvet.esp.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import io.nirahtech.petvet.esp.MessageSender;
import io.nirahtech.petvet.esp.Mode;
import io.nirahtech.petvet.esp.messages.OrchestratorAvailableMessage;

public final class AnalyseVotesToElectOrchestratorCommand extends AbstractCommand {


    private final Map<Byte, Long> candidacies;
    private final AtomicReference<Mode> mode;
    private final InetAddress emitter;
    private final MessageSender messageSender;
    private final UUID id;

    AnalyseVotesToElectOrchestratorCommand(final MessageSender messageSender, final UUID id, final InetAddress emitter, final AtomicReference<Mode> mode, final Map<Byte, Long> candidacies) {
        this.candidacies = candidacies;
        this.mode = mode;
        this.emitter = emitter;
        this.messageSender = messageSender;
        this.id = id;
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

        final Optional<Map.Entry<Byte, Long>> candidacyWithMaxUptime = this.candidacies
                .entrySet()
                .stream()
                .max((vote1, vote2) -> {
                    return Long.compare(vote1.getValue(), vote2.getValue());
                });

        if (!candidacyWithMaxUptime.isPresent()) {
            this.mode.set(Mode.NATIVE_NODE);
            return;
        }

        Collection<Map.Entry<Byte, Long>> candidaciesWithMaxUptime = this.candidacies
                .entrySet()
                .stream()
                .filter(vote -> {
                    final long voteUptime = vote.getValue();
                    final long maxUptimeFromVotes = candidacyWithMaxUptime.get().getValue();
                    return voteUptime == maxUptimeFromVotes;
                })
                .collect(Collectors.toList());

        byte maxOctet = 0;
        for (Map.Entry<Byte, Long> vote : candidaciesWithMaxUptime) {
            byte octet = vote.getKey();
            if (octet > maxOctet) {
                maxOctet = octet;
            }
        }

        if (maxOctet == this.emitter.getAddress()[3]) {
            try {
                mode.set(Mode.ORCHESTRATOR_NODE);
                this.notifyThatTheNewOrchestratorIsFound();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mode.set(Mode.NATIVE_NODE);
        }
    }

}
