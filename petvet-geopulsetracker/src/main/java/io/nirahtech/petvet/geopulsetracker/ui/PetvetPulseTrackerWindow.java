package io.nirahtech.petvet.geopulsetracker.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import io.nirahtech.petvet.geopulsetracker.domain.Cluster;
import io.nirahtech.petvet.geopulsetracker.domain.ESP32;
import io.nirahtech.petvet.messaging.brokers.MessageBroker;
import io.nirahtech.petvet.messaging.messages.MessageType;
import io.nirahtech.petvet.messaging.messages.ScanReportMessage;

public final class PetvetPulseTrackerWindow extends JFrame {
    private final MapPanel mapPanel;
    private final MessageBroker messageBroker;
    private final Cluster<ESP32> cluster;
    
    public PetvetPulseTrackerWindow(MessageBroker messageBroker) {
        super("PETVET - Geo Pulse Tracker");
        this.messageBroker = messageBroker;
        this.setLayout(new BorderLayout());
        this.cluster = new Cluster<>();
        this.mapPanel = new MapPanel(this.cluster);

        this.add(mapPanel, BorderLayout.CENTER);
        // this.add(this.createChipBoardButton, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);

        this.messageBroker.subscribe(MessageType.SCAN_REPORT, (message) -> {
            if (message instanceof ScanReportMessage) {
                final ScanReportMessage realMessage = (ScanReportMessage) message;
                final ESP32 esp32 = ESP32.getOrCreateWithWiFiMacAddress(realMessage.getEmitterMAC());
                if (!cluster.contains(esp32)) {
                    cluster.add(esp32);
                }
                final ESP32Sprite sprite = this.mapPanel.useAsSprite(esp32);
                System.out.println("Scan Report received for " + esp32.getId());
                // System.out.println("Saving distances...");
                this.mapPanel.saveDistances(sprite, realMessage.getScanReportResults());
                // System.out.println("Computing positions...");
                this.mapPanel.calculatePosition();
                // System.out.println("Trigging pulse...");
                this.mapPanel.triggerPulse(sprite);
            }
        });
    }
}
