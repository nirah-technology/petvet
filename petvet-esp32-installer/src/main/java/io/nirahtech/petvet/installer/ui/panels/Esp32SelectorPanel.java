package io.nirahtech.petvet.installer.ui.panels;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import io.nirahtech.petvet.installer.domain.ESP32;
import io.nirahtech.petvet.installer.infrastructure.out.ports.USB;
import io.nirahtech.petvet.installer.ui.components.JEsp32UsbTable;

public class Esp32SelectorPanel extends JPanel {

    private final USB<ESP32> usb;
    private ScheduledExecutorService scheduledExecutorService;

    private final JEsp32UsbTable availableEsp32UsbTable;
    private final JEsp32UsbTable selectedesp32UsbTable;

    private Consumer<Set<ESP32>> onSelectedESP32s = null;

    private final List<ESP32> esp32sAvailable = new ArrayList<>();
    private final List<ESP32> esp32sToConfigure = new ArrayList<>();

    public Esp32SelectorPanel(final USB<ESP32> usb) {
        super(new BorderLayout());
        this.usb = usb;

        // final JComboBox<Path> esp32Devices = new JComboBox<>(comboBoxModel);

        final JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        this.availableEsp32UsbTable = new JEsp32UsbTable(esp32sAvailable);
        this.selectedesp32UsbTable = new JEsp32UsbTable(esp32sToConfigure);

        // Panneau de gauche (availableEsp32UsbTable)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5); // marge
        panel.add(new JScrollPane(availableEsp32UsbTable), gbc);

        // Panneau du milieu (controlPanel)
        final JPanel controlPanel = new JPanel(new GridBagLayout());
        final JButton addToInstall = new JButton("Add");
        final JButton removeToInstall = new JButton("Remove");
        final JButton clearToInstall = new JButton("Clear");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        controlPanel.add(addToInstall, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(2, 0, 0, 0); // espace entre les boutons
        controlPanel.add(removeToInstall, gbc);


        gbc.gridy = 2;
        gbc.insets = new Insets(2, 0, 0, 0); // espace entre les boutons
        controlPanel.add(clearToInstall, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2; // permet au panneau de contrôle de s'étendre sur deux lignes
        gbc.weightx = 0.5; // moins de poids pour laisser plus de place aux panneaux de table
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(5, 5, 5, 5); // marge autour du panneau de contrôle
        panel.add(controlPanel, gbc);

        // Panneau de droite (selectedesp32UsbTable)
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1; // réinitialise la hauteur de la grille
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(selectedesp32UsbTable), gbc);

        this.add(panel, BorderLayout.CENTER);


        addToInstall.addActionListener((event) -> {
            this.availableEsp32UsbTable.getSelection().ifPresent(esp -> {
                if (!this.esp32sToConfigure.contains(esp)) {
                    this.esp32sToConfigure.add(esp);
                    if (Objects.nonNull(onSelectedESP32s)) {
                        onSelectedESP32s.accept(new HashSet<>(this.esp32sToConfigure));
                    }
                    selectedesp32UsbTable.refresh();
                }
            });
        });

        removeToInstall.addActionListener((event) -> {
            this.selectedesp32UsbTable.getSelection().ifPresent(esp -> {
                if (this.esp32sToConfigure.contains(esp)) {
                    this.esp32sToConfigure.remove(esp);
                    if (Objects.nonNull(onSelectedESP32s)) {
                        onSelectedESP32s.accept(new HashSet<>(this.esp32sToConfigure));
                    }
                    selectedesp32UsbTable.refresh();
                }
            });
        });

        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.startMonitoring();
    }

    private void startMonitoring() {
        this.scheduledExecutorService.scheduleAtFixedRate(() -> {
            esp32sAvailable.clear();
            final Set<ESP32> connectedESP32AsUSB = new HashSet<>(this.usb.list());
            esp32sAvailable.addAll(connectedESP32AsUSB);
            availableEsp32UsbTable.refresh();
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void onSelectedESP32s(final Consumer<Set<ESP32>> onSelectedESP32s) {
        this.onSelectedESP32s = onSelectedESP32s;
    }

}
