package io.nirahtech.petvet.installer.infrastructure.out.adapters;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import io.nirahtech.petvet.installer.domain.ESP32;
import io.nirahtech.petvet.installer.domain.Software;
import io.nirahtech.petvet.installer.domain.Version;
import io.nirahtech.petvet.installer.infrastructure.out.ports.USB;

public final class ESP32Usb implements USB<ESP32> {

    private static USB<ESP32> instance = null;

    /**
     * @return the instance
     */
    public static USB<ESP32> getInstance() {
        if (Objects.isNull(instance)) {
            final Televerser televerser = new Esp32SketchTeleverser();
            final Installer installer = new PetvetEsp32Installer(televerser);
            instance = new ESP32Usb(installer);
        }
        return instance;
    }

    private final Installer installer;
    private final Esp32UsbFilter esp32Filter;

    private final Map<String, ESP32> cache = new HashMap<>();

    private ESP32Usb(final Installer installer) {
        this.installer = installer;
        this.esp32Filter = new Esp32UsbFilter();
    }

    private synchronized Set<SerialPort> retrieveAllConnectedESP32AsUSB() {
        // System.out.println("Retrieving connected ESP32 to USB ports...");
        final Set<SerialPort> esp32Ports = new HashSet<>();
        final SerialPort[] connectedUsbDevices = SerialPort.getCommPorts();
        for (SerialPort usbDevice : connectedUsbDevices) {
            if (this.esp32Filter.test(usbDevice)) {
                esp32Ports.add(usbDevice);
            }
        }
        // System.out.println(String.format("There are %s connected devices including %s ESP32", connectedUsbDevices.length, esp32Ports.size()));
        return esp32Ports;
    }

    private synchronized final Collection<String> retrieveAllDisconnectedESP32FromUSB(Collection<SerialPort> connectedESP32sAsUSB) {
        // System.out.println("Detecting disconnected ESP32...");
        final Collection<String> disconnectedESP32Ports = new HashSet<>();
        this.cache.keySet()
                .stream()
                .filter(portInCache -> connectedESP32sAsUSB.stream()
                        .map(SerialPort::getSystemPortPath)
                        .noneMatch(port -> port.equals(portInCache)))
                .forEach(disconnectedESP32Ports::add);
        // System.out.println(disconnectedESP32Ports.size() + " ESP32 was disconnected.");
        return disconnectedESP32Ports;
    }

    private synchronized final void deleteAllDisconnectedESP32FromUSB(Collection<String> disconnectedESP32USBPorts) {
        // System.out.println("Deleting disconnected ESP32 from cache...");
        disconnectedESP32USBPorts.forEach(disconnectedPort -> {
            this.cache.remove(disconnectedPort);
            // System.out.println(disconnectedPort + " is deleted from cache.");
        });

    }

    private synchronized final void attachEventListerToRetrieveESPInfo(SerialPort espPort, ESP32 esp32) {
        // System.out.println("Attach event listener on ESP32 port to retrieve ESP infos for: " + espPort.getSystemPortPath());
        if (espPort.openPort()) {
            // System.out.println("Connected to: " + espPort.getSystemPortPath());
            espPort.setBaudRate(115200);
    
            espPort.addDataListener(new SerialPortDataListener() {
                private final HelloMessageFilter messageFilter = new HelloMessageFilter();
                private final Duration retrieveInfoDuration = Duration.ofSeconds(10);
                private LocalDateTime startReading = null;
    
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }
    
                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                        return;
                    }
                    if (Objects.isNull(this.startReading)) {
                        this.startReading = LocalDateTime.now();
                    }
                    // System.out.println("Retrieve data from ESP...");
                    byte[] newData = new byte[espPort.bytesAvailable()];
                    espPort.readBytes(newData, newData.length);
                    Optional<Map<String, Object>> helloMessage = messageFilter.filterMessage(newData);
                    if (helloMessage.isPresent()) {
                        // System.out.println("Hello Message successfully retrieve!");
                        final UUID id = (UUID) helloMessage.get().get(HelloMessageFilter.ID_KEY);
                        final String name = helloMessage.get().get(HelloMessageFilter.SOFTWARE_NAME_KEY).toString();
                        final Version version = (Version) helloMessage.get().get(HelloMessageFilter.SOFTWARE_VERSION_KEY);
                        final Software sketch = new Software(name, version);
                        esp32.setId(id);
                        esp32.setSoftware(sketch);
                        espPort.removeDataListener();
                        espPort.closePort();
                        // System.out.println("ESP32 is updated with sended ESP infos.");
                        // System.out.println("Detaching event listener on ESP32 port.");
                    }
                    if (LocalDateTime.now().isAfter(startReading.plus(this.retrieveInfoDuration))) {
                        // System.out.println("Stopping to retrieve ESP infos...");
                        espPort.removeDataListener();
                        espPort.closePort();
                    }
                }
            });
        } else {
            this.cache.remove(espPort.getSystemPortPath());
            // System.out.println("[FAILURE] Failed to open port: " + espPort.getSystemPortPath());
        }
    }

    private synchronized final void cacheMissingConnectedESPFromUSB(Collection<SerialPort> connectedESP32sAsUSB) {
        if (connectedESP32sAsUSB.isEmpty()) {
            this.cache.clear();
        }
        // System.out.println("Add missing ESP to cache...");
        connectedESP32sAsUSB.forEach(espPort -> {
            final String port = espPort.getSystemPortPath();
            // System.out.println("ESP connected: " + port);
            if (!this.cache.containsKey(port)) {
                // System.out.println("ESP not cached.");
                final ESP32 esp32 = new ESP32(null, Path.of(port), null);
                this.attachEventListerToRetrieveESPInfo(espPort, esp32);
                this.cache.put(port, esp32);
                // System.out.println("ESP cached.");
            } else {
                // System.out.println("ESP already cached.");
            }
        });
    }

    private synchronized final void syncronizeCache(Collection<SerialPort> connectedESP32sAsUSB) {
        // System.out.println("Start cache syncronization...");
        Collection<String> disconnectedESP32Ports = this.retrieveAllDisconnectedESP32FromUSB(connectedESP32sAsUSB);
        this.deleteAllDisconnectedESP32FromUSB(disconnectedESP32Ports);
        this.cacheMissingConnectedESPFromUSB(connectedESP32sAsUSB);
        // System.out.println("Cache terminated.");
    }

    @Override
    public synchronized Collection<ESP32> list() {
        // System.out.println("Begin list");
        final Set<SerialPort> connectedESP32s = this.retrieveAllConnectedESP32AsUSB();
        this.syncronizeCache(connectedESP32s);
        // System.out.println("End list");
        return this.cache.values();
    }

    @Override
    public void upload() {
        this.installer.install();
    }

}
