package io.nirahtech.petvet.installer.ui.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JPanel;

import io.nirahtech.petvet.installer.domain.ESP32;
import io.nirahtech.petvet.installer.ui.widgets.jinstallacard.JInstallCard;
import io.nirahtech.templateprocessor.TemplateEngine;

public class InstallationPanel extends JPanel {
    
    private final TemplateEngine templateEngine;
    private final JButton launchInstallBatchButton;

    private final JPanel espsPanel;

    private final Map<ESP32, Map<String, String>> configurationsPerESP = new HashMap<>();
    private final Set<JInstallCard> installCards = new HashSet<>();
    private File sketchFile;


    private JInstallCard addESP(ESP32 esp, Map<String, String> configuration) {
        final StringBuilder sourceCode = new StringBuilder();
        if (Objects.nonNull(this.sketchFile)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(this.sketchFile))) {
                String code = reader.lines().collect(Collectors.joining("\n"));
                code = this.templateEngine.parse(code);
                sourceCode.append(code);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        final JInstallCard installCard = new JInstallCard(esp, configuration, sourceCode.toString());
        return installCard;
    }

    public InstallationPanel(final TemplateEngine templateEngine) {
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
        this.templateEngine = templateEngine;
    }

    public void setSketch(final File sketchFile) {
        this.sketchFile = sketchFile;
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
