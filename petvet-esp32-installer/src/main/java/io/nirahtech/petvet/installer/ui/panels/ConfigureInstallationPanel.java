package io.nirahtech.petvet.installer.ui.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import io.nirahtech.templateprocessor.JinjaEngine;
import io.nirahtech.templateprocessor.TemplateEngine;

public class ConfigureInstallationPanel extends JPanel {

    private final TemplateEngine templateEngine = new JinjaEngine();

    private final JPanel basePanel = new JPanel();

    private String sourceCode;
    private final Map<String, Object> configurationTokens = new HashMap<>();

    public ConfigureInstallationPanel() {
        super(new BorderLayout());
        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.Y_AXIS));
        basePanel.add(new JLabel("No template file to parse."));

        this.add(new JScrollPane(basePanel), BorderLayout.CENTER);
    }

    private JPanel createTokenPanel(final String token) {
        final JPanel panel = new JPanel(new GridLayout(2, 1));
        final Dimension dimension = new Dimension(panel.getPreferredSize().width, 50);
        panel.setPreferredSize(dimension);
        panel.setMinimumSize(dimension);
        panel.setMaximumSize(dimension);

        final JLabel title = new JLabel(String.format("<html><h3>%s</h3></html>", token));
        final JTextField value = new JTextField();
        value.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onChange();
            }

            private void onChange() {
                templateEngine.put(token, value.getText());
            }
        });

        panel.add(title);
        panel.add(value);
        return panel;
    }

    private void updateTemplateTokensFromSourceCode() {
        this.configurationTokens.clear();
        basePanel.removeAll(); // Clear previous content
        if (Objects.nonNull(this.sourceCode)) {
            basePanel.add(new JLabel("Template file parsed. Add tokens below:"));
            this.templateEngine.retrieveTokens(this.sourceCode).forEach(tokenFound -> {
                basePanel.add(createTokenPanel(tokenFound));
            });
            basePanel.revalidate();
            basePanel.repaint();
        }
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
        this.updateTemplateTokensFromSourceCode();
    }

}
