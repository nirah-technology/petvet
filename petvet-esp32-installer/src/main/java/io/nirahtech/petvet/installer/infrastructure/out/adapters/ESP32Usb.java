package io.nirahtech.petvet.installer.infrastructure.out.adapters;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fazecast.jSerialComm.SerialPort;

import io.nirahtech.petvet.installer.infrastructure.out.ports.USB;

public final class ESP32Usb implements USB {

    private static USB instance = null;
    
    /**
     * @return the instance
     */
    public static USB getInstance() {
        if (Objects.isNull(instance)) {
            final Televerser televerser = new Esp32SketchTeleverser();
            final Installer installer = new PetvetEsp32Installer(televerser);
            instance = new ESP32Usb(installer);
        }
        return instance;
    }

    private final Installer installer;
    private final Esp32UsbFilter esp32Filter;

    private ESP32Usb(final Installer installer) {
        this.installer = installer;
        this.esp32Filter = new Esp32UsbFilter();
    }

    @Override
    public Set<Path> list() {
        final Set<Path> esp32Usb = new HashSet<>();
        for (SerialPort serialPort: SerialPort.getCommPorts()) {
            if (this.esp32Filter.test(serialPort)) {
                esp32Usb.add(Path.of(serialPort.getSystemPortPath()));
            }
        }
        return esp32Usb;
    }

    @Override
    public void upload() {
        this.installer.install();
    }
    
}
