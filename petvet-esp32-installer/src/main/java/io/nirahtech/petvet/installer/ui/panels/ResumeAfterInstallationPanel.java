package io.nirahtech.petvet.installer.ui.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.nio.file.Path;
import java.util.Objects;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.nirahtech.petvet.installer.infrastructure.out.ports.USB;
import io.nirahtech.petvet.installer.ui.components.stepper.Stepper;

public class ResumeAfterInstallationPanel extends JPanel {
        
    private final USB usb;
    private final JButton previousStepButton;
    private Runnable onPrevious = null;


    public ResumeAfterInstallationPanel(final USB usb, final Stepper stepper) {
        super(new BorderLayout());
        // this.setLayout(new GridLayout(5, 1));
        
        this.add(new JLabel("Choose an ESP32 as USB..."));
        
        this.usb = usb;
        final DefaultComboBoxModel<Path> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addAll(this.usb.list());
        final JComboBox<Path> esp32Devices = new JComboBox<>(comboBoxModel);
        this.add(esp32Devices, BorderLayout.CENTER);

        this.previousStepButton = new JButton("Previous");
        this.previousStepButton.addActionListener(event -> {
            stepper.selectPreviousStep();
            if (Objects.nonNull(this.onPrevious)) {
                this.onPrevious.run();
            }
        });


        final JPanel navigatorPanel = new JPanel(new GridLayout(1, 2));
        navigatorPanel.add(this.previousStepButton);
        this.add(navigatorPanel, BorderLayout.SOUTH);
    }

    /**
     * @param onPrevious the onPrevious to set
     */
    public void setOnPreviousEventHandler(Runnable onPrevious) {
        this.onPrevious = onPrevious;
    }
}
