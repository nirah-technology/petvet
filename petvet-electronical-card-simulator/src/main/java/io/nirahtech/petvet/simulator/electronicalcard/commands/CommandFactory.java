package io.nirahtech.petvet.simulator.electronicalcard.commands;

import java.net.InetAddress;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import io.nirahtech.petvet.messaging.brokers.MessagePublisher;
import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;
import io.nirahtech.petvet.simulator.electronicalcard.scanners.Scanner;

public final class CommandFactory {
    private CommandFactory() {

    }

    public static Command createScanNowCommand(MessagePublisher messageSender, UUID id, MacAddress mac, InetAddress ip, EmitterMode mode, Scanner scanner) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        return new ScanNowCommand(messageSender, id, mac, ip, mode, scanner);
    }


    public static Command createCheckIfOrchestratorIsAvailableCommand(MessagePublisher messageSender, UUID id, MacAddress mac, InetAddress ip, EmitterMode mode) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(mode, "mode is required to create a new command.");
        return new CheckIfOrchestratorIsAvailableCommand(messageSender, id, mac, ip, mode);
    }
    
    public static Command createChallengeToElectOrchestratorCommand(MessagePublisher messageSender, UUID id, MacAddress mac, InetAddress ip, EmitterMode mode, final AtomicLong uptime) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(messageSender, "Local IPV4 is required to create a new command.");
        Objects.requireNonNull(uptime, "Uptime is required to create a new command.");
        return new ChallengeToElectOrchestratorCommand(messageSender, id, mac, ip, mode, uptime);
    }
    
    public static Command createAnalyseVotesToElectOrchestratorCommand(MessagePublisher messageSender, UUID id, MacAddress mac, InetAddress ip, final AtomicReference<EmitterMode> mode, final long uptime, final Map.Entry<Byte, Long> candidacy) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(messageSender, "mode is required to create a new command.");
        Objects.requireNonNull(messageSender, "Votes are required to create a new command.");
        return new AnalyseVotesToElectOrchestratorCommand(messageSender, id, mac, ip, mode, uptime, candidacy);
    }


    public static Command createHeartBeatCommand(MessagePublisher messageSender, UUID id, MacAddress mac, InetAddress ip, final EmitterMode mode, final AtomicLong uptime) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(messageSender, "mode is required to create a new command.");
        Objects.requireNonNull(messageSender, "Votes are required to create a new command.");
        return new HeartBeatCommand(messageSender, id, mac, ip, mode, uptime);
    }


}
