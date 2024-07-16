package io.nirahtech.petvet.simulator.electronicalcard.gui.leftpanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import io.nirahtech.petvet.simulator.cadastre.domain.CadastralPlan;
import io.nirahtech.petvet.simulator.electronicalcard.Cluster;
import io.nirahtech.petvet.simulator.electronicalcard.Configuration;
import io.nirahtech.petvet.simulator.electronicalcard.MicroController;

public final class JTabbedMenuPanel extends JPanel {


    private transient Consumer<MicroController> onSelectionChangedEventListener = null;
    
    private final JTabbedPane tabbedPane;

    private final JClusterTabPanel clusterTabPanel;
    private final JZooTabPanel zooTabPanel;
    private final JCadastreTabPanel cadastreTabPanel;

    public JTabbedMenuPanel(Configuration configuration, Cluster cluster) {
        super();

        this.tabbedPane = new JTabbedPane();
        this.clusterTabPanel = new JClusterTabPanel(cluster);
        this.zooTabPanel = new JZooTabPanel();
        this.cadastreTabPanel = new JCadastreTabPanel();

        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(300, this.getPreferredSize().height));
        this.tabbedPane.addTab("Cadastre", new JScrollPane(this.cadastreTabPanel));
        this.tabbedPane.addTab("Pets Parc", new JScrollPane(this.zooTabPanel));
        this.tabbedPane.addTab("Detectors", new JScrollPane(this.clusterTabPanel));
        this.add(this.tabbedPane, BorderLayout.CENTER);
    }

    /**
     * @param onSelectionChangedEventListener the onSelectionChangedEventListener to set
     */
    public void setOnSelectionChangedEventListener(Consumer<MicroController> onSelectionChangedEventListener) {
        this.onSelectionChangedEventListener = onSelectionChangedEventListener;
        this.clusterTabPanel.setOnSelectionChangedEventListener(onSelectionChangedEventListener);
    }

    public void reload() {
        this.clusterTabPanel.reload();
    }


    /**
     * @param onCadastralPlanCreatedEventListener the onCadastralPlanCreatedEventListener to set
     */
    public void addOnPlotCreatedEventListener(Consumer<CadastralPlan> onCadastralPlanCreatedEventListener) {
        this.cadastreTabPanel.addOnCadastralPlanCreatedEventListener(onCadastralPlanCreatedEventListener);
    }
}
