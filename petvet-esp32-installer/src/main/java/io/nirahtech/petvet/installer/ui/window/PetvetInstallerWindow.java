package io.nirahtech.petvet.installer.ui.window;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import io.nirahtech.petvet.installer.infrastructure.out.adapters.ESP32Usb;
import io.nirahtech.petvet.installer.infrastructure.out.ports.USB;
import io.nirahtech.petvet.installer.ui.panels.ConfigureInstallationPanel;
import io.nirahtech.petvet.installer.ui.panels.Esp32SelectorPanel;
import io.nirahtech.petvet.installer.ui.panels.InstallationPanel;
import io.nirahtech.petvet.installer.ui.panels.ResumeAfterInstallationPanel;
import io.nirahtech.petvet.installer.ui.panels.SketchSelectorPanel;
import io.nirahtech.petvet.installer.ui.stepper.Step;
import io.nirahtech.petvet.installer.ui.stepper.Stepper;
import io.nirahtech.petvet.installer.ui.stepper.StepperPanel;

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
        
        this.esp32SelectorPanel = new Esp32SelectorPanel(this.usb, this.stepper);
        this.sketchSelectorPanel = new SketchSelectorPanel(this.stepper);
        this.configureInstallationPanel = new ConfigureInstallationPanel(this.stepper);
        this.installationPanel = new InstallationPanel(this.stepper);
        this.resumeAfterInstallationPanel = new ResumeAfterInstallationPanel(this.usb, this.stepper);

        
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
        final JPanel mainPanel = new JPanel(new BorderLayout());

        this.stepperPanel.onSelectedStepEventListenerHandler(selectedStep -> {
            mainPanel.removeAll();
            mainPanel.add(selectedStep.getGraphicComponent(), BorderLayout.CENTER);
            this.stepper.getSteps().forEach(step -> {
                step.getGraphicComponent().setVisible(false);
            });
            selectedStep.getGraphicComponent().setVisible(true);
            this.stepperPanel.redraw();
        });

        

        this.add(mainPanel, BorderLayout.CENTER);
        step1.select();

    }
}
