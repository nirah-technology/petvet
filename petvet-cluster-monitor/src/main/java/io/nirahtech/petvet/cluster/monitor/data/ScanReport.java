package io.nirahtech.petvet.cluster.monitor.data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import io.nirahtech.petvet.messaging.messages.ScanReportMessage;

public record ScanReport(
    UUID scanId,
    UUID espId,
    LocalDateTime sendedAt,
    Set<Device> detectedDevices
) implements Comparable<ScanReport> {

    public static final ScanReport map(final ScanReportMessage message) {
        Set<Device> detectedDevices = new HashSet<>();
        message.getScanReportResults().entrySet().forEach(device -> {
            detectedDevices.add(new Device(device.getKey(), device.getValue()));
        });
        return new ScanReport(message.getScanId(), message.getEmitterID(), message.getSentedAt(), detectedDevices);
    }

    @Override
    public int compareTo(ScanReport other) {
        return this.sendedAt.compareTo(other.sendedAt);
    }
}
