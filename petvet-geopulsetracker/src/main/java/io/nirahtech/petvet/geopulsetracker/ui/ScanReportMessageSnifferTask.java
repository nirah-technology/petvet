package io.nirahtech.petvet.geopulsetracker.ui;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import io.nirahtech.petvet.messaging.brokers.MessageBroker;
import io.nirahtech.petvet.messaging.messages.Message;

public class ScanReportMessageSnifferTask implements Runnable {

    private final MessageBroker messageBroker;

    private boolean isRunning = false;
    private Set<Consumer<Message>> onNewMessageHandlers = new HashSet<>();

    public ScanReportMessageSnifferTask(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
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
