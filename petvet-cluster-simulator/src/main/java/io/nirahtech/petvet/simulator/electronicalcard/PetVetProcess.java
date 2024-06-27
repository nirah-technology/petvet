package io.nirahtech.petvet.simulator.electronicalcard;

import java.net.InetAddress;
import java.util.UUID;
import java.util.function.Consumer;

import io.nirahtech.petvet.messaging.messages.MessageType;
import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;

public interface PetVetProcess {
    UUID getId();
    EmitterMode getMode();
    long getUptime();
    InetAddress getIp();
    MacAddress getMac();
    void addEventListenerOn(MessageType messageType, Runnable callback);
}
