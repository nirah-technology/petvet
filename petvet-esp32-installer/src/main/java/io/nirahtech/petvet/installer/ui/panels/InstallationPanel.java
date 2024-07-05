package io.nirahtech.petvet.installer.ui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import io.nirahtech.petvet.installer.domain.ESP32;

public class InstallationPanel extends JPanel {
    
    private final JButton launchInstallBatchButton;

    private final JPanel espsPanel;

    private final Map<ESP32, Map<String, String>> configurationsPerESP = new HashMap<>();


    private JPanel addESP(ESP32 esp, Map<String, String> configuration) {
        final JPanel panel = new JPanel(new GridLayout(4, 1));
        final Dimension dimension = new Dimension(250, 150);
        panel.setPreferredSize(dimension);
        final JLabel port = new JLabel(esp.getUsbPort().toString());
        final JTable evolutionTable = new JTable();
        final JProgressBar installProcess = new JProgressBar(0, 100);
        installProcess.setIndeterminate(true);
        final JLabel status = new JLabel("Installation in progress...");
        panel.add(port);
        panel.add(new JScrollPane(evolutionTable));
        panel.add(installProcess);
        panel.add(status);
        return panel;
    }

    public InstallationPanel() {
        super(new BorderLayout());
        this.espsPanel = new JPanel(new FlowLayout());
        this.add(this.espsPanel, BorderLayout.CENTER);
        this.launchInstallBatchButton = new JButton("Install");
        this.add(launchInstallBatchButton, BorderLayout.SOUTH);
    }

    public void setESPs(final Map<ESP32, Map<String, String>> configurationsPerESP) {
        this.espsPanel.removeAll();
        this.configurationsPerESP.clear();
        this.configurationsPerESP.putAll(configurationsPerESP);
        this.configurationsPerESP.entrySet().forEach(esp -> {
            this.espsPanel.add(this.addESP(esp.getKey(), esp.getValue()));
        });
        repaint();
    }
    
}
