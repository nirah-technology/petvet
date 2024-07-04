package io.nirahtech.petvet.installer.ui.panels;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import io.nirahtech.petvet.installer.ui.widgets.jtemplatetable.JTemplateTable;
import io.nirahtech.templateprocessor.JinjaEngine;
import io.nirahtech.templateprocessor.TemplateEngine;

public class ConfigureInstallationPanel extends JPanel {

    private final TemplateEngine templateEngine = new JinjaEngine();

    private final JPanel basePanel = new JPanel();
    private final JTemplateTable templateTable;

    private String sourceCode;
    private final Map<String, Object> configurationTokens = new HashMap<>();

    public ConfigureInstallationPanel() {
        super(new BorderLayout());
        basePanel.setLayout(new BorderLayout());
        basePanel.add(new JLabel("No template file parsed."), BorderLayout.CENTER);
        this.templateTable = new JTemplateTable();
        this.add(new JScrollPane(basePanel), BorderLayout.CENTER);
    }

    private void updateTemplateTokensFromSourceCode() {
        this.configurationTokens.clear();
        basePanel.removeAll(); // Clear previous content
        if (Objects.nonNull(this.sourceCode)) {
            this.templateEngine.retrieveTokens(this.sourceCode).forEach(tokenFound -> {
                this.templateTable.addTokenName(tokenFound);
            });
        }
        SwingUtilities.invokeLater(() -> {
            basePanel.add(new JScrollPane(this.templateTable), BorderLayout.CENTER);
            basePanel.revalidate();
            basePanel.repaint();
        });

        // Revalidate and repaint the parent container of basePanel
        JScrollPane scrollPane = (JScrollPane) basePanel.getParent().getParent();
        SwingUtilities.invokeLater(() -> {
            scrollPane.revalidate();
            scrollPane.repaint();
        });

    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
        this.updateTemplateTokensFromSourceCode();
    }

}
