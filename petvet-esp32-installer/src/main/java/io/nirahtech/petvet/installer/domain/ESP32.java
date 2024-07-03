package io.nirahtech.petvet.installer.domain;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.UUID;

public final class ESP32 implements Serializable, Comparable<ESP32>, Comparator<ESP32> {
    private final Path usbPort;
    private UUID id;
    private Software software;

    public ESP32(final UUID id, final Path usbPort, final Software software) {
        this.id = id;
        this.usbPort = usbPort;
        this.software = software;
    }

    public Software getSoftware() {
        return software;
    }
    public Path getUsbPort() {
        return usbPort;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((usbPort == null) ? 0 : usbPort.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((software == null) ? 0 : software.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ESP32 other = (ESP32) obj;
        if (usbPort == null) {
            if (other.usbPort != null)
                return false;
        } else if (!usbPort.equals(other.usbPort))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (software == null) {
            if (other.software != null)
                return false;
        } else if (!software.equals(other.software))
            return false;
        return true;
    }

    @Override
    public int compare(ESP32 esp1, ESP32 esp2) {
        int idComparison = esp1.id.compareTo(esp2.id);
        if (idComparison != 0) {
            return idComparison;
        }

        int usbPortComparison = esp1.usbPort.compareTo(esp2.usbPort);
        if (usbPortComparison != 0) {
            return usbPortComparison;
        }

        if (esp1.software == null && esp2.software != null) {
            return -1;
        } else if (esp1.software != null && esp2.software == null) {
            return 1;
        } else if (esp1.software == null && esp2.software == null) {
            return 0;
        }

        return esp1.software.compareTo(esp2.software);

    }

    @Override
    public int compareTo(ESP32 other) {
        return this.compare(this, other);

    }

    @Override
    public String toString() {
        return "ESP32 [usbPort=" + usbPort + ", id=" + id + ", software=" + software + "]";
    }
}
