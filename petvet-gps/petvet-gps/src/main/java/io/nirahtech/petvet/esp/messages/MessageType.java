package io.nirahtech.petvet.esp.messages;

import java.util.Optional;

public enum MessageType implements MessageParser {
    SCAN_NOW {
        @Override
        public Optional<ScanNowMessage> parse(String messageAsString) {
            return ScanNowMessage.parse(messageAsString);
        }
    },
    IS_ORCHESTRATOR_AVAILABLE {
        @Override
        public Optional<IsOrchestratorAvailableMessage> parse(String messageAsString) {
            return IsOrchestratorAvailableMessage.parse(messageAsString);
        }
    },
    ORCHESTRATOR_AVAILABLE {
        @Override
        public Optional<OrchestratorAvailableMessage> parse(String messageAsString) {
            return OrchestratorAvailableMessage.parse(messageAsString);
        }
    },
    CHALLENGE_ORCHESTRATOR {
        @Override
        public Optional<ChallengeOrchestratorMessage> parse(String messageAsString) {
            return ChallengeOrchestratorMessage.parse(messageAsString);
        }
    },
    VOTE {
        @Override
        public Optional<VoteMessage> parse(String messageAsString) {
            return VoteMessage.parse(messageAsString);
        }
    },
}
