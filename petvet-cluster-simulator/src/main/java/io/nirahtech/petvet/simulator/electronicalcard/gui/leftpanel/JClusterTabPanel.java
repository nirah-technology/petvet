package io.nirahtech.petvet.simulator.electronicalcard.gui.leftpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;

import io.nirahtech.petvet.simulator.electronicalcard.Cluster;
import io.nirahtech.petvet.simulator.electronicalcard.MicroController;

final class JClusterTabPanel extends JPanel {

    private final AtomicReference<MicroController> selectMicroController = new AtomicReference<>(null);
    private Consumer<MicroController> onSelectionChangedEventListener = null;

    private final Cluster cluster;
    private final JElectronicCardList electronicCardList;

    private final JButton startClusterButton;
    private final JButton stopClusterButton;
    
    
    JClusterTabPanel(final Cluster cluster) {
        super();


        this.cluster = cluster;
        this.electronicCardList = new JElectronicCardList(this.cluster);
        this.startClusterButton = new JButton("Turn ON Cluster");
        this.stopClusterButton = new JButton("Turn OFF Cluster");

        this.startClusterButton.setEnabled(!this.cluster.isRunning());
        this.stopClusterButton.setEnabled(this.cluster.isRunning());

        this.startClusterButton.addActionListener(event -> {
            cluster.turnOn();
            this.startClusterButton.setEnabled(false);
            this.stopClusterButton.setEnabled(true);
            this.electronicCardList.reload();
        });

        this.stopClusterButton.addActionListener(event -> {
            cluster.turnOff();
            this.stopClusterButton.setEnabled(false);
            this.startClusterButton.setEnabled(true);
            this.electronicCardList.reload();
        });

        this.electronicCardList.setOnSelectionChangedEventListener(microController -> {
            this.selectMicroController.set(microController);
        });
        
        final JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new GridLayout(1, 2));
        actionsPanel.add(this.startClusterButton);
        actionsPanel.add(this.stopClusterButton);
        actionsPanel.setBackground(Color.RED);

        this.setLayout(new BorderLayout());
        this.add(this.electronicCardList, BorderLayout.CENTER);
        this.add(actionsPanel, BorderLayout.SOUTH);
    }

    public final Optional<MicroController> getSelectedMicroController() {
        return Optional.ofNullable(this.selectMicroController.get());
    }

    /**
     * @param onSelectionChangedEventListener the onSelectionChangedEventListener to set
     */
    public void setOnSelectionChangedEventListener(Consumer<MicroController> onSelectionChangedEventListener) {
        this.onSelectionChangedEventListener = onSelectionChangedEventListener;
        this.electronicCardList.setOnSelectionChangedEventListener(microController -> {
            this.selectMicroController.set(microController);
            onSelectionChangedEventListener.accept(microController);
        });
    }

    public void reload() {
        this.electronicCardList.reload();
    }

}
