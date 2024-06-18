package io.nirahtech.petvet.esp.monitor.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.nirahtech.petvet.esp.monitor.ESP32;

public final class EspPanel extends JPanel {
    private final ESP32 esp;

    public EspPanel(final ESP32 esp) {
        this.esp = esp;
        this.setLayout(new GridBagLayout()); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(new JLabel("MAC Address:"), gbc);

        gbc.gridx = 1;
        this.add(new JLabel(this.esp.getMac().toString()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(new JLabel("IP Address:"), gbc);

        gbc.gridx = 1;
        this.add(new JLabel(this.esp.getIp().toString()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(new JLabel("Signal Strength (dBm):"), gbc);

        gbc.gridx = 1;
        // this.add(new JLabel(String.valueOf(this.esp.getSignalStrengthInDBm())), gbc);
        
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
