package io.nirahtech.petvet.messaging.util;

public enum EmitterMode implements ModeChecker {
    NATIVE_NODE {
        @Override
        public boolean isNativeMode() {
            return true;
        }
        @Override
        public boolean isOrchestratorMode() {
            return false;
        }
    },
    ORCHESTRATOR_NODE {
        @Override
        public boolean isNativeMode() {
            return false;
        }
        @Override
        public boolean isOrchestratorMode() {
            return true;
        }
    };
}
