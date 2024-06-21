package io.nirahtech.petvet.cluster.monitor.ui.features.reports;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import io.nirahtech.petvet.cluster.monitor.data.Device;

public final class DetectedDeviceTable extends JTable {
 

    public DetectedDeviceTable(final Set<Device> devices) {
        super(new DetectedDeviceTableModel(devices));
        this.setDefaultRenderer(Object.class, new ClusterTableCellRenderer());
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public final void refresh() {
        ((DetectedDeviceTableModel)this.getModel()).fireTableDataChanged();
    }

    private static final class ClusterTableCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            DetectedDeviceTableModel model = (DetectedDeviceTableModel) table.getModel();
            Device device = model.getElectronicCards().get(row);

            return cell;
        }
    }

    private static final class DetectedDeviceTableModel extends AbstractTableModel {

        private final String[] columnsNames = { "BSSID", "Signal Strength (dBm)" };
        private final Set<Device> devices;

        private DetectedDeviceTableModel(final Set<Device> devices) {
            this.devices = devices;
        }

        /**
         * @return the electronicCards
         */
        public List<Device> getElectronicCards() {
            return Collections.unmodifiableList(new ArrayList<>(this.devices));
        }

        @Override
        public int getRowCount() {
            return this.devices.size();
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
                synchronized(this.devices) {
                    Device device = new ArrayList<>(this.devices).get(rowIndex);
                    switch (columnIndex) {
                        case 0:
                            return device.getMac();
                        case 1:
                            return device.getSignalStrengthInDBm();
                    }
                }
            }
            return null;
        }

    }

}
