package io.nirahtech.petvet.geopulsetracker.ui;

import java.awt.BorderLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;

import io.nirahtech.petvet.geopulsetracker.domain.ESP32;
import io.nirahtech.petvet.geopulsetracker.domain.ElectronicChipBoard;
import io.nirahtech.petvet.messaging.brokers.MessageBroker;
import io.nirahtech.petvet.messaging.messages.MessageType;
import io.nirahtech.petvet.messaging.messages.ScanReportMessage;

public final class PetvetPulseTrackerWindow extends JFrame {
    private final MapPanel mapPanel;

    private final Set<ElectronicChipBoard> electronicChipBoards;
    private final JButton createChipBoardButton;
    private final MessageBroker messageBroker;
    
    public PetvetPulseTrackerWindow(MessageBroker messageBroker) {
        super("PETVET - Geo Pulse Tracker");
        this.messageBroker = messageBroker;
        this.electronicChipBoards = new HashSet<>();
        this.setLayout(new BorderLayout());
        this.mapPanel = new MapPanel(this.electronicChipBoards);

        
        this.createChipBoardButton = new JButton("Create Chip Board");
        this.createChipBoardButton.addActionListener(event -> {
            final ESP32 esp = ESP32.generate();
            esp.powerOn();
            this.electronicChipBoards.add(esp);
            this.mapPanel.addElectronicChipBoard(esp);
        });

        this.add(mapPanel, BorderLayout.CENTER);
        this.add(this.createChipBoardButton, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);

        this.messageBroker.subscribe(MessageType.SCAN_REPORT, (message) -> {
            if (message instanceof ScanReportMessage) {
                final ScanReportMessage realMessage = (ScanReportMessage) message;
                this.mapPanel.triggerPulse(realMessage.getScanReportResults());
            }
        });
    }
}
