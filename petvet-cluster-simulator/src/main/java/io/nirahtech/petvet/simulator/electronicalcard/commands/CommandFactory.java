package io.nirahtech.petvet.simulator.electronicalcard.commands;

import java.net.InetAddress;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import io.nirahtech.petvet.messaging.brokers.MessagePublisher;
import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;
import io.nirahtech.petvet.simulator.electronicalcard.ElectronicCard;
import io.nirahtech.petvet.simulator.electronicalcard.scanners.Scanner;

public final class CommandFactory {
    private CommandFactory() {

    }

    public static Command createScanNowCommand(MessagePublisher messageSender, UUID scan, UUID id, MacAddress mac, InetAddress ip, EmitterMode mode, Scanner scanner, Set<MacAddress> neighborsBSSID, Map<ElectronicCard, Float> neigborsNodeSignals, final Runnable eventListerOnSendedMessage) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        return new ScanNowCommand(messageSender, scan, id, mac, ip, mode, scanner, neighborsBSSID, neigborsNodeSignals, eventListerOnSendedMessage);
    }


    public static Command createCheckIfOrchestratorIsAvailableCommand(MessagePublisher messageSender, UUID id, MacAddress mac, InetAddress ip, EmitterMode mode, final Runnable eventListerOnSendedMessage) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(mode, "mode is required to create a new command.");
        return new CheckIfOrchestratorIsAvailableCommand(messageSender, id, mac, ip, mode, eventListerOnSendedMessage);
    }
    
    public static Command createChallengeToElectOrchestratorCommand(MessagePublisher messageSender, UUID id, MacAddress mac, InetAddress ip, EmitterMode mode, final AtomicLong uptime, final Runnable eventListerOnSendedMessage) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(messageSender, "Local IPV4 is required to create a new command.");
        Objects.requireNonNull(uptime, "Uptime is required to create a new command.");
        return new ChallengeToElectOrchestratorCommand(messageSender, id, mac, ip, mode, uptime, eventListerOnSendedMessage);
    }
    
    public static Command createAnalyseVotesToElectOrchestratorCommand(MessagePublisher messageSender, UUID id, MacAddress mac, InetAddress ip, final AtomicReference<EmitterMode> mode, final long uptime, final Map.Entry<Byte, Long> candidacy, final Runnable eventListerOnSendedMessage) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(messageSender, "mode is required to create a new command.");
        Objects.requireNonNull(messageSender, "Votes are required to create a new command.");
        return new AnalyseVotesToElectOrchestratorCommand(messageSender, id, mac, ip, mode, uptime, candidacy, eventListerOnSendedMessage);
    }


    public static Command createHeartBeatCommand(MessagePublisher messageSender, UUID id, MacAddress mac, InetAddress ip, final EmitterMode mode, final AtomicLong uptime, final Runnable eventListerOnSendedMessage) {
        Objects.requireNonNull(messageSender, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(messageSender, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(messageSender, "mode is required to create a new command.");
        Objects.requireNonNull(messageSender, "Votes are required to create a new command.");
        return new HeartBeatCommand(messageSender, id, mac, ip, mode, uptime, eventListerOnSendedMessage);
    }


}
