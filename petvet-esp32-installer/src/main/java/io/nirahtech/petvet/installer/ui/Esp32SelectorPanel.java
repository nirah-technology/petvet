package io.nirahtech.petvet.installer.ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.nio.file.Path;
import java.util.Objects;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.nirahtech.petvet.installer.infrastructure.out.ports.USB;

public class Esp32SelectorPanel extends ResponsiveSliderPanel {

    
    private final USB usb;

    private final JButton nextStepButton;
    private Runnable onNext = null;


    Esp32SelectorPanel(Dimension dimension, final USB usb, final Stepper stepper) {
        super(dimension);
        this.setLayout(new GridLayout(5, 1));
        
        this.add(new JLabel("Choose an ESP32 as USB..."));
        
        this.usb = usb;
        final DefaultComboBoxModel<Path> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addAll(this.usb.list());
        final JComboBox<Path> esp32Devices = new JComboBox<>(comboBoxModel);
        this.add(esp32Devices);

        this.nextStepButton = new JButton("Next");
        this.nextStepButton.addActionListener(event -> {
            stepper.selectNextStep();
            if (Objects.nonNull(this.onNext)) {
                this.onNext.run();
            }
        });
        final JPanel navigatorPanel = new JPanel(new GridLayout(1, 2));
        navigatorPanel.add(this.nextStepButton);
        this.add(navigatorPanel);
    }

    /**
     * @param onNext the onNext to set
     */
    public void setOnNextEventHandler(Runnable onNext) {
        this.onNext = onNext;
    }
    
}
