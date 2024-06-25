package io.nirahtech.petvet.geopulsetracker.ui;

import java.awt.BorderLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;

import io.nirahtech.petvet.geopulsetracker.domain.ESP32;
import io.nirahtech.petvet.geopulsetracker.domain.ElectronicChipBoard;

public final class Window extends JFrame {
    private final MapPanel mapPanel;

    private final Set<ElectronicChipBoard> electronicChipBoards;
    private final JButton createChipBoardButton;
    
    public Window() {
        super("PETVET - Geo Pulse Tracker");
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
    }
}
