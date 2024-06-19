package io.nirahtech.petvet.messaging.util;

interface ModeChecker {
    boolean isOrchestratorMode();
    boolean isNativeMode();
}
