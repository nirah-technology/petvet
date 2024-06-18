package io.nirahtech.petvet.esp.monitor.brokers;

import java.io.IOException;

import io.nirahtech.petvet.esp.messages.Message;

public interface MessagePublisher {
    void send(Message message) throws IOException;
}
