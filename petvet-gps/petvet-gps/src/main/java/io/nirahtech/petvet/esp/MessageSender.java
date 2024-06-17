package io.nirahtech.petvet.esp;

import java.io.IOException;

import io.nirahtech.petvet.esp.messages.Message;

public interface MessageSender {
    void send(Message message) throws IOException;
}
