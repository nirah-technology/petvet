package io.nirahtech.petvet.simulator.electronicalcard;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import io.nirahtech.petvet.messaging.brokers.MessageBroker;
import io.nirahtech.petvet.messaging.brokers.UDPMessageBroker;
import io.nirahtech.petvet.messaging.messages.ChallengeOrchestratorMessage;
import io.nirahtech.petvet.messaging.messages.IsOrchestratorAvailableMessage;
import io.nirahtech.petvet.messaging.messages.MessageType;
import io.nirahtech.petvet.messaging.messages.OrchestratorAvailableMessage;
import io.nirahtech.petvet.messaging.messages.ScanNowMessage;
import io.nirahtech.petvet.messaging.messages.VoteMessage;
import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;
import io.nirahtech.petvet.simulator.electronicalcard.commands.Command;
import io.nirahtech.petvet.simulator.electronicalcard.commands.CommandFactory;
import io.nirahtech.petvet.simulator.electronicalcard.scanners.Scanner;
import io.nirahtech.petvet.simulator.electronicalcard.scanners.WifiScanner;

public class Sketch implements Program {

    private final UUID id = UUID.randomUUID();
    private final AtomicReference<EmitterMode> mode = new AtomicReference<>();

    private final Scanner scanner;
    private boolean isRunning = false;
    private final AtomicLong uptime = new AtomicLong(0);

    private final NetworkInterface networkInterface;
    private final InetAddress ip;
    private final MacAddress mac;

    private LocalDateTime lastScanExecutionOrder = null;
    private LocalDateTime lastReceivedOrchestratorAvailabilityResponse = LocalDateTime.now();
    private LocalDateTime lastReceivedScanExecutionOrder = LocalDateTime.now();
    private LocalDateTime lastSendedHeartBeat = LocalDateTime.now();
    private final Duration intervalBetweenEachScans;
    private final Duration intervalBetweenEachHeartBeat;

    private final MessageBroker messageBroker;
    private final Set<MacAddress> neighborsBSSID;

    public Sketch(final NetworkInterface networkInterface, final MacAddress mac, final InetAddress ip,  final Configuration configuration, final Set<MacAddress> neighborsBSSID) {
        this.messageBroker = UDPMessageBroker.newInstance();
        this.neighborsBSSID = neighborsBSSID;
        this.networkInterface = networkInterface;
        this.mac = mac;
        this.ip = ip;
        this.mode.set(configuration.mode());
        this.intervalBetweenEachScans = configuration.scanInterval();
        this.intervalBetweenEachHeartBeat = configuration.heartBeatInterval();
        try {
            this.messageBroker.connect(this.networkInterface, configuration.multicastGroup(), configuration.multicastPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.scanner = new WifiScanner();
    }



    private final void sendScanNowRequest() {
        final ScanNowMessage message = ScanNowMessage.create(
            this.id, this.mac, this.ip, this.mode.get() 
        );
        try {
            this.messageBroker.send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.lastScanExecutionOrder = LocalDateTime.now();

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
            System.out.println(String.format("[%s] Scan is required!", this.id));
            this.sendScanNowRequest();
        } else {
            if (LocalDateTime.now().isAfter(this.lastScanExecutionOrder
                    .plus(this.intervalBetweenEachScans.toMillis(), ChronoUnit.MILLIS))) {
                System.out.println(String.format("[%s] Another scan must be executed!", this.id));
                this.sendScanNowRequest();
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

    private final void requestChallenge() {
        System.out.println(String.format("[%s] I want to elect a new orchestrator...", this.id));
        ChallengeOrchestratorMessage message = ChallengeOrchestratorMessage.create(id, mac, ip, this.mode.get());
        try {
            this.messageBroker.send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.lastReceivedOrchestratorAvailabilityResponse = LocalDateTime.now();
        this.lastReceivedScanExecutionOrder = this.lastReceivedOrchestratorAvailabilityResponse;
    }

    private final boolean isScanRequestInLate() {
        if (Objects.isNull(this.lastReceivedScanExecutionOrder)) {
            return false;
        }
        return LocalDateTime.now().isAfter(this.lastReceivedScanExecutionOrder.plus(this.intervalBetweenEachScans.plusSeconds(1)));
    }

    private final void sendHeartBeat() {
        final Command heartBeat = CommandFactory.createHeartBeatCommand(messageBroker, id, mac, ip, this.mode.get(), this.uptime);
        try {
            heartBeat.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.lastSendedHeartBeat = LocalDateTime.now();
    }

    private final boolean isHeartBeatInLate() {
        if (Objects.isNull(this.lastSendedHeartBeat)) {
            return false;
        }
        return LocalDateTime.now().isAfter(this.lastSendedHeartBeat.plus(this.intervalBetweenEachHeartBeat));
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

        if (this.isScanRequestInLate()) {
            System.out.println(String.format("[%s] It's seem that scan request is in late...", this.id));
            this.requestChallenge();
        }

        if (this.isHeartBeatInLate()) {
            System.out.println(String.format("[%s] I'm alive!", this.id));
            this.sendHeartBeat();
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
        System.out.println(String.format("[%s] I want to know if an orchestrator is available...", this.id));
        final IsOrchestratorAvailableMessage message = IsOrchestratorAvailableMessage.create(id, mac, ip, mode.get());
        try {
            this.messageBroker.send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private final void setup() {
        this.messageBroker.subscribe(MessageType.IS_ORCHESTRATOR_AVAILABLE, (message) -> {
            if (message instanceof IsOrchestratorAvailableMessage) {
                final IsOrchestratorAvailableMessage realMessage = (IsOrchestratorAvailableMessage) message;
                final Command command = CommandFactory.createCheckIfOrchestratorIsAvailableCommand(this.messageBroker,
                id, mac, ip, mode.get());
                try {
                    command.execute();
                } catch (IOException e) {
                    System.err.println(String.format("[%s] Command execution failure: %s", this.id, e.getMessage()));
                }
            }
        });

        this.messageBroker.subscribe(MessageType.CHALLENGE_ORCHESTRATOR, (message) -> {
            if (message instanceof ChallengeOrchestratorMessage) {
                final ChallengeOrchestratorMessage realMessage = (ChallengeOrchestratorMessage) message;
                final RuntimeMXBean runtimeMX = ManagementFactory.getRuntimeMXBean();
                this.uptime.set(runtimeMX.getUptime());
                final Command command = CommandFactory.createChallengeToElectOrchestratorCommand(messageBroker, id, mac, ip, mode.get(), this.uptime);
                try {
                    command.execute();
                } catch (IOException e) {
                    System.err.println(String.format("[%s] Command execution failure: %s", this.id, e.getMessage()));
                }
            }
        });

        this.messageBroker.subscribe(MessageType.SCAN_NOW, (message) -> {
            if (message instanceof ScanNowMessage) {
                this.lastReceivedScanExecutionOrder = LocalDateTime.now();
                this.lastReceivedOrchestratorAvailabilityResponse = this.lastReceivedScanExecutionOrder;
                final ScanNowMessage realMessage = (ScanNowMessage) message;
                final Command command = CommandFactory.createScanNowCommand(this.messageBroker, realMessage.getScanId(), id, mac, ip, mode.get(), this.scanner, this.neighborsBSSID);
                try {
                    command.execute();
                } catch (IOException e) {
                    System.err.println(String.format("[%s] Command execution failure: %s", this.id, e.getMessage()));
                }
            }
        });

        this.messageBroker.subscribe(MessageType.VOTE, (message) -> {
            if (message instanceof VoteMessage) {
                final VoteMessage realMessage = (VoteMessage) message;
                final Command command = CommandFactory.createAnalyseVotesToElectOrchestratorCommand(this.messageBroker,
                id, mac, ip, mode, this.uptime.get(), Map.entry(realMessage.getLastIpByte(), realMessage.getUptime()));
                try {
                    command.execute();
                } catch (IOException e) {
                    System.err.println(String.format("[%s] Command execution failure: %s", this.id, e.getMessage()));
                }
            }
        });

        this.messageBroker.subscribe(MessageType.ORCHESTRATOR_AVAILABLE, (message) -> {
            if (message instanceof OrchestratorAvailableMessage) {
                final OrchestratorAvailableMessage realMessage = (OrchestratorAvailableMessage) message;
                lastReceivedOrchestratorAvailabilityResponse = LocalDateTime.now();
                if (realMessage.getEmitterIP().equals(this.ip) && realMessage.getEmitterID().equals(this.id)) {
                    this.mode.set(EmitterMode.ORCHESTRATOR_NODE);
                } else {
                    this.mode.set(EmitterMode.NATIVE_NODE);
                }
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
            this.setup();

            while (this.isRunning) {
                // Execute the main process
                this.loop();
            }
        }
    }

}
