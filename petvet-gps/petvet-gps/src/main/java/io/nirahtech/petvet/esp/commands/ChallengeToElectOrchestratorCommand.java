package io.nirahtech.petvet.esp.commands;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import io.nirahtech.petvet.esp.MessageType;
import io.nirahtech.petvet.esp.MulticastEmitter;

public final class ChallengeToElectOrchestratorCommand extends AbstractCommand {

    private final AtomicLong uptime;
    private final Inet4Address ipv4Addess;

    ChallengeToElectOrchestratorCommand(InetAddress group, int port, final Inet4Address ipv4Addess, final AtomicLong uptime) {
        super(group, port);
        this.ipv4Addess = ipv4Addess;
        this.uptime = uptime;
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
                MulticastEmitter.broadcast(this.group, this.port, voteMessage);
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
