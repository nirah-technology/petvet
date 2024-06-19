package io.nirahtech.petvet.messaging.brokers;

import java.io.IOException;

import io.nirahtech.petvet.messaging.messages.Message;

public interface MessagePublisher {
    void send(Message message) throws IOException;
}
