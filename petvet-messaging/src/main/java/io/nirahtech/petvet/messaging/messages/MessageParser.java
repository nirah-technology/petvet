package io.nirahtech.petvet.messaging.messages;

import java.util.Optional;

interface MessageParser {
    Optional<? extends Message> parse(String messageAsString);    
}
