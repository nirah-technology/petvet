package io.nirahtech.esp;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import io.nirahtech.esp.commands.Command;
import io.nirahtech.esp.commands.CommandFactory;

public class Sketch implements Program {

    private static final String UPTIME_KEY = "uptime";
    private static final String OCTET_KEY = "octet";
    private static final byte[] networkId = { (byte) 192, (byte) 168, (byte) 20 };

    private Mode mode = Mode.NATIVE_NODE;

    private MulticastSocket multicastSocketForReception;
    private final byte[] incommingMessagesBuffer = new byte[256];

    private final InetAddress group;
    private final int port;

    private boolean isFirstLoopCycle = true;

    private boolean isRunning = false;
    private AtomicLong uptime = new AtomicLong(0);
    private byte lastOctetOfIp = 0;

    private LocalDateTime lastScanExecutionOrder = null;
    private LocalDateTime lastSendedOrchestratorAvailabilityRequest = null;
    private Duration intervalBetweenEachScans = Duration.ofSeconds(10);
    private Duration intervalBetweenEachOrchestratorAvailabilityRequests = Duration.ofSeconds(5);

    public Sketch(final InetAddress group, final int port) {
        this.group = group;
        this.port = port;
    }

    private final void triggerOrchestratorTask() {
        System.out.println("Running ORCHESTRATOR TASK");
        final Optional<String> availabilityRequest = this
                .retrieveSpecificIncommingMessage(MessageType.IS_ORCHESTRATOR_AVAILABLE, 200);

        if (this.mode == Mode.ORCHESTRATOR_NODE) {
            availabilityRequest.ifPresent(request -> {
                final Command command = CommandFactory.createCheckIfOrchestratorIsAvailableCommand(this.group,
                        this.port, this.mode);
                try {
                    command.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            if (Objects.isNull(this.lastScanExecutionOrder)) {
                System.out.println("Scan is required!");
                try {
                    MulticastEmitter.broadcast(this.group, this.port, MessageType.SCAN_NOW.name());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                this.lastScanExecutionOrder = LocalDateTime.now();
            } else {
                if (LocalDateTime.now().isAfter(this.lastScanExecutionOrder
                        .plus(this.intervalBetweenEachScans.toMillis(), ChronoUnit.MILLIS))) {
                    System.out.println("Another Scan is required!");
                    try {
                        MulticastEmitter.broadcast(this.group, this.port, MessageType.SCAN_NOW.name());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    this.lastScanExecutionOrder = LocalDateTime.now();
                }
            }

        }
        this.triggerNativeTask();
    }


    private final void askIfOrchestratorIsAvailable() {
        try {
            MulticastEmitter.broadcast(this.group, this.port, MessageType.IS_ORCHESTRATOR_AVAILABLE.name());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.lastSendedOrchestratorAvailabilityRequest = LocalDateTime.now();
        this.runJobForOchestratorAvailibilityChecking();
    }

    private final void triggerNativeTask() {
        System.out.println("Running NATIVE TASK");

        // SCAN_NOW
        Optional<String> message = this.retrieveSpecificIncommingMessage(MessageType.SCAN_NOW, 100);
        if (message.isPresent()) {
            final Command command = CommandFactory.createScanNowCommand(group, port);
            try {
                command.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // IS_ORCHESTRATOR_AVAILABLE
        message = this.retrieveSpecificIncommingMessage(MessageType.IS_ORCHESTRATOR_AVAILABLE, 100);
        if (message.isPresent()) {
            final Command command = CommandFactory.createCheckIfOrchestratorIsAvailableCommand(this.group, this.port,
                    this.mode);
            try {
                command.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // CHALLENGE_ORCHESTRATOR
        message = this.retrieveSpecificIncommingMessage(MessageType.CHALLENGE_ORCHESTRATOR, 100);
        if (message.isPresent()) {
            this.runJobForOrchestratorElection();

            // or

            final Command command = CommandFactory.createChallengeToElectOrchestratorCommand(this.group, this.port,
                    null, this.uptime);
            try {
                command.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Check orchestrator availability
        if (Objects.isNull(this.lastSendedOrchestratorAvailabilityRequest)) {
            this.askIfOrchestratorIsAvailable();
        } else {
            if (LocalDateTime.now().isAfter(this.lastSendedOrchestratorAvailabilityRequest
                    .plus(this.intervalBetweenEachOrchestratorAvailabilityRequests.toMillis(), ChronoUnit.MILLIS))) {
                this.askIfOrchestratorIsAvailable();
            }
        }
    }

    private final Optional<String> retrieveIncommingMessage(final int timeoutInMilliseconds) {
        Optional<String> receivedMessage = Optional.empty();
        try {
            this.multicastSocketForReception.setSoTimeout(timeoutInMilliseconds);
            while (true) {
                final DatagramPacket incommingUdpPacket = new DatagramPacket(this.incommingMessagesBuffer,
                        this.incommingMessagesBuffer.length);
                try {
                    this.multicastSocketForReception.receive(incommingUdpPacket);
                    final String message = new String(incommingUdpPacket.getData(), 0, incommingUdpPacket.getLength());
                    if (Objects.nonNull(message) && !message.isEmpty()) {
                        receivedMessage = Optional.of(message);
                        break;
                    }
                } catch (SocketTimeoutException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receivedMessage;
    }

    private final Stream<String> retrieveAllSpecificIncommingMessages(final MessageType requiredMessageType,
            final int timeoutInMilliseconds) {
        final Collection<String> receivedMessages = new ArrayList<>();
        long endTime = System.currentTimeMillis() + timeoutInMilliseconds;
        while (System.currentTimeMillis() < endTime) {
            int timeoutInMillisLeft = (int) (endTime - System.currentTimeMillis());
            this.retrieveSpecificIncommingMessage(requiredMessageType, timeoutInMillisLeft)
                    .ifPresent(receivedMessages::add);
        }
        return receivedMessages.stream();
    }

    private final Optional<String> retrieveSpecificIncommingMessage(final MessageType requiredMessageType,
            final int timeoutInMilliseconds) {
        long endTime = System.currentTimeMillis() + timeoutInMilliseconds;
        Optional<String> receivedSpecificMessage = Optional.empty();
        while (System.currentTimeMillis() < endTime) {
            int timeoutInMillisLeft = (int) (endTime - System.currentTimeMillis());
            Optional<String> receivedMessage = this.retrieveIncommingMessage(timeoutInMillisLeft);
            if (receivedMessage.isPresent()) {
                final String message = receivedMessage.get().split(":")[0];
                try {
                    MessageType receivedMessageType = MessageType.valueOf(message);
                    if (Objects.nonNull(receivedMessageType) && receivedMessageType.equals(requiredMessageType)) {
                        receivedSpecificMessage = receivedMessage;
                        break;
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return receivedSpecificMessage;
    }

    private final Map<String, Object> loadJson(final String message) {
        final Map<String, Object> map = new HashMap<>();

        final String[] entries = message.split(",");
        for (String entry : entries) {
            final String[] keyValue = entry.split("=");
            if (keyValue.length == 2) {
                map.put(keyValue[0].strip(), keyValue[1].strip());
            }
        }
        return map;
    }

    private final void runJobForOchestratorSelection() {

        List<String> voteMessagesList = retrieveAllSpecificIncommingMessages(MessageType.VOTE, 2_000)
                .map(message -> message.split(":")[1])
                .collect(Collectors.toList());
        // Faire la convertion String -> Map.Enrey<Byte,Long>
        Command command = CommandFactory.createAnalyseVotesToElectOrchestratorCommand(group, port, null, null, null)
        command.execute();
    }

    private final void runJobForOchestratorAvailibilityChecking() {
        final Optional<String> messageForAvailableOrchestrator = this
                .retrieveSpecificIncommingMessage(MessageType.ORCHESTRATOR_AVAILABLE, 2_000);
        if (!messageForAvailableOrchestrator.isPresent()) {
            try {
                MulticastEmitter.broadcast(this.group, this.port, MessageType.CHALLENGE_ORCHESTRATOR.name());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            this.runJobForOrchestratorElection();
        }
    }

    private final void runJobForOrchestratorElection() {
        final Map<String, Object> json = new HashMap<>();
        final Optional<String> startChallengeMessage = this
                .retrieveSpecificIncommingMessage(MessageType.CHALLENGE_ORCHESTRATOR, 2_000);
        if (startChallengeMessage.isPresent()) {
            final RuntimeMXBean runtimeMX = ManagementFactory.getRuntimeMXBean();
            this.uptime.set(runtimeMX.getUptime());
            json.put(UPTIME_KEY, this.uptime);
            if (this.lastOctetOfIp == 0) {
                try {
                    final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                    NICS: while (networkInterfaces.hasMoreElements()) {
                        final NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
                        final Enumeration<InetAddress> ipAddresses = networkInterface.getInetAddresses();
                        IPS_FOR_NIC: while (ipAddresses.hasMoreElements()) {
                            final InetAddress ipAddress = (InetAddress) ipAddresses.nextElement();
                            final byte[] address = ipAddress.getAddress();
                            if ((address[0] == networkId[0]) && (address[1] == networkId[1])
                                    && (address[2] == networkId[2])) {
                                this.lastOctetOfIp = address[3];
                                break NICS;
                            }
                        }
                    }
                } catch (SocketException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            json.put(OCTET_KEY, this.lastOctetOfIp);

        }
        if (json.size() == 2) {
            final String voteMessage = String.format("%s:%s=%s,%s=%s", MessageType.VOTE.name(), UPTIME_KEY,
                    json.get(UPTIME_KEY), OCTET_KEY, json.get(OCTET_KEY));
            try {
                MulticastEmitter.broadcast(this.group, this.port, voteMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.runJobForOchestratorSelection();

    }

    private final void setup() throws IOException {
        this.multicastSocketForReception = new MulticastSocket(this.port);
        this.multicastSocketForReception.joinGroup(this.group);
        this.askIfOrchestratorIsAvailable();
    }

    private final void loop() {
        switch (this.mode) {
            case ORCHESTRATOR_NODE:
                this.triggerOrchestratorTask();
                break;
            default:
                this.triggerNativeTask();
                break;
        }
    }

    @Override
    public void run() {
        System.out.println("Running the sketch...");
        // Prevent for multiples executions
        if (!this.isRunning) {
            System.out.println("Sketch not yet running...");
            // The following boilerplate is similar of an Arduino Activity (setup, loop).
            this.isRunning = true;

            // Setup the node
            try {
                System.out.println("SETUP task...");
                this.setup();
            } catch (IOException e) {
                this.isRunning = false;
                e.printStackTrace();
            }

            while (this.isRunning) {
                System.out.println("LOOP task...");
                // Execute the main process
                this.loop();
            }
        }
    }

}
