package io.nirahtech.petvet.cluster.monitor.data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import io.nirahtech.petvet.messaging.messages.ScanReportMessage;
import io.nirahtech.petvet.messaging.util.MacAddress;

public record ScanReport(
    UUID scanId,
    UUID espId,
    LocalDateTime sendedAt,
    Map<MacAddress, Float> detectedDevices
) implements Comparable<ScanReport> {

    public static final ScanReport map(final ScanReportMessage message) {
        return new ScanReport(message.getScanId(), message.getEmitterID(), message.getSentedAt(), message.getScanReportResults());
    }

    @Override
    public int compareTo(ScanReport other) {
        return this.sendedAt.compareTo(other.sendedAt);
    }
}
