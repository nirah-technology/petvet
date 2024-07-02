package io.nirahtech.petvet.installer.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import io.nirahtech.petvet.installer.infrastructure.out.adapters.ESP32Usb;
import io.nirahtech.petvet.installer.infrastructure.out.ports.USB;

public class PetvetInstallerWindow extends JFrame {
    
    private final USB usb;

    private final Stepper stepper;
    private final StepperPanel stepperPanel;

    private final Esp32SelectorPanel esp32SelectorPanel;
    private final SketchSelectorPanel sketchSelectorPanel;
    private final ConfigureInstallationPanel configureInstallationPanel;
    private final InstallationPanel installationPanel;
    private final ResumeAfterInstallationPanel resumeAfterInstallationPanel;

    public PetvetInstallerWindow() {
        super("PetVet : ESP32 Installer");
        this.usb = ESP32Usb.getInstance();
        final Dimension windowDimension = new Dimension(1240, 720);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setPreferredSize(windowDimension);
        this.setMinimumSize(windowDimension);
        this.setMaximumSize(windowDimension);
        this.setResizable(false);

        this.setLayout(new BorderLayout());

        this.stepper = new Stepper();
        final Dimension dimensionPanel = new Dimension(this.getWidth()/5, this.getHeight());
        this.esp32SelectorPanel = new Esp32SelectorPanel(dimensionPanel, this.usb, this.stepper);


        this.sketchSelectorPanel = new SketchSelectorPanel(dimensionPanel, this.stepper);
        this.configureInstallationPanel = new ConfigureInstallationPanel(dimensionPanel);
        this.installationPanel = new InstallationPanel(dimensionPanel);
        this.resumeAfterInstallationPanel = new ResumeAfterInstallationPanel(dimensionPanel);
        final Step step1 = new Step(1, "Select the ESP32 to setup", this.esp32SelectorPanel, true);
        final Step step2 = new Step(2, "Select the Sketch to install", this.sketchSelectorPanel, false);
        final Step step3 = new Step(3, "Configure installation", this.configureInstallationPanel, false);
        final Step step4 = new Step(4, "Install Sketch on ESP32", this.installationPanel, false);
        final Step step5 = new Step(5, "Installation resume", this.resumeAfterInstallationPanel, false);
        this.stepper.addStep(step1);
        this.stepper.addStep(step2);
        this.stepper.addStep(step3);
        this.stepper.addStep(step4);
        this.stepper.addStep(step5);
        this.stepperPanel = new StepperPanel(this.stepper); 
        this.add(this.stepperPanel, BorderLayout.NORTH);

        this.stepperPanel.onStepChanged(selectedStep -> {
            this.stepper.getSteps().forEach(step -> {
                step.getGraphicComponent().setVisible(false);
            });
            selectedStep.getGraphicComponent().setVisible(true);
            this.stepperPanel.redraw();
        });


        this.esp32SelectorPanel.setOnNextEventHandler(() -> {
            this.stepperPanel.redraw();
        });


        this.sketchSelectorPanel.setOnNextEventHandler(() -> {
            this.stepperPanel.redraw();
        });

        this.sketchSelectorPanel.setOnPreviousEventHandler(() -> {
            this.stepperPanel.redraw();
        });

        final JPanel mainPanel = new JPanel(new GridLayout(1, this.stepperPanel.getTotalSteps()));
        mainPanel.add(this.esp32SelectorPanel);
        mainPanel.add(this.sketchSelectorPanel);
        mainPanel.add(this.configureInstallationPanel);
        mainPanel.add(this.installationPanel);
        mainPanel.add(this.resumeAfterInstallationPanel);

        this.add(mainPanel, BorderLayout.CENTER);

    }
}
