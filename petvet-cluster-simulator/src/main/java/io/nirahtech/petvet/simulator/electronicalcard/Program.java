package io.nirahtech.petvet.simulator.electronicalcard;

interface Program extends Runnable {
    void kill();
    boolean isRunning();
}
