package io.nirahtech.petvet.installer.ui.panels;

import java.awt.BorderLayout;
import java.nio.file.Path;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.nirahtech.petvet.installer.infrastructure.out.ports.USB;

public class ResumeAfterInstallationPanel extends JPanel {
        
    private final USB usb;


    public ResumeAfterInstallationPanel(final USB usb) {
        super(new BorderLayout());
        // this.setLayout(new GridLayout(5, 1));
        
        this.add(new JLabel("Choose an ESP32 as USB..."));
        
        this.usb = usb;
        final DefaultComboBoxModel<Path> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addAll(this.usb.list());
        final JComboBox<Path> esp32Devices = new JComboBox<>(comboBoxModel);
        this.add(esp32Devices, BorderLayout.CENTER);

    }

}
