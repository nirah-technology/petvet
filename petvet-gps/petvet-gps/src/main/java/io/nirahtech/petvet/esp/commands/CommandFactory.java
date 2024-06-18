package io.nirahtech.petvet.esp.commands;

import java.net.InetAddress;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import io.nirahtech.petvet.esp.Mode;
import io.nirahtech.petvet.esp.brokers.MessagePublisher;
import io.nirahtech.petvet.esp.scanners.Scanner;

public final class CommandFactory {
    private CommandFactory() {

    }

    public static Command createScanNowCommand(MessagePublisher messageSender, UUID id, InetAddress emitter, Mode mode, Scanner scanner) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        return new ScanNowCommand(messageSender, id, emitter, mode, scanner);
    }


    public static Command createCheckIfOrchestratorIsAvailableCommand(MessagePublisher messageSender, UUID id, InetAddress emitter, Mode mode) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(mode, "Mode is required to create a new command.");
        return new CheckIfOrchestratorIsAvailableCommand(messageSender, id, emitter, mode);
    }
    
    public static Command createChallengeToElectOrchestratorCommand(MessagePublisher messageSender, UUID id, InetAddress emitter, final AtomicLong uptime) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(messageSender, "Local IPV4 is required to create a new command.");
        Objects.requireNonNull(uptime, "Uptime is required to create a new command.");
        return new ChallengeToElectOrchestratorCommand(messageSender, id, emitter, uptime);
    }
    
    public static Command createAnalyseVotesToElectOrchestratorCommand(MessagePublisher messageSender, UUID id, InetAddress emitter, final AtomicReference<Mode> mode, final long uptime, final Map.Entry<Byte, Long> candidacy) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(messageSender, "Mode is required to create a new command.");
        Objects.requireNonNull(messageSender, "Votes are required to create a new command.");
        return new AnalyseVotesToElectOrchestratorCommand(messageSender, id, emitter, mode, uptime, candidacy);
    }

}
