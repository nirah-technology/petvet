package io.nirahtech.petvet.esp;

import java.util.function.Consumer;

import io.nirahtech.petvet.esp.messages.Message;


interface MessageReceiver {
    void subscribe(final io.nirahtech.petvet.esp.messages.MessageType type, Consumer<Message> handler);
}
