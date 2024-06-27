package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.time.Duration;
import java.util.Objects;

import javax.swing.JLabel;
import javax.swing.JPanel;

import io.nirahtech.petvet.simulator.electronicalcard.ElectronicCard;

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

    public void setElectronicCard(ElectronicCard electronicCard) {
        this.electronicCard = electronicCard;
        this.repaintPanel();
    }

    private void repaintPanel() {
        if (Objects.nonNull(this.electronicCard)) {
            this.idValue.setText(this.electronicCard.getProcess().getId().toString());
            this.macValue.setText(this.electronicCard.getProcess().getMac().toString());
            this.ipValue.setText(this.electronicCard.getProcess().getIp().toString());
            this.modeValue.setText(this.electronicCard.getProcess().getMode().toString());
            this.uptimeValue.setText(String.valueOf(Duration.ofMillis(this.electronicCard.getProcess().getUptime())));

        } else {
            this.idValue.setText("-");
            this.macValue.setText("-");
            this.ipValue.setText("-");
            this.modeValue.setText("-");
            this.uptimeValue.setText("-");
        }
    }


    NodeDetailPanel() {
        super(new GridLayout(5, 2));
        final Dimension dimension = new Dimension(300, this.getPreferredSize().height);
        this.setPreferredSize(dimension); 
        this.setMinimumSize(dimension); 
        this.setMaximumSize(dimension);

        this.idLabel = new JLabel("ID".toUpperCase());
        this.macLabel = new JLabel("MAC Address".toUpperCase());
        this.ipLabel = new JLabel("IP Address".toUpperCase());
        this.modeLabel = new JLabel("Mode Node".toUpperCase());
        this.uptimeLabel = new JLabel("Uptime".toUpperCase());

        this.idValue = new JLabel("");
        this.macValue = new JLabel("");
        this.ipValue = new JLabel("");
        this.modeValue = new JLabel("");
        this.uptimeValue = new JLabel("");

        this.add(this.idLabel);
        this.add(this.idValue);
        this.add(this.macLabel);
        this.add(this.macValue);
        this.add(this.ipLabel);
        this.add(this.ipValue);
        this.add(this.modeLabel);
        this.add(this.modeValue);
        this.add(this.uptimeLabel);
        this.add(this.uptimeValue);
    }

}
