package io.nirahtech.petvet.esp.commands;

import java.net.InetAddress;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import io.nirahtech.petvet.esp.MessageSender;
import io.nirahtech.petvet.esp.Mode;

public final class CommandFactory {
    private CommandFactory() {

    }

    public static Command createScanNowCommand(MessageSender messageSender, UUID id, InetAddress emitter, Mode mode) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        return new ScanNowCommand(messageSender, id, emitter, mode);
    }


    public static Command createCheckIfOrchestratorIsAvailableCommand(MessageSender messageSender, UUID id, InetAddress emitter, Mode mode) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(mode, "Mode is required to create a new command.");
        return new CheckIfOrchestratorIsAvailableCommand(messageSender, id, emitter, mode);
    }
    
    public static Command createChallengeToElectOrchestratorCommand(MessageSender messageSender, UUID id, InetAddress emitter, final AtomicLong uptime) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(messageSender, "Local IPV4 is required to create a new command.");
        Objects.requireNonNull(uptime, "Uptime is required to create a new command.");
        return new ChallengeToElectOrchestratorCommand(messageSender, id, emitter, uptime);
    }
    
    public static Command createAnalyseVotesToElectOrchestratorCommand(MessageSender messageSender, UUID id, InetAddress emitter, final AtomicReference<Mode> mode, final Map<Byte, Long> candidacies) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(messageSender, "Mode is required to create a new command.");
        Objects.requireNonNull(messageSender, "Votes are required to create a new command.");
        return new AnalyseVotesToElectOrchestratorCommand(messageSender, id, emitter, mode, candidacies);
    }

}
