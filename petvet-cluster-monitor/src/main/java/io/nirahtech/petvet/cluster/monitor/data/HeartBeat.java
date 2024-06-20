package io.nirahtech.petvet.cluster.monitor.data;

import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalDateTime;

import io.nirahtech.petvet.messaging.util.EmitterMode;

public record HeartBeat(
    LocalDateTime dateTime,
    InetAddress ip,
    EmitterMode mode,
    Duration uptime,
    float temperature,
    float consumption,
    String location
) {
    
}
