package io.nirahtech.petvet.installer.ui.components;

import java.awt.Component;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import io.nirahtech.petvet.installer.domain.ESP32;

// Lister le port USB
// Si la version sur port serie: 
// Afficher nom du logiciel
// Afficher version
// 

public class AvailableEsp32UsbTable extends JTable {

    private final Esp32UsbTableModel tableModel;
    private final Esp32UsbTableCellRenderer tableCellRenderer;

    public AvailableEsp32UsbTable(final List<ESP32> esp32s) {
        this.tableModel = new Esp32UsbTableModel(esp32s);
        this.tableCellRenderer = new Esp32UsbTableCellRenderer();
        this.setModel(this.tableModel);
        this.setDefaultRenderer(Object.class, this.tableCellRenderer);
    }

    public final Optional<ESP32> getSelection() {
        return this.tableCellRenderer.getSelectedESP();
    }

    public void refresh() {
        int selectedRow = this.getSelectedRow();
        this.tableModel.fireTableDataChanged();
        if (selectedRow > -1 && selectedRow < this.tableModel.getRowCount()) {
            this.setRowSelectionInterval(selectedRow, selectedRow);
        }
    }

    private final class Esp32UsbTableModel extends AbstractTableModel {
        private static final String[] COLUMNS_NAMES = { "Port", "ID", "Software", "Version" };

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

        public List<ESP32> getEsp32s() {
            return esp32s;
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

    private final class Esp32UsbTableCellRenderer extends DefaultTableCellRenderer {

        private ESP32 selectedESP = null;

        public Optional<ESP32> getSelectedESP() {
            return Optional.ofNullable(this.selectedESP);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            Esp32UsbTableModel model = (Esp32UsbTableModel) table.getModel();
            ESP32 esp = model.getEsp32s().get(row);
            if (isSelected) {
                if (selectedESP != esp) {
                    selectedESP = esp;
                }
                cell.setBackground(table.getSelectionBackground()); // Changez la couleur de fond pour les devices obsolètes
                cell.setForeground(table.getSelectionForeground()); // Changez la couleur de fond pour les devices obsolètes
            } else {
                cell.setForeground(table.getForeground()); // Rétablissez la couleur de fond par défaut
                cell.setBackground(table.getBackground()); // Rétablissez la couleur de fond par défaut
            }
            return cell;
        }

    }
}
