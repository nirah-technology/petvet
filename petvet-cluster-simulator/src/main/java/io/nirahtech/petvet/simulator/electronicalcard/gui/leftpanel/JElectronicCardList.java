package io.nirahtech.petvet.simulator.electronicalcard.gui.leftpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import io.nirahtech.petvet.simulator.electronicalcard.Cluster;
import io.nirahtech.petvet.simulator.electronicalcard.ElectronicCard;
import io.nirahtech.petvet.simulator.electronicalcard.MicroController;

public final class JElectronicCardList extends JList<MicroController> {
    
    private final JElectronicCardListModel model;
    private final JElectronicCardListRenderer renderer;
    private transient Consumer<MicroController> onSelectionChangedEventListener = null;

    public JElectronicCardList(final Cluster cluster) {
        super();
        this.model = new JElectronicCardListModel(cluster);
        this.renderer = new JElectronicCardListRenderer();
        this.setModel(this.model);
        this.setCellRenderer(this.renderer);

        this.addListSelectionListener(new ListSelectionListener() {
            
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    if (Objects.nonNull(onSelectionChangedEventListener)) {
                        int selectedIndex = getSelectedIndex();
                        if (selectedIndex != -1) {
                            MicroController selectedMicroController = getModel().getElementAt(selectedIndex);
                            onSelectionChangedEventListener.accept(selectedMicroController);
                        }
                    }
                }
            }
        });
    }

    public void reload() {
        this.model.reloadDatastore();
    }

    public Optional<MicroController> getSelection() {
        return this.model.getSelection();
    }

    public void setOnSelectionChangedEventListener(Consumer<MicroController> onSelectionChangedEventListener) {
        this.onSelectionChangedEventListener = onSelectionChangedEventListener;
    }

    private final class JElectronicCardListModel extends DefaultListModel<MicroController> {
        
        private final Cluster cluster;

        private JElectronicCardListModel(final Cluster cluster) {
            super();
            this.cluster = cluster;
            this.reloadDatastore();
        }

        private Optional<MicroController> getSelection() {
            MicroController electronicCard = null;
            final int selectedIndex = JElectronicCardList.this.getSelectedIndex();
            if (selectedIndex >= 0) {
                electronicCard = getElementAt(selectedIndex);
            }
            return Optional.ofNullable(electronicCard);
        }

        private void reloadDatastore() {
            this.removeAllElements();
            this.cluster.nodes().forEach(microController -> {
                this.addElement(microController);
            });
        }

    }

    private final class JElectronicCardListRenderer extends DefaultListCellRenderer {
        
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, 
                Object value, 
                int index, 
                boolean isSelected,
                boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            ElectronicCard electronicCard = (ElectronicCard) value;
            final JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            final JLabel name = new JLabel(electronicCard.getProcess().getMac().toString());
            final JLabel status = new JLabel(new StatusIcon(electronicCard.isRunning()));
            panel.add(name, BorderLayout.WEST);
            panel.add(status, BorderLayout.EAST);

            if (isSelected) {
                panel.setBackground(new Color(100,0,0));
            }
            return panel;
        }
    }
}
