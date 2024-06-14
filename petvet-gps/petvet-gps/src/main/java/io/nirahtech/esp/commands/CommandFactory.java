package io.nirahtech.esp.commands;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import io.nirahtech.esp.Mode;

public final class CommandFactory {
    private CommandFactory() {

    }

    public static Command createScanNowCommand(final InetAddress group, final int port) {
        Objects.requireNonNull(group, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(port, "Multicast Port is required to create a new command.");
        return new ScanNowCommand(group, port);
    }


    public static Command createCheckIfOrchestratorIsAvailableCommand(final InetAddress group, final int port, final Mode mode) {
        Objects.requireNonNull(group, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(port, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(mode, "Mode is required to create a new command.");
        return new CheckIfOrchestratorIsAvailableCommand(group, port, mode);
    }
    
    public static Command createChallengeToElectOrchestratorCommand(final InetAddress group, final int port, final Inet4Address ip, final AtomicLong uptime) {
        Objects.requireNonNull(group, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(port, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(ip, "Local IPV4 is required to create a new command.");
        Objects.requireNonNull(uptime, "Uptime is required to create a new command.");
        return new ChallengeToElectOrchestratorCommand(group, port, ip, uptime);
    }
    
    public static Command createAnalyseVotesToElectOrchestratorCommand(final InetAddress group, final int port, final AtomicReference<Mode> mode, final Inet4Address ip, final Map<Byte, Long> candidacies) {
        Objects.requireNonNull(group, "Multicast Group is required to create a new command.");
        Objects.requireNonNull(port, "Multicast Port is required to create a new command.");
        Objects.requireNonNull(mode, "Mode is required to create a new command.");
        Objects.requireNonNull(candidacies, "Votes are required to create a new command.");
        return new AnalyseVotesToElectOrchestratorCommand(group, port, mode, ip, candidacies);
    }

}
