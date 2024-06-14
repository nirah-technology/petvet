package io.nirahtech.esp.commands;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import io.nirahtech.esp.MessageType;
import io.nirahtech.esp.MulticastEmitter;

public final class ChallengeToElectOrchestratorCommand extends AbstractCommand {

    private static final String UPTIME_KEY = "uptime";
    private static final String OCTET_KEY = "octet";

    private final AtomicLong uptime;
    private final Inet4Address ipv4Addess;

    ChallengeToElectOrchestratorCommand(InetAddress group, int port, final Inet4Address ipv4Addess, final AtomicLong uptime) {
        super(group, port);
        this.ipv4Addess = ipv4Addess;
        this.uptime = uptime;
    }

    private final Map<String, Object> retrieveInfoToBeElectedAsOrchestrator() {
        final Map<String, Object> infos = new HashMap<>();    
        final RuntimeMXBean runtimeMX = ManagementFactory.getRuntimeMXBean();
        this.uptime.set(runtimeMX.getUptime());
        infos.put(UPTIME_KEY, this.uptime);
        infos.put(OCTET_KEY, this.ipv4Addess.getAddress()[3]);
        return infos;
    }

    private final void publishInfoToBeElectedAsOrchestrator(final Map<String, Object> infos) {
        final StringBuilder ballotBuilder = new StringBuilder(MessageType.VOTE.name())
        .append(":")
        .append(UPTIME_KEY)
        .append("=")
        .append(infos.get(UPTIME_KEY))
        .append(",")
        .append(OCTET_KEY)
        .append("=")
        .append(infos.get(OCTET_KEY));
 
        final String voteMessage = ballotBuilder.toString();
        try {
            MulticastEmitter.broadcast(this.group, this.port, voteMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute() throws IOException {
        super.execute();
        final Map<String, Object> infos = this.retrieveInfoToBeElectedAsOrchestrator();
        if (infos.size() == 2 && infos.containsKey(UPTIME_KEY) && infos.containsKey(OCTET_KEY)) {
            this.publishInfoToBeElectedAsOrchestrator(infos);
        }
    }
}
