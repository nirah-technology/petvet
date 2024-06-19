package io.nirahtech.petvet.messaging.brokers;

import java.io.IOException;
import java.util.Optional;

import io.nirahtech.petvet.messaging.messages.Message;

public interface MessageReceiver {
    Optional<Message> receive() throws IOException;
}
