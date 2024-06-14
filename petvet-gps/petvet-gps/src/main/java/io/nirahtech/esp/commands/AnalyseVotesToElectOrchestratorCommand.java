package io.nirahtech.esp.commands;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import io.nirahtech.esp.MessageType;
import io.nirahtech.esp.Mode;
import io.nirahtech.esp.MulticastEmitter;

public final class AnalyseVotesToElectOrchestratorCommand extends AbstractCommand {

    private final Map<Byte, Long> candidacies;
    private final AtomicReference<Mode> mode;
    private final Inet4Address ipv4Addess;

    AnalyseVotesToElectOrchestratorCommand(InetAddress group, int port, final AtomicReference<Mode> mode,
            final Inet4Address ipv4Addess, final Map<Byte, Long> candidacies) {
        super(group, port);
        this.candidacies = candidacies;
        this.mode = mode;
        this.ipv4Addess = ipv4Addess;
    }
    
    private final void notifyThatTheNewOrchestratorIsFound() throws IOException {
        MulticastEmitter.broadcast(group, port, MessageType.ORCHESTRATOR_AVAILABLE.name());
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

        if (maxOctet == this.ipv4Addess.getAddress()[3]) {
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
