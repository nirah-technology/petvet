package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import io.nirahtech.petvet.messaging.util.MacAddress;
import io.nirahtech.petvet.simulator.electronicalcard.ElectronicCard;
import io.nirahtech.petvet.simulator.electronicalcard.PetVetSketch;

public class NodeDetailPanel extends JPanel {

    private ElectronicCard electronicCard = null;;

    private final JLabel idLabel;
    private final JLabel macLabel;
    private final JLabel ipLabel;
    private final JLabel modeLabel;
    private final JLabel uptimeLabel;

    private final JLabel idValue;
    private final JLabel macValue;
    private final JLabel ipValue;
    private final JLabel modeValue;
    private final JLabel uptimeValue;

    private final List<Map.Entry<MacAddress, Float>> signals = new ArrayList<>();
    private final SignalsStrengthsTable signalsTables;

    private final JButton powerOnButton;
    private final JButton powerOffButton;

    public void setElectronicCard(ElectronicCard electronicCard) {
        this.electronicCard = electronicCard;
        if (Objects.nonNull(electronicCard)) {
            this.powerOnButton.setEnabled(!this.electronicCard.isRunning());
            this.powerOffButton.setEnabled(!this.powerOnButton.isEnabled());
        } else {
            this.powerOnButton.setEnabled(false);
            this.powerOffButton.setEnabled(false);
        }
        this.repaintPanel();
    }

    private final Map<MacAddress, Float> checkAndTransform(Map<ElectronicCard, Float> inputMap) {
        Map<MacAddress, Float> result = new HashMap<>();
        for (Map.Entry<ElectronicCard, Float> entry : inputMap.entrySet()) {
            result.put(entry.getKey().getProcess().getMac(), entry.getValue());
        }
        return result;
    }

    public void refresh() {
        this.repaintPanel();
    }

    private void repaintPanel() {
        if (Objects.nonNull(this.electronicCard)) {
            this.idValue.setText(this.electronicCard.getProcess().getId().toString());
            this.macValue.setText(this.electronicCard.getProcess().getMac().toString());
            this.ipValue.setText(this.electronicCard.getProcess().getIp().toString());
            this.modeValue.setText(this.electronicCard.getProcess().getMode().toString());
            this.uptimeValue.setText(String.valueOf(Duration.ofMillis(this.electronicCard.getProcess().getUptime())));

            this.signalsTables.setSignals(checkAndTransform(((PetVetSketch)this.electronicCard.getProcess()).getNeigborsNodeSignals()));

            this.powerOnButton.setEnabled(!this.electronicCard.isRunning());
            this.powerOffButton.setEnabled(!this.powerOnButton.isEnabled());
        } else {
            this.idValue.setText("-");
            this.macValue.setText("-");
            this.ipValue.setText("-");
            this.modeValue.setText("-");
            this.uptimeValue.setText("-");
            this.powerOnButton.setEnabled(false);
            this.powerOffButton.setEnabled(false);
        }
    }

    private final String title(final String text) {
        return String.format("<html><h3>%s</h3></html>", text.toUpperCase());
    }

    NodeDetailPanel() {
        super(new BorderLayout());
        final Dimension dimension = new Dimension(450, this.getPreferredSize().height);
        this.setPreferredSize(dimension); 
        this.setMinimumSize(dimension); 
        this.setMaximumSize(dimension);

        this.idLabel = new JLabel(title("Id"));
        this.macLabel = new JLabel(title("MAC Address"));
        this.ipLabel = new JLabel(title("IP Address"));
        this.modeLabel = new JLabel(title("Mode Node"));
        this.uptimeLabel = new JLabel(title("Uptime"));

        this.idValue = new JLabel("");
        this.macValue = new JLabel("");
        this.ipValue = new JLabel("");
        this.modeValue = new JLabel("");
        this.uptimeValue = new JLabel("");

        this.signalsTables = new SignalsStrengthsTable(this.signals);

        this.powerOnButton = new JButton("Power On");
        this.powerOffButton = new JButton("Power Off");

        this.powerOnButton.setEnabled(false);
        this.powerOffButton.setEnabled(false);

        this.powerOnButton.addActionListener((event) -> {
            if (Objects.nonNull(this.electronicCard)) {
                if (!this.electronicCard.isRunning()) {
                    System.out.println("Must start: " + this.electronicCard.getProcess().getId());
                }
            }
        });

        this.powerOffButton.addActionListener((event) -> {
            if (Objects.nonNull(this.electronicCard)) {
                if (this.electronicCard.isRunning()) {
                    System.out.println("Must stop: " + this.electronicCard.getProcess().getId());
                }
            }
        });

        final JPanel detailsPanel = new JPanel(new GridLayout(5, 2));
        detailsPanel.add(this.idLabel);
        detailsPanel.add(this.idValue);
        detailsPanel.add(this.macLabel);
        detailsPanel.add(this.macValue);
        detailsPanel.add(this.ipLabel);
        detailsPanel.add(this.ipValue);
        detailsPanel.add(this.modeLabel);
        detailsPanel.add(this.modeValue);
        detailsPanel.add(this.uptimeLabel);
        detailsPanel.add(this.uptimeValue);
        this.add(detailsPanel, BorderLayout.NORTH);
        
        final JPanel signalsPanel = new JPanel(new BorderLayout());
        signalsPanel.add(new JScrollPane(this.signalsTables), BorderLayout.CENTER);
        this.add(signalsPanel, BorderLayout.CENTER);

        final JPanel actionsPanel = new JPanel(new GridLayout(1, 2));
        actionsPanel.add(this.powerOnButton);
        actionsPanel.add(this.powerOffButton);
        this.add(actionsPanel, BorderLayout.SOUTH);
    }
}
