package io.nirahtech.petvet.simulator.electronicalcard;

import java.io.IOException;
import java.net.MulticastSocket;

public abstract class Sketch implements Program {
    private boolean isRunning = false;

    protected Sketch() {
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
    protected abstract void setup();

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
    protected abstract void loop();

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
    public final void run() {
        System.out.println("Running the sketch...");
        // Prevent for multiples executions
        if (!this.isRunning()) {
            System.out.println("Sketch not yet running...");
            // The following boilerplate is similar of an Arduino Activity (setup, loop).
            this.isRunning = true;

            // Setup the node
            this.setup();

            while (this.isRunning()) {
                // Execute the main process
                this.loop();
            }
        }
    }

    @Override
    public final boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public final void kill() {
        this.isRunning  = false;
    }

}
