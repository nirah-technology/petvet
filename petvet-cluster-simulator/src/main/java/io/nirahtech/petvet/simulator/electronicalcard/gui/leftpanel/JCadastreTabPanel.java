package io.nirahtech.petvet.simulator.electronicalcard.gui.leftpanel;

import java.awt.BorderLayout;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;

import io.nirahtech.petvet.simulator.land.domain.Land;
import io.nirahtech.petvet.simulator.land.gui.PetVetLandWindow;

final class JCadastreTabPanel extends JPanel {

    private final JButton createCadastreButton;
    private final AtomicReference<Land> landReference = new AtomicReference<>();

    private Consumer<Land> onPlotCreatedEventListener = null;

    JCadastreTabPanel() {
        super();
        this.setLayout(new BorderLayout());
        this.createCadastreButton = new JButton("Create");
        
        this.createCadastreButton.addActionListener(event -> {
            final PetVetLandWindow petvetLandWindow = new PetVetLandWindow();
            petvetLandWindow.setVisible(true);
            petvetLandWindow.setOnCadastreCreatedEventLister(land -> {
                petvetLandWindow.setVisible(false);
                petvetLandWindow.dispose();
                landReference.set(land);
                if (Objects.nonNull(onPlotCreatedEventListener)) {
                    onPlotCreatedEventListener.accept(land);
                }
            });
        });

        this.add(this.createCadastreButton, BorderLayout.NORTH);
    }

    public Optional<Land> getLand() {
        return Optional.ofNullable(this.landReference.get());
    }

    /**
     * @param onPlotCreatedEventListener the onPlotCreatedEventListener to set
     */
    public void addOnPlotCreatedEventListener(Consumer<Land> onPlotCreatedEventListener) {
        this.onPlotCreatedEventListener = onPlotCreatedEventListener;
    }
}
