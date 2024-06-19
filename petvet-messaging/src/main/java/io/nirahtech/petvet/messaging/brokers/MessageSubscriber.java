package io.nirahtech.petvet.messaging.brokers;

import java.util.function.Consumer;

import io.nirahtech.petvet.messaging.messages.Message;
import io.nirahtech.petvet.messaging.messages.MessageType;


interface MessageSubscriber {
    void subscribe(final MessageType type, Consumer<Message> handler);
}
