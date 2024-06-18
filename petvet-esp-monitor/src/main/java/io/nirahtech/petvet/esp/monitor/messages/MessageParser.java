package io.nirahtech.petvet.esp.monitor.messages;

import java.util.Optional;

interface MessageParser {
    Optional<? extends Message> parse(String messageAsString);    
}
