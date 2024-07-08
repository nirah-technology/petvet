package io.nirahtech.petvet.cluster.monitor.data;

import java.time.LocalDateTime;
import java.util.UUID;

import io.nirahtech.petvet.messaging.messages.ScanNowMessage;

public record ScanNow(
    UUID scanId,
    UUID espId,
    LocalDateTime sendedAt
) implements Comparable<ScanNow> {
    
    public static final ScanNow map(final ScanNowMessage message) {
        return new ScanNow(message.getScanId(), message.getEmitterID(), message.getSentedAt());
    }

    @Override
    public int compareTo(ScanNow other) {
        return this.sendedAt.compareTo(other.sendedAt);
    }
}
