package io.nirahtech.petvet.installer.ui.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;

import io.nirahtech.petvet.installer.domain.ESP32;
import io.nirahtech.petvet.installer.ui.widgets.jinstallacard.JInstallCard;

public class InstallationPanel extends JPanel {
    
    private final JButton launchInstallBatchButton;

    private final JPanel espsPanel;

    private final Map<ESP32, Map<String, String>> configurationsPerESP = new HashMap<>();
    private final Set<JInstallCard> installCards = new HashSet<>();


    private JInstallCard addESP(ESP32 esp, Map<String, String> configuration) {
        final JInstallCard installCard = new JInstallCard(esp, configuration);
        return installCard;
    }

    public InstallationPanel() {
        super(new BorderLayout());
        this.espsPanel = new JPanel(new FlowLayout());
        this.add(this.espsPanel, BorderLayout.CENTER);
        this.launchInstallBatchButton = new JButton("Install");
        this.launchInstallBatchButton.addActionListener((event) -> {
            this.installCards.forEach(card -> {
                card.install();
            });
        });
        this.add(launchInstallBatchButton, BorderLayout.SOUTH);
    }

    public void setESPs(final Map<ESP32, Map<String, String>> configurationsPerESP) {
        this.espsPanel.removeAll();
        this.configurationsPerESP.clear();
        this.configurationsPerESP.putAll(configurationsPerESP);
        this.configurationsPerESP.entrySet().forEach(esp -> {
            JInstallCard card = this.addESP(esp.getKey(), esp.getValue());
            this.installCards.add(card);
            this.espsPanel.add(card);
        });
        repaint();
    }
    
}
