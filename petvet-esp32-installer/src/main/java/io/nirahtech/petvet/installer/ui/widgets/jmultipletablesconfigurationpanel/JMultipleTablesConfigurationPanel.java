package io.nirahtech.petvet.installer.ui.widgets.jmultipletablesconfigurationpanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import io.nirahtech.petvet.installer.domain.ESP32;
import io.nirahtech.petvet.installer.ui.widgets.jtemplatetable.JTemplateTable;
import io.nirahtech.templateprocessor.TemplateEngine;

public final class JMultipleTablesConfigurationPanel extends JPanel {

    private final Map<ESP32, JTemplateTable> specificsTables = new HashMap<>();

    private final JTemplateTable globalTemplateConfigurationTable;
    private final JTabbedPane specificsConfigurationPanel;

    private Runnable onChanged = null;

    public JMultipleTablesConfigurationPanel(final TemplateEngine templateEngine) {
        super();
        this.setLayout(new GridLayout(2, 1));

        this.globalTemplateConfigurationTable = new JTemplateTable();

        final JPanel globalPanel = new JPanel(new BorderLayout());
        globalPanel.setBorder(BorderFactory.createTitledBorder("Global ESP32 Configuration"));
        globalPanel.add(new JScrollPane(this.globalTemplateConfigurationTable), BorderLayout.CENTER);

        this.specificsConfigurationPanel = new JTabbedPane();
        final JPanel specificsPanel = new JPanel(new BorderLayout());
        specificsPanel.setBorder(BorderFactory.createTitledBorder("Specific ESP32 Configurations"));
        specificsPanel.add(this.specificsConfigurationPanel, BorderLayout.CENTER);

        // Ajout d'un écouteur pour détecter les changements dans le tableau global
        this.globalTemplateConfigurationTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int row = e.getFirstRow();
                    int column = e.getColumn();
                    if (column == 1) { // Only value column changes are propagated
                        String token = (String) globalTemplateConfigurationTable.getValueAt(row, 0);
                        String newValue = (String) globalTemplateConfigurationTable.getValueAt(row, column);
                        templateEngine.put(token, newValue);
                        updateSpecificTables(token, newValue);
                        if (Objects.nonNull(onChanged)) {
                            onChanged.run();
                        }
                    }
                }
            }
        });

        this.add(globalPanel);
        this.add(specificsPanel);
    }

    private void updateSpecificTables(String token, String newValue) {
        specificsTables.values().forEach(specificTable -> {
            int row = specificTable.findRowByToken(token);
            if (row != -1) {
                specificTable.setValueAt(newValue, row, 1);
            }
        });
    }

    public void synchronizeConfigurations() {
        specificsTables.values().forEach(specificTable -> {
            specificTable.setTokens(this.globalTemplateConfigurationTable.getTokens());
        });
        repaint();
    }

    public final void setESP32s(final Set<ESP32> esps) {
        this.specificsConfigurationPanel.removeAll();
        this.specificsTables.clear();
        esps.forEach(esp -> {
            final Map<String, String> specificConfiguration = this.globalTemplateConfigurationTable.getTokens();
            final JTemplateTable specificConfigurationTable = new JTemplateTable();
            specificsTables.put(esp, specificConfigurationTable);
            specificConfigurationTable.setTokens(specificConfiguration);
            specificsConfigurationPanel.addTab(esp.getUsbPort().toString(), new JScrollPane(specificConfigurationTable));
        });
        this.globalTemplateConfigurationTable.getTokens().keySet().forEach(this::addTokenName);
        this.synchronizeConfigurations();
        this.repaint();
    }

    public void addTokenName(String tokenFound) {
        this.globalTemplateConfigurationTable.addTokenName(tokenFound);
        this.specificsTables.values().forEach(table -> table.addTokenName(tokenFound));
        this.synchronizeConfigurations();
    }

    public final Map<ESP32, Map<String, String>> getESP32sConfigurations() {
        final Map<ESP32, Map<String, String>> configurations = new HashMap<>();
        this.specificsTables.entrySet().forEach(esp -> {
            configurations.put(esp.getKey(), esp.getValue().getTokens());
        });
        return configurations;
    }

    public void setOnChanged(Runnable onChanged) {
        this.onChanged = onChanged;
    }

    
}
