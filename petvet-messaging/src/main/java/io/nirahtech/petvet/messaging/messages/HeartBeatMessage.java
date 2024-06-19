package io.nirahtech.petvet.messaging.messages;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;

public final class HeartBeatMessage extends AbstractMessage {
    private static final String UPTIME_KEY = "uptime";
    private static final String TEMERATURE_KEY = "temeratureInCelcus";
    private static final String CONSUMPTION_KEY = "consumptionInVolt";
    private static final String LOCATION_KEY = "location";

    private final long uptime;
    private final float temeratureInCelcus;
    private final float consumptionInVolt;
    private final String location;

    public final long getUptime() {
        return uptime;
    }

    public float getTemeratureInCelcus() {
        return temeratureInCelcus;
    }

    public float getConsumptionInVolt() {
        return consumptionInVolt;
    }

    public String getLocation() {
        return location;
    }

    private HeartBeatMessage(
            final UUID emitterId,
            final MacAddress emitterMAC,
            final InetAddress emitterIP,
            final EmitterMode emitterMode,
            final LocalDateTime sentAt,
            final long uptime,
            final float temeratureInCelcus,
            final float consumptionInVolt,
            final String location) {
        super(emitterId, emitterMAC, emitterIP, emitterMode, MessageType.HEARTBEAT, sentAt);
        this.uptime = uptime;
        this.temeratureInCelcus = temeratureInCelcus;
        this.consumptionInVolt = consumptionInVolt;
        this.location = location;
    }

    public static HeartBeatMessage create(final UUID emitterId, final MacAddress emitterMAC,
            final InetAddress emitterIP, final EmitterMode emitterMode, final long uptime, final float temeratureInCelcus,
            final float consumptionInVolt,
            final String location) {
        return new HeartBeatMessage(emitterId, emitterMAC, emitterIP, emitterMode, LocalDateTime.now(), uptime,
                temeratureInCelcus, consumptionInVolt, location);
    }

    public static Optional<HeartBeatMessage> parse(String messageAsString) {
        Optional<HeartBeatMessage> HeartBeatMessage = Optional.empty();

        if (messageAsString.contains(":")) {
            final String[] messageParts = messageAsString.split(":", 2);
            final MessageType type = MessageType.valueOf(messageParts[0]);
            if (type.equals(MessageType.VOTE)) {
                final Map<String, Object> properties = Message.fromStringToMap(messageParts[1]);
                try {

                    final UUID id = UUID
                            .fromString(sanitize(properties.get(Message.EMITTER_ID_PROPERTY_NAME).toString()).strip());
                    final InetAddress ip = InetAddress
                            .getByName(sanitize(properties.get(Message.EMITTER_IP_PROPERTY_NAME).toString()).strip());
                    final MacAddress mac = MacAddress
                            .of(sanitize(properties.get(Message.EMITTER_MAC_PROPERTY_NAME).toString()).strip());
                    final EmitterMode mode = EmitterMode
                            .valueOf(sanitize(properties.get(Message.EMITTER_MODE_PROPERTY_NAME).toString()).strip());
                    final LocalDateTime sendedAt = LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(
                                    Long.parseLong(sanitize(properties.get(Message.SENDED_AT_PROPERTY_NAME).toString()).strip())),
                            ZoneOffset.UTC);
                    final long uptime = Long.parseLong(sanitize(properties.get(UPTIME_KEY).toString()));
                    final float temperature = Float.parseFloat(sanitize(properties.get(TEMERATURE_KEY).toString()));
                    final float concumption = Float.parseFloat(sanitize(properties.get(CONSUMPTION_KEY).toString()));
                    final String location = sanitize(properties.get(LOCATION_KEY).toString());
                    final HeartBeatMessage message = new HeartBeatMessage(id, mac, ip, mode, sendedAt, uptime,
                            temperature, concumption, location);
                    HeartBeatMessage = Optional.of(message);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }

        return HeartBeatMessage;
    }

    @Override
    public String toString() {
        StringBuilder messageBuilder = new StringBuilder()
                .append(super.toString())
                .append(String.format(",%s=%s,", UPTIME_KEY, this.uptime))
                .append(String.format("%s=%s,", TEMERATURE_KEY, this.temeratureInCelcus))
                .append(String.format("%s=%s,", CONSUMPTION_KEY, this.consumptionInVolt))
                .append(String.format("%s=%s", LOCATION_KEY, this.location));
        return messageBuilder.toString();
    }
}
