package io.nirahtech.petvet.esp.messages;

import java.util.Optional;

interface MessageParser {
    Optional<? extends Message> parse(String messageAsString);    
}
