package io.nirahtech.petvet.cluster.monitor.ui.features.cluster;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import io.nirahtech.petvet.cluster.monitor.data.ElectronicalCard;

public final class ClusterTable extends JTable {
 

    public ClusterTable(final SortedSet<ElectronicalCard> cluster) {
        super(new ClusterTableModel(cluster));
        this.setDefaultRenderer(Object.class, new ClusterTableCellRenderer());
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public final void refresh() {
        ((ClusterTableModel)this.getModel()).fireTableDataChanged();
    }

    private static final class ClusterTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            ClusterTableModel model = (ClusterTableModel) table.getModel();
            ElectronicalCard device = model.getElectronicCards().get(row);

            if (device.isObsolete()) {
                cell.setBackground(new Color(161, 9, 5)); // Changez la couleur de fond pour les devices obsolètes
                cell.setForeground(Color.WHITE);
            } else {
                if (isSelected) {
                    cell.setBackground(new Color(102, 187, 106)); // Rétablissez la couleur de fond par défaut
                    cell.setForeground(Color.BLACK);
                } else {
                    cell.setBackground(table.getBackground()); // Rétablissez la couleur de fond par défaut
                    cell.setForeground(table.getForeground());
                }
            }

            return cell;
        }
    }

    private static final class ClusterTableModel extends AbstractTableModel {

        private final String[] columnsNames = { "ID", "MAC", "IP", "Mode", "Uptime", "Température (°C)", "Consomation",
                "Localisation", "Date/Time Dernier Message" };
        private final SortedSet<ElectronicalCard> electronicCards;

        private ClusterTableModel(final SortedSet<ElectronicalCard> electronicCards) {
            this.electronicCards = electronicCards;
        }

        /**
         * @return the electronicCards
         */
        public List<ElectronicalCard> getElectronicCards() {
            return Collections.unmodifiableList(new ArrayList<>(this.electronicCards));
        }

        @Override
        public int getRowCount() {
            return this.electronicCards.size();
        }

        @Override
        public int getColumnCount() {
            return this.columnsNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return this.columnsNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= 0) {
                ElectronicalCard electronicCard = new ArrayList<>(this.electronicCards).get(rowIndex);
                switch (columnIndex) {
                    case 0:
                        return electronicCard.getId();
                    case 1:
                        return electronicCard.getMac();
                    case 2:
                        return electronicCard.getIp();
                    case 3:
                        return electronicCard.getMode();
                    case 4:
                        return (electronicCard.getUptime().isPresent()) ? electronicCard.getUptime().get() : "NC";
                    case 5:
                        return (electronicCard.getTemperatureInCelcus().isPresent())
                                ? electronicCard.getTemperatureInCelcus().get()
                                : "NC";
                    case 6:
                        return (electronicCard.getConsumptionInVolt().isPresent())
                                ? electronicCard.getConsumptionInVolt().get()
                                : "NC";
                    case 7:
                        return (electronicCard.getLocation().isPresent()) ? electronicCard.getLocation().get() : "NC";
                    case 8:
                        return "NC";
                }
            }
            return null;
        }

    }

}
