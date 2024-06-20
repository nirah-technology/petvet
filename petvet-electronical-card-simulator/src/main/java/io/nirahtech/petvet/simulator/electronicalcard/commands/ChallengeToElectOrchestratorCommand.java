package io.nirahtech.petvet.simulator.electronicalcard.commands;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import io.nirahtech.petvet.messaging.brokers.MessagePublisher;
import io.nirahtech.petvet.messaging.messages.VoteMessage;
import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;

public final class ChallengeToElectOrchestratorCommand extends AbstractCommand {

    private final AtomicLong uptime;
    private final MacAddress mac;
    private final InetAddress ip;
    private final MessagePublisher messageSender;
    private final EmitterMode mode;
    private final UUID id;

    ChallengeToElectOrchestratorCommand(final MessagePublisher messageSender, final UUID id, final MacAddress mac, final InetAddress ip, final EmitterMode mode, final AtomicLong uptime) {
        this.ip = ip;
        this.mac = mac;
        this.uptime = uptime;
        this.mode = mode;
        this.messageSender = messageSender;
        this.id = id;
    }

    private final Map<Byte, Long> retrieveInfoToBeElectedAsOrchestrator() {
        final Map<Byte, Long> infos = new HashMap<>();
        final RuntimeMXBean runtimeMX = ManagementFactory.getRuntimeMXBean();
        this.uptime.set(runtimeMX.getUptime());
        infos.put(this.ip.getAddress()[3], this.uptime.get());
        return infos;
    }

    private final void publishInfoToBeElectedAsOrchestrator(final Map<Byte, Long> infos) {
        
        final Optional<Map.Entry<Byte, Long>> info = infos.entrySet().stream().findFirst();
        info.ifPresent(vote -> {
            try {
                final VoteMessage message = VoteMessage.create(
                    id, mac, ip, mode, vote.getValue(), vote.getKey()
                );
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
