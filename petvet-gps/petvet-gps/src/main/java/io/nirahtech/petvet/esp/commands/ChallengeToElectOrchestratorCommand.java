package io.nirahtech.petvet.esp.commands;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import io.nirahtech.petvet.esp.brokers.MessagePublisher;
import io.nirahtech.petvet.esp.messages.MessageType;
import io.nirahtech.petvet.esp.messages.VoteMessage;

public final class ChallengeToElectOrchestratorCommand extends AbstractCommand {

    private final AtomicLong uptime;
    private final InetAddress ipv4Addess;
    private final MessagePublisher messageSender;
    private final UUID id;

    ChallengeToElectOrchestratorCommand(final MessagePublisher messageSender, final UUID id, final InetAddress ipv4Addess, final AtomicLong uptime) {
        this.ipv4Addess = ipv4Addess;
        this.uptime = uptime;
        this.messageSender = messageSender;
        this.id = id;
    }

    private final Map<Byte, Long> retrieveInfoToBeElectedAsOrchestrator() {
        final Map<Byte, Long> infos = new HashMap<>();    
        final RuntimeMXBean runtimeMX = ManagementFactory.getRuntimeMXBean();
        this.uptime.set(runtimeMX.getUptime());
        infos.put(this.ipv4Addess.getAddress()[3], this.uptime.get());
        return infos;
    }

    private final void publishInfoToBeElectedAsOrchestrator(final Map<Byte, Long> infos) {
        final Optional<Map.Entry<Byte, Long>> info = infos.entrySet().stream().findFirst();
        info.ifPresent(vote -> {
            final StringBuilder ballotBuilder = new StringBuilder(MessageType.VOTE.name())
            .append(":")
            .append(vote.getKey())
            .append("=")
            .append(vote.getValue());
     
            final String voteMessage = ballotBuilder.toString();
            try {
                final VoteMessage message = VoteMessage.create(this.id, this.ipv4Addess, vote.getValue(), vote.getKey());
                messageSender.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void execute() throws IOException {
        super.execute();
        final Map<Byte, Long> infos = this.retrieveInfoToBeElectedAsOrchestrator();
        this.publishInfoToBeElectedAsOrchestrator(infos);
    }
}
