package io.nirahtech.petvet.esp;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.DatagramPacket;
import java.net.Inet4Address;
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.nirahtech.petvet.esp.commands.Command;
import io.nirahtech.petvet.esp.commands.CommandFactory;

public class Sketch implements Program {
    private static final byte[] NETWORK_MASK = { (byte) 192, (byte) 168 };

    private AtomicReference<Mode> mode = new AtomicReference<>(Mode.NATIVE_NODE);

    private MulticastSocket multicastSocketForReception;
    private final byte[] incommingMessagesBuffer = new byte[256];

    private final InetAddress group;
    private final int port;

    private boolean isRunning = false;
    private AtomicLong uptime = new AtomicLong(0);

    private Inet4Address ip;

    private LocalDateTime lastScanExecutionOrder = null;
    private LocalDateTime lastSendedOrchestratorAvailabilityRequest = null;
    private Duration intervalBetweenEachScans = Duration.ofSeconds(10);
    private Duration intervalBetweenEachOrchestratorAvailabilityRequests = Duration.ofSeconds(5);

    public Sketch(final InetAddress group, final int port) {
        this.group = group;
        this.port = port;
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
     * Retrieves an incoming message from the multicast socket within a specified
     * timeout period.
     * <p>
     * This method sets the timeout for the multicast socket to the provided value
     * in milliseconds.
     * It then continuously attempts to receive a datagram packet until either a
     * valid message is received
     * or the timeout period elapses. If a valid message is received (i.e., it is
     * non-null and non-empty),
     * it is returned as an {@link Optional} containing the message. If the timeout
     * period elapses without
     * receiving a valid message, an empty {@link Optional} is returned.
     * </p>
     * <p>
     * If an I/O error occurs while receiving the datagram packet, an
     * {@link IOException} is caught and
     * the stack trace is printed. If a {@link SocketTimeoutException} occurs, the
     * method stops attempting
     * to receive messages and returns the current state of {@code receivedMessage}.
     * </p>
     * 
     * @param timeoutInMilliseconds the timeout period in milliseconds for receiving
     *                              a message.
     * @return an {@link Optional} containing the received message, or an empty
     *         {@link Optional} if no
     *         message is received within the timeout period.
     * @throws IOException if an I/O error occurs when setting the socket timeout or
     *                     receiving the datagram packet.
     */
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

    /**
     * Retrieves all incoming messages of a specific type within a specified timeout
     * period.
     * <p>
     * This method continuously attempts to retrieve messages of the specified type
     * from the multicast socket
     * until the timeout period elapses. It accumulates all messages of the
     * specified type into a collection
     * and returns them as a {@link Stream} of strings.
     * </p>
     * <p>
     * The method calculates the remaining timeout for each attempt to retrieve a
     * specific message, ensuring
     * that the total operation respects the initial timeout period. If a message of
     * the required type is
     * received, it is added to the collection of received messages.
     * </p>
     * 
     * @param requiredMessageType   the type of messages to be retrieved.
     * @param timeoutInMilliseconds the total timeout period in milliseconds for
     *                              retrieving messages.
     * @return a {@link Stream} of strings containing all messages of the specified
     *         type received within the timeout period.
     */
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

    /**
     * Retrieves a specific incoming message of the required type within a specified
     * timeout period.
     * <p>
     * This method attempts to retrieve messages from the multicast socket until a
     * message of the specified type
     * is found or the timeout period elapses. It continuously calls
     * {@link #retrieveIncommingMessage(int)} with
     * the remaining timeout to receive messages.
     * </p>
     * <p>
     * If a message is received, it is checked to see if it matches the required
     * message type. If a matching
     * message is found, it is returned as an {@link Optional} containing the
     * message. If no matching message is
     * found within the timeout period, an empty {@link Optional} is returned.
     * </p>
     * <p>
     * If a received message cannot be parsed to a {@link MessageType}, an
     * {@link IllegalArgumentException} is
     * caught and its stack trace is printed.
     * </p>
     * 
     * @param requiredMessageType   the type of message to be retrieved.
     * @param timeoutInMilliseconds the total timeout period in milliseconds for
     *                              retrieving the message.
     * @return an {@link Optional} containing the received message of the specified
     *         type, or an empty {@link Optional}
     *         if no such message is received within the timeout period.
     */
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

    /**
     * Decodes a vote message into a map of host uptimes.
     * <p>
     * This method parses a vote message, which is expected to be a comma-separated
     * string of key-value pairs.
     * Each key-value pair represents a host's identifier (as a byte) and its uptime
     * (as a long). The method
     * splits the message into individual entries, further splits each entry into a
     * key and a value, and adds
     * these to a map. The resulting map associates each host's identifier with its
     * uptime.
     * </p>
     * <p>
     * If an entry does not contain exactly two parts (a key and a value), it is
     * ignored.
     * </p>
     * 
     * @param message the vote message to decode, in the format
     *                "host1=uptime1,host2=uptime2,...".
     * @return a {@link Map} where the keys are host identifiers (bytes) and the
     *         values are their uptimes (longs).
     */
    private final Map<Byte, Long> decodeVote(final String message) {
        final Map<Byte, Long> uptimesByHost = new HashMap<>();

        final String[] entries = message.split(",");
        for (String entry : entries) {
            final String[] keyValue = entry.split("=");
            if (keyValue.length == 2) {
                final byte host = Byte.parseByte(keyValue[0].strip());
                final long uptime = Long.parseLong(keyValue[1].strip());
                uptimesByHost.put(host, uptime);
            }
        }
        return uptimesByHost;
    }

    /**
     * Runs the job for orchestrator selection.
     * <p>
     * This method retrieves all incoming VOTE messages within a specified timeout,
     * decodes the messages to extract
     * host uptimes, and aggregates the votes into a map of candidacies. It then
     * creates and executes a command to
     * analyze the votes and elect an orchestrator.
     * </p>
     * <p>
     * The method handles the retrieval, decoding, and aggregation of vote messages
     * and ensures that the command
     * for analyzing votes is executed. Any IO exceptions encountered during command
     * execution are caught and
     * their stack traces are printed.
     * </p>
     */
    private final void runJobForOchestratorSelection() {

        final Collection<Map<Byte, Long>> voteMessagesList = retrieveAllSpecificIncommingMessages(MessageType.VOTE,
                2_000)
                .map(message -> message.split(":")[1])
                .map(message -> decodeVote(message))
                .collect(Collectors.toList());
        final Map<Byte, Long> candidacies = new HashMap<>();
        voteMessagesList.forEach(map -> {
            final Optional<Map.Entry<Byte, Long>> vote = map.entrySet().stream().findFirst();
            if (vote.isPresent()) {
                candidacies.put(vote.get().getKey(), vote.get().getValue());
            }
        });
        final Command command = CommandFactory.createAnalyseVotesToElectOrchestratorCommand(group, port, this.mode,
                this.ip,
                candidacies);
        try {
            command.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs the job for orchestrator election.
     * <p>
     * This method initiates the process for orchestrator election by first checking
     * for a CHALLENGE_ORCHESTRATOR message
     * within a specified timeout. If such a message is present, it retrieves the
     * system uptime and broadcasts a VOTE
     * message containing the current node's IP address and uptime. It then proceeds
     * to run the job for orchestrator
     * selection to analyze the received votes and elect an orchestrator.
     * </p>
     * <p>
     * The method handles the retrieval of CHALLENGE_ORCHESTRATOR message, updates
     * the node's uptime, broadcasts the
     * VOTE message, and triggers the job for orchestrator selection. Any IO
     * exceptions encountered during message
     * broadcasting are caught and their stack traces are printed.
     * </p>
     */

    private final void runJobForOrchestratorElection() {
        final Optional<String> startChallengeMessage = this
                .retrieveSpecificIncommingMessage(MessageType.CHALLENGE_ORCHESTRATOR, 2_000);
        if (startChallengeMessage.isPresent()) {
            final RuntimeMXBean runtimeMX = ManagementFactory.getRuntimeMXBean();
            this.uptime.set(runtimeMX.getUptime());
        }
        final String voteMessage = String.format("%s:%s=%s", MessageType.VOTE.name(), this.ip.getAddress()[3],
                this.uptime.get());
        try {
            MulticastEmitter.broadcast(this.group, this.port, voteMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.runJobForOchestratorSelection();

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
        System.out.println("Running ORCHESTRATOR TASK");
        this.sendAnswerToTheIsOrchestratorAvailableMessage();
        this.sendScanNowMessageIfRequired();
        this.triggerNativeTask();
    }

    /**
     * Sends an answer to check if the orchestrator is available.
     * <p>
     * This method retrieves a specific incoming message of type
     * IS_ORCHESTRATOR_AVAILABLE with a timeout of 100 milliseconds.
     * If such a message is present, it creates a command to check the availability
     * of the orchestrator using
     * {@link CommandFactory#createCheckIfOrchestratorIsAvailableCommand(InetAddress, int, Mode)}.
     * </p>
     * <p>
     * The command is executed to perform the check, handling IOExceptions by
     * printing their stack traces.
     * </p>
     */
    private final void sendAnswerToTheIsOrchestratorAvailableMessage() {
        final Optional<String> message = this.retrieveSpecificIncommingMessage(MessageType.IS_ORCHESTRATOR_AVAILABLE,
                100);
        if (message.isPresent()) {
            final Command command = CommandFactory.createCheckIfOrchestratorIsAvailableCommand(this.group, this.port,
                    this.mode.get());
            try {
                command.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends an answer in response to the SCAN_NOW message.
     * <p>
     * This method retrieves a specific incoming message of type SCAN_NOW with a
     * timeout of 100 milliseconds.
     * If such a message is present, it creates a command to execute a scan now
     * using {@link CommandFactory#createScanNowCommand(InetAddress, int)}.
     * </p>
     * <p>
     * The command is executed to perform the scan, handling IOExceptions by
     * printing their stack traces.
     * </p>
     */
    private final void sendAnswerToTheScanNowMessage() {
        final Optional<String> message = this.retrieveSpecificIncommingMessage(MessageType.SCAN_NOW, 100);
        if (message.isPresent()) {
            final Command command = CommandFactory.createScanNowCommand(group, port);
            try {
                command.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends an answer in response to the CHALLENGE_ORCHESTRATOR message.
     * <p>
     * This method retrieves a specific incoming message of type
     * CHALLENGE_ORCHESTRATOR with a timeout of 100 milliseconds.
     * If such a message is present, it creates a command to challenge and elect an
     * orchestrator using
     * {@link CommandFactory#createChallengeToElectOrchestratorCommand(InetAddress, int, Inet4Address, AtomicLong)}.
     * </p>
     * <p>
     * The command is executed to perform the orchestrator election process,
     * handling IOExceptions by printing their stack traces.
     * </p>
     */
    private final void sendAnwserToTheChallengeOrchestratorMessage() {
        final Optional<String> message = this.retrieveSpecificIncommingMessage(MessageType.CHALLENGE_ORCHESTRATOR, 100);
        if (message.isPresent()) {
            final Command command = CommandFactory.createChallengeToElectOrchestratorCommand(this.group, this.port,
                    this.ip, this.uptime);
            try {
                command.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks the availability of the orchestrator node.
     * <p>
     * This method checks if an orchestrator availability request has been sent
     * recently.
     * If no request has been sent yet (null value for
     * {@code lastSendedOrchestratorAvailabilityRequest}),
     * it sends an availability request by invoking
     * {@link #askIfOrchestratorIsAvailable()}.
     * </p>
     * <p>
     * If a request has been sent, it checks if the interval since the last request
     * exceeds
     * {@code intervalBetweenEachOrchestratorAvailabilityRequests}. If it does, it
     * sends another
     * availability request using {@link #askIfOrchestratorIsAvailable()}.
     * </p>
     */
    private final void checkOrchestratorAvailability() {
        if (Objects.isNull(this.lastSendedOrchestratorAvailabilityRequest)) {
            this.askIfOrchestratorIsAvailable();
        } else {
            if (LocalDateTime.now().isAfter(this.lastSendedOrchestratorAvailabilityRequest
                    .plus(this.intervalBetweenEachOrchestratorAvailabilityRequests.toMillis(), ChronoUnit.MILLIS))) {
                this.askIfOrchestratorIsAvailable();
            }
        }
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
        System.out.println("Running NATIVE TASK");

        // SCAN_NOW
        this.sendAnswerToTheScanNowMessage();

        // IS_ORCHESTRATOR_AVAILABLE
        this.sendAnswerToTheIsOrchestratorAvailableMessage();

        // CHALLENGE_ORCHESTRATOR
        this.sendAnwserToTheChallengeOrchestratorMessage();

        // Check orchestrator availability
        this.checkOrchestratorAvailability();
    }

    /**
     * Runs the process for orchestrator election.
     * <p>
     * This method checks for the availability of an orchestrator node by retrieving
     * a specific message,
     * {@link MessageType#ORCHESTRATOR_AVAILABLE}, within a given timeout period.
     * If no message is received, it initiates the process to challenge the
     * orchestrator by broadcasting
     * {@link MessageType#CHALLENGE_ORCHESTRATOR} message and subsequently runs the
     * orchestrator election
     * process using {@link #runJobForOrchestratorElection()}.
     * </p>
     */
    private final void runJobForOrchestrationElectionProcess() {
        final Optional<String> messageForAvailableOrchestrator = this
                .retrieveSpecificIncommingMessage(MessageType.ORCHESTRATOR_AVAILABLE, 2_000);
        if (!messageForAvailableOrchestrator.isPresent()) {
            try {
                MulticastEmitter.broadcast(this.group, this.port, MessageType.CHALLENGE_ORCHESTRATOR.name());
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.runJobForOrchestratorElection();
        }
    }

    /**
     * Broadcasts a message to check the availability of the orchestrator.
     * <p>
     * This method broadcasts {@link MessageType#IS_ORCHESTRATOR_AVAILABLE} message
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
        try {
            MulticastEmitter.broadcast(this.group, this.port, MessageType.IS_ORCHESTRATOR_AVAILABLE.name());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.lastSendedOrchestratorAvailabilityRequest = LocalDateTime.now();
        this.runJobForOrchestrationElectionProcess();
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
        this.multicastSocketForReception = new MulticastSocket(this.port);
        this.multicastSocketForReception.joinGroup(this.group);
        this.retrieveIpAddress();
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
