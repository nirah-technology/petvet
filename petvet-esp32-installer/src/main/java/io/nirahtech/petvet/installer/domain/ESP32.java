package io.nirahtech.petvet.installer.domain;

import java.nio.file.Path;

public final class ESP32 {
    private final Path usbPort;
    private Software software;

    public ESP32(final Path usbPort, final Software software) {
        this.usbPort = usbPort;
        this.software = software;
    }

    public Software getSoftware() {
        return software;
    }
    public Path getUsbPort() {
        return usbPort;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }
}
