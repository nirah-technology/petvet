package io.nirahtech.petvet.simulator.electronicalcard.gui.leftpanel;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import io.nirahtech.petvet.simulator.land.gui.PetVetLandWindow;

final class JCadastreTabPanel extends JPanel {

    private final JButton createCadastreButton;

    JCadastreTabPanel() {
        super();
        this.setLayout(new BorderLayout());
        this.createCadastreButton = new JButton("Create");
        
        this.createCadastreButton.addActionListener(event -> {
            PetVetLandWindow petvetLandWindow = new PetVetLandWindow();
            petvetLandWindow.setVisible(true);
        });

        this.add(this.createCadastreButton, BorderLayout.NORTH);
    }
}
