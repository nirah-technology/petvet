package io.nirahtech.petvet.esp.monitor.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;

import javax.swing.JPanel;

import io.nirahtech.petvet.esp.monitor.ESP32;

public class ESPInventoryPanel extends JPanel {

    private final Collection<ESP32> esps;
    private final JPanel espsPanel;
    
    public ESPInventoryPanel(final Collection<ESP32> esps) {
        this.esps = esps;
        this.espsPanel = new JPanel(new GridBagLayout());

        updateEspPanels();

    }


    private void updateEspPanels() {
        this.espsPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        for (ESP32 esp : esps) {
            this.espsPanel.add(new EspPanel(esp), gbc);
            gbc.gridy++;
        }
        this.espsPanel.revalidate();
        this.espsPanel.repaint();
    }
}
