package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import io.nirahtech.petvet.messaging.util.MacAddress;

public final class SignalsStrengthsTable extends JTable {

    private final SignalsStrenghtsTableModel tableModel;

    public SignalsStrengthsTable(List<Map.Entry<MacAddress, Float>> signals) {
        super();
        this.tableModel = new SignalsStrenghtsTableModel(signals);
        super.setModel(dataModel);
    }

    private final class SignalsStrenghtsTableModel extends AbstractTableModel {
        private static final String[] COLUMNS_NAMES = { "Source", "Signal" };

        private final List<Map.Entry<MacAddress, Float>> signals;

        public SignalsStrenghtsTableModel(List<Map.Entry<MacAddress, Float>> signals) {
            this.signals = signals;
        }

        @Override
        public int getColumnCount() {
            return COLUMNS_NAMES.length;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS_NAMES[column];
        }

        @Override
        public int getRowCount() {
            return this.signals.size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Map.Entry<MacAddress, Float> item = this.signals.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return item.getKey();
                case 1:
                    return item.getValue();
            }
            return null;

        }
    }

}
