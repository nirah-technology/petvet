package io.nirahtech.petvet.installer.ui.components;

import java.util.List;
import java.util.Objects;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import io.nirahtech.petvet.installer.domain.ESP32;


// Lister le port USB
// Si la version sur port serie: 
// Afficher nom du logiciel
// Afficher version
// 

public class SelectedEsp32UsbTable extends JTable {

    private final Esp32UsbTableModel tableModel;

    public SelectedEsp32UsbTable(final List<ESP32> esp32s) {
        this.tableModel = new Esp32UsbTableModel(esp32s);
        this.setModel(this.tableModel);
    }


    public void refresh() {
        int selectedRow = this.getSelectedRow();
        this.tableModel.fireTableDataChanged();
        if (selectedRow > -1 && selectedRow < this.tableModel.getRowCount()) {
            this.setRowSelectionInterval(selectedRow, selectedRow);
        }
    }
    

    private final class Esp32UsbTableModel extends AbstractTableModel {
        private static final String[] COLUMNS_NAMES = { "Port", "ID", "Software", "Version"};

        private final List<ESP32> esp32s;

        public Esp32UsbTableModel(final List<ESP32> esp32s) {
            this.esp32s = esp32s;
        }

        @Override
        public int getColumnCount() {
            return COLUMNS_NAMES.length;
        }

        @Override
        public int getRowCount() {
            return this.esp32s.size();
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS_NAMES[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= 0 && rowIndex < this.esp32s.size()) {
                final ESP32 esp32 = this.esp32s.get(rowIndex);
                switch (columnIndex) {
                    case 0:
                        return esp32.getUsbPort();
                    case 1:
                        return Objects.nonNull(esp32.getId()) ? esp32.getId() : null;
                    case 2:
                        return Objects.nonNull(esp32.getSoftware()) ? esp32.getSoftware().name() : null;
                    case 3:
                        return Objects.nonNull(esp32.getSoftware()) ? esp32.getSoftware().version() : null;

                    default:
                        break;
                }
            }
            return null;
        }

    }
}
