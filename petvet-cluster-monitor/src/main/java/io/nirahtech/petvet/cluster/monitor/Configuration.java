package io.nirahtech.petvet.cluster.monitor;

import java.net.InetAddress;

/**
 * Configuration
 */
public record Configuration(
   InetAddress multicastGroup,
   int multicastPort
) {
}