package io.nirahtech.petvet.esp.commands;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import io.nirahtech.petvet.messaging.brokers.MessagePublisher;
import io.nirahtech.petvet.messaging.messages.HeartBeatMessage;
import io.nirahtech.petvet.messaging.util.EmitterMode;
import io.nirahtech.petvet.messaging.util.MacAddress;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;

public final class HeartBeatCommand extends AbstractCommand {

    private final long uptime;
    private final MacAddress mac;
    private final InetAddress ip;
    private final MessagePublisher messageSender;
    private final EmitterMode mode;
    private final UUID id;

    HeartBeatCommand(final MessagePublisher messageSender, final UUID id, final MacAddress mac, final InetAddress ip, final EmitterMode mode, final long uptime) {
        this.ip = ip;
        this.mac = mac;
        this.uptime = uptime;
        this.mode = mode;
        this.messageSender = messageSender;
        this.id = id;
    }

    private final void publishInfoToBeElectedAsOrchestrator() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        Sensors sensors = hardware.getSensors();
        
        double cpuTemperature = sensors.getCpuTemperature();
        double voltage = 0.0D;
        try {
            final HeartBeatMessage message = HeartBeatMessage.create(
                id, mac, ip, mode, uptime, (float)cpuTemperature, (float)voltage, null
            );
            messageSender.send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void execute() throws IOException {
        super.execute();
        this.publishInfoToBeElectedAsOrchestrator();
    }
}
