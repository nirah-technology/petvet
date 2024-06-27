package io.nirahtech.petvet.simulator.electronicalcard.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import io.nirahtech.petvet.messaging.brokers.MessagePublisher;
import io.nirahtech.petvet.messaging.messages.MessageType;
import io.nirahtech.petvet.messaging.messages.OrchestratorAvailableMessage;
import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;

public final class CheckIfOrchestratorIsAvailableCommand extends AbstractCommand {

    private final EmitterMode mode;
    private final InetAddress ip;
    private final MacAddress mac;
    private final MessagePublisher messageSender;
    private final UUID id;
    private final Runnable eventListerOnSendedMessage;

    CheckIfOrchestratorIsAvailableCommand(final MessagePublisher messageSender, final UUID id, final MacAddress mac, InetAddress ip, final EmitterMode mode, final Runnable eventListerOnSendedMessage) {
        this.mode = mode;
        this.id = id;
        this.mac = mac;
        this.messageSender = messageSender;
        this.ip = ip;
        this.eventListerOnSendedMessage = eventListerOnSendedMessage;
    }

    private final void notifyThatOrchestratorIsAvailable() throws IOException {
        final OrchestratorAvailableMessage message = OrchestratorAvailableMessage.create(
            id, mac, ip, mode
        );
        this.messageSender.send(message);

        if (Objects.nonNull(this.eventListerOnSendedMessage)) {
            this.eventListerOnSendedMessage.run();
        }
    }

    /**
     * <p>Check the mode.</p>
     * <p>If the mode is NATIVE_NODE, then, do nothing.</p>
     * <p>If the mode is ORCHESTRATOR_NODE, then, notify that an orchestrator is available broadcasting a ORCHESTRATOR_AVAILABLE message.</p>
     */
    @Override
    public void execute() throws IOException {
        super.execute();
        if (this.mode.isOrchestratorMode()) {
            this.notifyThatOrchestratorIsAvailable();
        }
    }
    
}
