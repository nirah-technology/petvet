package io.nirahtech.petvet.simulator.electronicalcard.gui.leftpanel;

import java.awt.BorderLayout;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;

import io.nirahtech.petvet.simulator.cadastre.domain.CadastralPlan;
import io.nirahtech.petvet.simulator.cadastre.gui.PetVetLandWindow;

final class JCadastreTabPanel extends JPanel {

    private final JButton createCadastreButton;
    private final AtomicReference<CadastralPlan> cadastralPlanReference = new AtomicReference<>();

    private Consumer<CadastralPlan> onCadastralPlanCreatedEventListener = null;

    JCadastreTabPanel() {
        super();
        this.setLayout(new BorderLayout());
        this.createCadastreButton = new JButton("Create");
        
        this.createCadastreButton.addActionListener(event -> {
            final PetVetLandWindow petvetLandWindow = new PetVetLandWindow();
            petvetLandWindow.setVisible(true);
            petvetLandWindow.setOnCadastreCreatedEventLister(cadastralPlan -> {
                petvetLandWindow.setVisible(false);
                petvetLandWindow.dispose();
                cadastralPlanReference.set(cadastralPlan);
                if (Objects.nonNull(onCadastralPlanCreatedEventListener)) {
                    onCadastralPlanCreatedEventListener.accept(cadastralPlan);
                }
            });
        });

        this.add(this.createCadastreButton, BorderLayout.NORTH);
    }

    public Optional<CadastralPlan> getLand() {
        return Optional.ofNullable(this.cadastralPlanReference.get());
    }

    /**
     * @param onPlotCreatedEventListener the onPlotCreatedEventListener to set
     */
    public void addOnCadastralPlanCreatedEventListener(Consumer<CadastralPlan> onCadastralPlanCreatedEventListener) {
        this.onCadastralPlanCreatedEventListener = onCadastralPlanCreatedEventListener;
    }
}
