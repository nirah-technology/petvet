package io.nirahtech.petvet.installer.infrastructure.out.adapters;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import io.nirahtech.petvet.installer.domain.Version;

public final class HelloMessageFilter {

    public static final String ID_KEY = "id";
    public static final String SOFTWARE_NAME_KEY = "softwareName";
    public static final String SOFTWARE_VERSION_KEY = "softwareVersion";
    private static final String SEPARATOR = ";";

    private final StringBuilder helloMessageBuilder = new StringBuilder();
    private boolean isReadingMessage = false;

    public final Optional<Map<String, Object>> filterMessage(final byte[] messagePartAsBytes) {
        Map<String, Object> properties = null;
        String helloMessage = null;

        for (byte data : messagePartAsBytes) {
            if (data == 60) { // début
                this.isReadingMessage = true;

            } else if (data == 62) { // fin
                this.isReadingMessage = false;
                if (this.helloMessageBuilder.toString().startsWith(ID_KEY)) {
                    helloMessage = this.helloMessageBuilder.toString();
                }
                this.helloMessageBuilder.delete(0, this.helloMessageBuilder.length()-1);

            } else if (data != 10 && this.isReadingMessage) { // saut de ligne
                this.helloMessageBuilder.append((char)data);
            }
        }

        if (Objects.nonNull(helloMessage)) {
            if (helloMessage.contains(ID_KEY) && helloMessage.contains(SOFTWARE_NAME_KEY) && helloMessage.contains(SOFTWARE_VERSION_KEY)) {
                properties = new HashMap<>();
                final String[] messageParts = helloMessage.split(SEPARATOR, 3);
                for (String messagePart : messageParts) {
                    String[] attribute = messagePart.split("=", 2);
                    final String propertyName = attribute[0];
                    final String propertyValue = attribute[1];
                    Object value = null;
                    if (propertyName.equalsIgnoreCase(ID_KEY)) {
                        value = UUID.fromString(propertyValue);
                    } else if (propertyName.equalsIgnoreCase(SOFTWARE_VERSION_KEY)) {
                        value = Version.of(propertyValue);
                    } else {
                        value = propertyValue;
                    }
                    properties.put(propertyName, value);
                }
            }
        }

        return Optional.ofNullable(properties);
    }
}
