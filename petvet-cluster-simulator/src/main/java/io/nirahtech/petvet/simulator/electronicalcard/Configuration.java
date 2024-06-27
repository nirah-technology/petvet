package io.nirahtech.petvet.simulator.electronicalcard;

import java.net.InetAddress;
import java.time.Duration;

import io.nirahtech.petvet.messaging.util.EmitterMode;

/**
 * Configuration
 */
public record Configuration(
   int clusterSize,
   InetAddress ipFilter,
   InetAddress multicastGroup,
   int multicastPort,
   EmitterMode mode,
   Duration scanInterval,
   Duration checkOrchestratoryInterval,
   Duration heartBeatInterval
) {
}