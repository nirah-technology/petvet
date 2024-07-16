package io.nirahtech.petvet.simulator.cadastre.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.util.function.Consumer;

import javax.swing.JFrame;

import io.nirahtech.petvet.simulator.cadastre.domain.CadastralPlan;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.JCadastrePlanTree;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers.JLayersPanel;

public class PetVetLandWindow extends JFrame {

    private final CadastralPlan cadastralPlan;
    private final JDrawerPanel drawerPanel;
    private final JLayersPanel layersPanel;

    public PetVetLandWindow() {
        super("PetVet : Land Simulator");
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());

        this.cadastralPlan = new CadastralPlan();
        
        this.drawerPanel = new JDrawerPanel(this.cadastralPlan);
        this.layersPanel = new JLayersPanel(this.cadastralPlan);

        this.layersPanel.setOnSelectedLayerEventListerner(layer -> {
            this.drawerPanel.setSelectedLayer(layer);
            this.layersPanel.setSelectedLayer(layer);

        });

        this.layersPanel.setOnLockChangeOnLayer(layer -> {
            this.drawerPanel.redraw();
        });

        this.layersPanel.setOnVisibilityChangeOnLayer(layer -> {
            this.drawerPanel.redraw();
        });


        this.add(this.drawerPanel, BorderLayout.CENTER);
        this.add(new JCadastrePlanTree(cadastralPlan), BorderLayout.EAST);

    }


    public final void setOnCadastreCreatedEventLister(Consumer<CadastralPlan> onCadastreCreatedEventLister) {
        this.layersPanel.setOnCadastreCreatedEventLister(onCadastreCreatedEventLister);
    }
}
