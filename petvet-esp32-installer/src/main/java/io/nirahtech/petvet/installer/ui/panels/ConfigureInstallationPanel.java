package io.nirahtech.petvet.installer.ui.panels;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import io.nirahtech.petvet.installer.domain.ESP32;
import io.nirahtech.petvet.installer.ui.widgets.jmultipletablesconfigurationpanel.JMultipleTablesConfigurationPanel;
import io.nirahtech.templateprocessor.JinjaEngine;
import io.nirahtech.templateprocessor.TemplateEngine;

public class ConfigureInstallationPanel extends JPanel {

    private final TemplateEngine templateEngine = new JinjaEngine();

    private final JPanel basePanel = new JPanel();
    

    private final JMultipleTablesConfigurationPanel jMultipleTablesConfigurationPanel;

    private final Set<ESP32> espsToConfigure = new HashSet<>();

    private String sourceCode;
    private final Map<String, Object> configurationTokens = new HashMap<>();

    public ConfigureInstallationPanel() {
        super(new BorderLayout());
        basePanel.setLayout(new BorderLayout());
        basePanel.add(new JLabel("No template file parsed."), BorderLayout.CENTER);
        this.jMultipleTablesConfigurationPanel = new JMultipleTablesConfigurationPanel();
        this.add(basePanel, BorderLayout.CENTER);
    }

    private void updateTemplateTokensFromSourceCode() {
        this.configurationTokens.clear();
        basePanel.removeAll(); // Clear previous content
        if (Objects.nonNull(this.sourceCode)) {
            this.templateEngine.retrieveTokens(this.sourceCode).forEach(tokenFound -> {
                this.jMultipleTablesConfigurationPanel.addTokenName(tokenFound);
            });
        }
        basePanel.add(jMultipleTablesConfigurationPanel, BorderLayout.CENTER);
        this.repaint();

    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
        this.updateTemplateTokensFromSourceCode();
    }

	public void setEsp32s(Set<ESP32> esps) {
        this.espsToConfigure.clear();
        if (Objects.nonNull(esps)) {
            this.espsToConfigure.addAll(esps);
            this.jMultipleTablesConfigurationPanel.setESP32s(esps);
        }
	}

    public void synchronizeConfigurations() {
        this.jMultipleTablesConfigurationPanel.synchronizeConfigurations();
    }

    public final Map<ESP32, Map<String, String>> getESP32sConfigurations() {
        return this.jMultipleTablesConfigurationPanel.getESP32sConfigurations();
    }

    public void addOnConfigurationChanged(Runnable onConfigurationChanged) {
        this.jMultipleTablesConfigurationPanel.setOnChanged(onConfigurationChanged);
    }

}
