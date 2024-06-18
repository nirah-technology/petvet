package io.nirahtech.petvet.esp.monitor.brokers;

import java.util.function.Consumer;

import io.nirahtech.petvet.esp.monitor.messages.Message;
import io.nirahtech.petvet.esp.monitor.messages.MessageType;


interface MessageSubscriber {
    void subscribe(final MessageType type, Consumer<Message> handler);
}
