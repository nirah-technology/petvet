package io.nirahtech.petvet.simulator.electronicalcard.commands;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import io.nirahtech.petvet.messaging.brokers.MessagePublisher;
import io.nirahtech.petvet.messaging.messages.HeartBeatMessage;
import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;

public final class HeartBeatCommand extends AbstractCommand {

    private final AtomicLong uptime;
    private final MacAddress mac;
    private final InetAddress ip;
    private final MessagePublisher messageSender;
    private final EmitterMode mode;
    private final UUID id;
    private final Consumer<UUID> eventListerOnSendedMessage;

    HeartBeatCommand(final MessagePublisher messageSender, final UUID id, final MacAddress mac, final InetAddress ip, final EmitterMode mode, final AtomicLong uptime, final Consumer<UUID> eventListerOnSendedMessage) {
        this.ip = ip;
        this.mac = mac;
        this.uptime = uptime;
        this.mode = mode;
        this.messageSender = messageSender;
        this.id = id;
        this.eventListerOnSendedMessage = eventListerOnSendedMessage;
    }

    private final void publishHealthData() {
        double cpuTemperature = 0.0D;
        double voltage = 0.0D;

        final RuntimeMXBean runtimeMX = ManagementFactory.getRuntimeMXBean();
        this.uptime.set(runtimeMX.getUptime());

        final HeartBeatMessage message = HeartBeatMessage.create(
            id, mac, ip, mode, uptime.get(), (float)cpuTemperature, (float)voltage, null
        );
        try {
            messageSender.send(message);
        } catch (IOException e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }

        if (Objects.nonNull(this.eventListerOnSendedMessage)) {
            this.eventListerOnSendedMessage.accept(id);
        }
    }

    @Override
    public void execute() throws IOException {
        super.execute();
        this.publishHealthData();
    }
}
