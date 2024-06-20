package io.nirahtech.petvet.cluster.monitor;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Consumer;

import io.nirahtech.petvet.cluster.monitor.data.Device;
import io.nirahtech.petvet.cluster.monitor.data.ElectronicalCard;
import io.nirahtech.petvet.cluster.monitor.data.HeartBeat;
import io.nirahtech.petvet.cluster.monitor.data.ScanReport;
import io.nirahtech.petvet.messaging.brokers.MessageBroker;
import io.nirahtech.petvet.messaging.messages.Message;
import io.nirahtech.petvet.messaging.util.MacAddress;

public class MonitorTask implements Runnable {

    private final MessageBroker messageBroker;
    private final List<Message> receivedMessages;
    private final SortedSet<ElectronicalCard> esps;
    private final Map<MacAddress, Set<HeartBeat>> heartBeats;
    private final SortedSet<ScanReport> scanReports;
    private final SortedSet<Device> detectedDevices;

    private boolean isRunning = false;
    private Set<Consumer<Message>> onNewMessageHandlers = new HashSet<>();

    public MonitorTask(MessageBroker messageBroker, List<Message> receivedMessages, SortedSet<ElectronicalCard> esps,
            Map<MacAddress, Set<HeartBeat>> heartBeats, SortedSet<ScanReport> scanReports, SortedSet<Device> detectedDevices) {
        this.messageBroker = messageBroker;
        this.receivedMessages = receivedMessages;
        this.esps = esps;
        this.heartBeats = heartBeats;
        this.scanReports = scanReports;
        this.detectedDevices = detectedDevices;
    }

    public void addOnNewMessageHandler(Consumer<Message> onNewMessageHandler) {
        if (Objects.nonNull(onNewMessageHandler)) {
            this.onNewMessageHandlers.add(onNewMessageHandler);
        }
    }

    @Override
    public void run() {
        if (!this.isRunning) {
            this.isRunning = true;
            while (this.isRunning) {
                if (this.messageBroker.isConnected()) {
                    Optional<Message> receivedMessage = Optional.empty();
                    try {
                        receivedMessage = this.messageBroker.receive();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    receivedMessage.ifPresent(message -> {
                        this.receivedMessages.add(message);
                        this.onNewMessageHandlers.forEach(handler -> {
                            handler.accept(message);
                        });
                    });
                } else {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
