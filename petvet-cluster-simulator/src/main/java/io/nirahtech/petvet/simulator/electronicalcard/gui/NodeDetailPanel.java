package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.time.Duration;
import java.util.Objects;

import javax.swing.JButton;
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

    private void repaintPanel() {
        if (Objects.nonNull(this.electronicCard)) {
            this.idValue.setText(this.electronicCard.getProcess().getId().toString());
            this.macValue.setText(this.electronicCard.getProcess().getMac().toString());
            this.ipValue.setText(this.electronicCard.getProcess().getIp().toString());
            this.modeValue.setText(this.electronicCard.getProcess().getMode().toString());
            this.uptimeValue.setText(String.valueOf(Duration.ofMillis(this.electronicCard.getProcess().getUptime())));

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
        super(new GridLayout(6, 2));
        final Dimension dimension = new Dimension(300, this.getPreferredSize().height);
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
        this.add(this.powerOnButton);
        this.add(this.powerOffButton);
    }

}
