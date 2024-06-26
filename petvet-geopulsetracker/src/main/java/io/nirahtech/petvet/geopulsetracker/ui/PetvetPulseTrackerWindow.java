package io.nirahtech.petvet.geopulsetracker.ui;

import java.awt.BorderLayout;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.swing.JFrame;

import io.nirahtech.petvet.geopulsetracker.domain.ESP32;
import io.nirahtech.petvet.geopulsetracker.domain.ElectronicChipBoard;
import io.nirahtech.petvet.messaging.brokers.MessageBroker;
import io.nirahtech.petvet.messaging.messages.MessageType;
import io.nirahtech.petvet.messaging.messages.ScanReportMessage;

public final class PetvetPulseTrackerWindow extends JFrame {
    private final MapPanel mapPanel;

    private final Set<ElectronicChipBoard> electronicChipBoards;
    private final MessageBroker messageBroker;
    
    public PetvetPulseTrackerWindow(MessageBroker messageBroker) {
        super("PETVET - Geo Pulse Tracker");
        this.messageBroker = messageBroker;
        this.electronicChipBoards = new HashSet<>();
        this.setLayout(new BorderLayout());
        this.mapPanel = new MapPanel(this.electronicChipBoards);


        this.add(mapPanel, BorderLayout.CENTER);
        // this.add(this.createChipBoardButton, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);

        this.messageBroker.subscribe(MessageType.SCAN_REPORT, (message) -> {
            if (message instanceof ScanReportMessage) {
                final ScanReportMessage realMessage = (ScanReportMessage) message;
                Optional<ESP32> reporter = this.electronicChipBoards
                        .stream()
                        .filter(ESP32.class::isInstance)
                        .map(ESP32.class::cast)
                        .filter(esp -> esp.getWifi().getMacAddress().equals(realMessage.getEmitterMAC()))
                        .findFirst();
                
                ESP32 scanner;
                if (reporter.isPresent()) {
                    scanner = reporter.get();
                } else {
                    scanner = ESP32.getOrCreateWithWiFiMacAddress(realMessage.getEmitterMAC());
                }

                this.mapPanel.triggerPulse(scanner, realMessage.getScanReportResults());
            }
        });
    }
}
