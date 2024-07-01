package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import io.nirahtech.petvet.messaging.util.MacAddress;
import io.nirahtech.petvet.simulator.electronicalcard.Signal;

public final class SignalsStrengthsTable extends JTable {

    private final SignalsStrenghtsTableModel tableModel;

    public SignalsStrengthsTable(List<Map.Entry<MacAddress, Float>> signals) {
        super();
        this.tableModel = new SignalsStrenghtsTableModel(signals);
        super.setModel(this.tableModel);
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
                    return String.format("%s%% - (%s dBm)", item.getValue(), Signal.percentageToDbm(item.getValue()));
            }
            return null;
        }

        public void clear() {
            this.signals.clear();
            fireTableDataChanged(); // Notify the table that data has changed
        }

        public void addAll(List<Map.Entry<MacAddress, Float>> signals) {
            this.signals.addAll(signals);
            fireTableDataChanged();
        }
    }

    public void setSignals(Map<MacAddress, Float> signalsAsMap) {
        this.tableModel.clear();
        List<Map.Entry<MacAddress, Float>> signals = new ArrayList<>(signalsAsMap.entrySet());
        this.tableModel.addAll(signals);
    }
}
