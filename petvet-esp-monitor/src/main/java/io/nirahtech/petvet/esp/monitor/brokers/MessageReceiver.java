package io.nirahtech.petvet.esp.monitor.brokers;

import java.io.IOException;
import java.util.Optional;

import io.nirahtech.petvet.esp.monitor.messages.Message;

public interface MessageReceiver {
    Optional<Message> receive() throws IOException;
}
