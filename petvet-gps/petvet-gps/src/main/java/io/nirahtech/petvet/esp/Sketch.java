package io.nirahtech.petvet.esp;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import io.nirahtech.petvet.esp.brokers.MessageBroker;
import io.nirahtech.petvet.esp.brokers.UDPMessageBroker;
import io.nirahtech.petvet.esp.commands.Command;
import io.nirahtech.petvet.esp.commands.CommandFactory;
import io.nirahtech.petvet.esp.messages.ChallengeOrchestratorMessage;
import io.nirahtech.petvet.esp.messages.IsOrchestratorAvailableMessage;
import io.nirahtech.petvet.esp.messages.MessageType;
import io.nirahtech.petvet.esp.messages.OrchestratorAvailableMessage;
import io.nirahtech.petvet.esp.messages.ScanNowMessage;
import io.nirahtech.petvet.esp.messages.VoteMessage;
import io.nirahtech.petvet.esp.scanners.Scanner;
import io.nirahtech.petvet.esp.scanners.WifiScanner;

public class Sketch implements Program {
    private static final byte[] NETWORK_MASK = { (byte) 192, (byte) 168 };

    private final UUID id = UUID.randomUUID();
    private AtomicReference<Mode> mode = new AtomicReference<>(Mode.NATIVE_NODE);

    private final Scanner scanner;
    private boolean isRunning = false;
    private AtomicLong uptime = new AtomicLong(0);

    private NetworkInterface networkInterface;
    private InetAddress ip;

    private LocalDateTime lastScanExecutionOrder = LocalDateTime.now();
    private LocalDateTime lastSendedOrchestratorAvailabilityRequest = LocalDateTime.now();
    private LocalDateTime lastReceivedOrchestratorAvailabilityResponse = LocalDateTime.now();
    private Duration intervalBetweenEachScans = Duration.ofSeconds(10);
    private Duration intervalBetweenEachOrchestratorAvailabilityRequests = Duration.ofSeconds(5);

    private final MessageBroker messageBroker;

    public Sketch(final InetAddress group, final int port) {
        this.retrieveIpAddress();
        this.messageBroker = UDPMessageBroker.getOrCreate(this.networkInterface, group, port);
        this.scanner = new WifiScanner();
    }

    /**
     * Retrieves the IP address of the current machine that matches the predefined
     * network mask.
     * <p>
     * This method iterates through all available network interfaces and their
     * associated IP addresses,
     * and finds the first IP address that matches the specified network mask
     * defined by {@link #NETWORK_MASK}.
     * When a matching IP address is found, it sets the last octet of the IP address
     * to {@link #lastOctetOfIp}
     * and the full IP address to {@link #ip}.
     * <p>
     * This method is designed to work with IPv4 addresses.
     * </p>
     * <p>
     * If an error occurs while accessing the network interfaces, a
     * {@link SocketException} is caught
     * and the stack trace is printed.
     * </p>
     * 
     * @throws SocketException if an I/O error occurs when accessing the network
     *                         interfaces.
     */
    private final void retrieveIpAddress() {
        try {
            final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NICS: while (networkInterfaces.hasMoreElements()) {
                final NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
                final Enumeration<InetAddress> ipAddresses = networkInterface.getInetAddresses();
                IPS_FOR_NIC: while (ipAddresses.hasMoreElements()) {
                    final InetAddress ipAddress = (InetAddress) ipAddresses.nextElement();
                    final byte[] address = ipAddress.getAddress();
                    if ((address[0] == NETWORK_MASK[0]) && (address[1] == NETWORK_MASK[1])) {
                        this.networkInterface = networkInterface;
                        this.ip = (Inet4Address) ipAddress;
                        break NICS;
                    }
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Sends a SCAN_NOW message if required.
     * <p>
     * This method checks if a scan execution order is required based on the last
     * execution time and interval
     * between scans. If no scan has been executed yet (null last execution time),
     * it broadcasts a SCAN_NOW
     * message and updates the last execution time to the current timestamp. If
     * another scan is due based on
     * the configured interval, it broadcasts another SCAN_NOW message and updates
     * the last execution time
     * accordingly.
     * </p>
     * <p>
     * The method broadcasts SCAN_NOW messages using MulticastEmitter and handles
     * IOExceptions by printing
     * their stack traces.
     * </p>
     */
    private final void sendScanNowMessageIfRequired() {
        if (Objects.isNull(this.lastScanExecutionOrder)) {
            System.out.println("Scan is required!");
            final ScanNowMessage message = ScanNowMessage.create(this.id, this.ip,
                    this.mode.get().equals(Mode.ORCHESTRATOR_NODE));
            try {
                this.messageBroker.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.lastScanExecutionOrder = LocalDateTime.now();
        } else {
            if (LocalDateTime.now().isAfter(this.lastScanExecutionOrder
                    .plus(this.intervalBetweenEachScans.toMillis(), ChronoUnit.MILLIS))) {
                System.out.println("Another Scan is required!");
                final ScanNowMessage message = ScanNowMessage.create(this.id, this.ip,
                        this.mode.get().equals(Mode.ORCHESTRATOR_NODE));
                try {
                    this.messageBroker.send(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.lastScanExecutionOrder = LocalDateTime.now();
            }
        }
    }

    /**
     * Triggers the orchestrator task.
     * <p>
     * This method initiates the tasks required for orchestrator functionality. It
     * prints a log message indicating
     * the start of the orchestrator task. It sequentially executes the following
     * tasks:
     * </p>
     * <ol>
     * <li>Sends a response to check if the orchestrator is available by calling
     * {@link #sendAnswerToTheIsOrchestratorAvailableMessage()}.</li>
     * <li>Sends a SCAN_NOW message if required by calling
     * {@link #sendScanNowMessageIfRequired()}.</li>
     * <li>Triggers native tasks by calling {@link #triggerNativeTask()}.</li>
     * </ol>
     * <p>
     * Each task is executed in sequence to manage orchestrator functionality and
     * system tasks.
     * </p>
     */
    private final void triggerOrchestratorTask() {
        this.sendScanNowMessageIfRequired();
        this.triggerNativeTask();
    }

    /**
     * Executes the native task operations.
     * <p>
     * This method runs several operations including sending responses to specific
     * messages:
     * <ul>
     * <li>{@link #sendAnswerToTheScanNowMessage()} - Sends a response to the
     * SCAN_NOW message.</li>
     * <li>{@link #sendAnswerToTheIsOrchestratorAvailableMessage()} - Sends a
     * response to the IS_ORCHESTRATOR_AVAILABLE message.</li>
     * <li>{@link #sendAnwserToTheChallengeOrchestratorMessage()} - Sends a response
     * to the CHALLENGE_ORCHESTRATOR message.</li>
     * </ul>
     * Additionally, it checks the availability of the orchestrator node using
     * {@link #checkOrchestratorAvailability()}.
     * </p>
     */
    private final void triggerNativeTask() {
        try {
            this.messageBroker.receive();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (this.lastSendedOrchestratorAvailabilityRequest.isBefore(LocalDateTime.now().minus(this.intervalBetweenEachOrchestratorAvailabilityRequests.toMillis(), ChronoUnit.MILLIS))) {
            this.askIfOrchestratorIsAvailable();
        }

        if (this.lastReceivedOrchestratorAvailabilityResponse.isBefore(LocalDateTime.now().minus(20, ChronoUnit.SECONDS))) {
            this.mode.set(Mode.ORCHESTRATOR_NODE);
            OrchestratorAvailableMessage message = OrchestratorAvailableMessage.create(this.id, this.ip, this.mode.get().equals(Mode.ORCHESTRATOR_NODE));
            try {
                this.messageBroker.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Broadcasts a message to check the availability of the orchestrator.
     * <p>
     * This method broadcasts {@link MessageTypeOld#IS_ORCHESTRATOR_AVAILABLE}
     * message
     * to the multicast
     * group and port specified. It updates the timestamp
     * {@code lastSendedOrchestratorAvailabilityRequest}
     * to the current date and time upon sending the message. After broadcasting, it
     * triggers the
     * orchestration election process by calling
     * {@link #runJobForOrchestrationElectionProcess()}.
     * </p>
     */
    private final void askIfOrchestratorIsAvailable() {
        final IsOrchestratorAvailableMessage message = IsOrchestratorAvailableMessage.create(this.id, this.ip,
                this.mode.get().equals(Mode.ORCHESTRATOR_NODE));
        try {
            this.messageBroker.send(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.lastSendedOrchestratorAvailabilityRequest = LocalDateTime.now();
    }

    /**
     * Initializes and sets up the necessary components for the system.
     * <p>
     * This method initializes a {@link MulticastSocket} for reception on the
     * specified {@code port} and {@code group}.
     * It joins the multicast group and retrieves the IP address of the current
     * network interface. Additionally,
     * it broadcasts a message to check the availability of the orchestrator by
     * calling {@link #askIfOrchestratorIsAvailable()}.
     * </p>
     *
     * @throws IOException if an I/O error occurs while setting up the multicast
     *                     socket or joining the multicast group.
     */
    private final void setup() throws IOException {
        this.messageBroker.subscribe(MessageType.IS_ORCHESTRATOR_AVAILABLE, (message) -> {
            if (message instanceof IsOrchestratorAvailableMessage) {
                final IsOrchestratorAvailableMessage realMessage = (IsOrchestratorAvailableMessage) message;
                final Command command = CommandFactory.createCheckIfOrchestratorIsAvailableCommand(this.messageBroker,
                        this.id, this.ip, this.mode.get());
                try {
                    command.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        this.messageBroker.subscribe(MessageType.CHALLENGE_ORCHESTRATOR, (message) -> {
            if (message instanceof ChallengeOrchestratorMessage) {
                final ChallengeOrchestratorMessage realMessage = (ChallengeOrchestratorMessage) message;
                final RuntimeMXBean runtimeMX = ManagementFactory.getRuntimeMXBean();
                this.uptime.set(runtimeMX.getUptime());
                final Command command = CommandFactory.createChallengeToElectOrchestratorCommand(messageBroker, id,
                        this.ip, this.uptime);
                try {
                    command.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        this.messageBroker.subscribe(MessageType.SCAN_NOW, (message) -> {
            System.out.println("ok..");
            if (message instanceof ScanNowMessage) {
                final ScanNowMessage realMessage = (ScanNowMessage) message;
                final Command command = CommandFactory.createScanNowCommand(this.messageBroker, this.id, this.ip,
                        this.mode.get(), this.scanner);
                try {
                    System.out.println("I will scan...");
                    command.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        this.messageBroker.subscribe(MessageType.VOTE, (message) -> {
            if (message instanceof VoteMessage) {
                final VoteMessage realMessage = (VoteMessage) message;
                final Command command = CommandFactory.createAnalyseVotesToElectOrchestratorCommand(this.messageBroker,
                        this.id, this.ip,
                        this.mode, this.uptime.get(), Map.entry(realMessage.getLastIpByte(), realMessage.getUptime()));
                try {
                    command.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        this.messageBroker.subscribe(MessageType.ORCHESTRATOR_AVAILABLE, (message) -> {
            if (message instanceof OrchestratorAvailableMessage) {
                final OrchestratorAvailableMessage realMessage = (OrchestratorAvailableMessage) message;
                lastReceivedOrchestratorAvailabilityResponse = LocalDateTime.now();
                if (realMessage.getEmitter().equals(this.ip) && realMessage.getId().equals(this.id)) {
                    this.mode.set(Mode.ORCHESTRATOR_NODE);
                } else {
                    this.mode.set(Mode.NATIVE_NODE);
                }
                System.out.println("Switching to: " + this.mode.get().name());
            }
        });


        this.askIfOrchestratorIsAvailable();
    }

    /**
     * Executes the appropriate task based on the current operational mode.
     * <p>
     * This method checks the current mode retrieved from {@link #mode}. If the mode
     * is {@code ORCHESTRATOR_NODE},
     * it triggers tasks related to orchestrator functions by calling
     * {@link #triggerOrchestratorTask()}.
     * Otherwise, it executes native tasks by invoking {@link #triggerNativeTask()}.
     * </p>
     * <p>
     * The choice of task execution is determined by the operational mode of the
     * system.
     * </p>
     */
    private final void loop() {
        switch (this.mode.get()) {
            case ORCHESTRATOR_NODE:
                this.triggerOrchestratorTask();
                break;
            default:
                this.triggerNativeTask();
                break;
        }
    }

    /**
     * Starts the execution of the sketch in a loop until stopped.
     * <p>
     * This method simulates the behavior of an Arduino sketch with a setup phase
     * followed by a continuous loop.
     * It ensures that the sketch is only executed once to prevent multiple
     * executions concurrently.
     * </p>
     * <p>
     * The method first checks if the sketch is not already running. If not running,
     * it sets the {@code isRunning} flag
     * to {@code true} and proceeds with the setup phase by invoking
     * {@link #setup()} method.
     * </p>
     * <p>
     * Once setup is completed successfully, the method enters a continuous loop
     * ({@code while (this.isRunning)}) where
     * it repeatedly executes the main process by calling {@link #loop()}.
     * </p>
     * <p>
     * If any {@link IOException} occurs during setup, the method sets
     * {@code isRunning} to {@code false} and prints the
     * stack trace of the exception.
     * </p>
     */
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
                this.setup();
            } catch (IOException e) {
                this.isRunning = false;
                e.printStackTrace();
            }

            while (this.isRunning) {
                // Execute the main process
                this.loop();
            }
        }
    }

}
