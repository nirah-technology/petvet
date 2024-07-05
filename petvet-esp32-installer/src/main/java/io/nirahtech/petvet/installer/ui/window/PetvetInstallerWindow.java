package io.nirahtech.petvet.installer.ui.window;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import io.nirahtech.petvet.installer.domain.ESP32;
import io.nirahtech.petvet.installer.infrastructure.out.adapters.ESP32Usb;
import io.nirahtech.petvet.installer.infrastructure.out.ports.USB;
import io.nirahtech.petvet.installer.ui.panels.ConfigureInstallationPanel;
import io.nirahtech.petvet.installer.ui.panels.Esp32SelectorPanel;
import io.nirahtech.petvet.installer.ui.panels.InstallationPanel;
import io.nirahtech.petvet.installer.ui.panels.ResumeAfterInstallationPanel;
import io.nirahtech.petvet.installer.ui.panels.SketchSelectorPanel;
import io.nirahtech.petvet.installer.ui.widgets.jstepper.JStepPanel;
import io.nirahtech.templateprocessor.JinjaEngine;
import io.nirahtech.templateprocessor.TemplateEngine;

public class PetvetInstallerWindow extends JFrame {
    
    private final USB<ESP32> usb;

    private final JStepPanel stepPanel;

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

        final TemplateEngine templateEngine = new JinjaEngine();
        
        this.esp32SelectorPanel = new Esp32SelectorPanel(this.usb);
        this.sketchSelectorPanel = new SketchSelectorPanel();
        this.configureInstallationPanel = new ConfigureInstallationPanel(templateEngine);
        this.installationPanel = new InstallationPanel(templateEngine);
        this.resumeAfterInstallationPanel = new ResumeAfterInstallationPanel(this.usb);

        this.esp32SelectorPanel.onSelectedESP32s((esps) -> {
            this.configureInstallationPanel.setEsp32s(esps);
            this.configureInstallationPanel.synchronizeConfigurations();
        });


        this.stepPanel = new JStepPanel(); 
        this.stepPanel.addStep("Devices", "Select the ESP32s to setup", this.esp32SelectorPanel);
        this.stepPanel.addStep("Sketch", "Select the Sketch to install", this.sketchSelectorPanel);
        this.stepPanel.addStep("Configuration", "Configure installation", this.configureInstallationPanel);
        this.stepPanel.addStep("Installation", "Install Sketch on ESP32s", this.installationPanel);
        this.stepPanel.addStep("Resume", "Installation resume", this.resumeAfterInstallationPanel);
        this.add(this.stepPanel, BorderLayout.CENTER);
    
        this.sketchSelectorPanel.addOnSourceCodeChangedEventListener((sketch, sourceCode) -> {
            this.configureInstallationPanel.setSourceCode(sourceCode);
            this.configureInstallationPanel.synchronizeConfigurations();
            this.installationPanel.setSketch(sketch);
        });

        this.configureInstallationPanel.addOnConfigurationChanged(() -> {
            this.installationPanel.setESPs(this.configureInstallationPanel.getESP32sConfigurations());
        });

        

    }
}
