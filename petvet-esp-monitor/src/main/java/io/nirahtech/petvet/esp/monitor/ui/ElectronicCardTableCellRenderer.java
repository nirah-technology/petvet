package io.nirahtech.petvet.esp.monitor.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import io.nirahtech.petvet.esp.monitor.ElectronicCard;

public class ElectronicCardTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        ElectronicCardTableModel model = (ElectronicCardTableModel) table.getModel();
        ElectronicCard device = model.getElectronicCards().get(row);

        if (device.isObsolete()) {
            c.setBackground(Color.RED);  // Changez la couleur de fond pour les devices obsolètes
        } else {
            c.setBackground(table.getBackground());  // Rétablissez la couleur de fond par défaut
        }

        return c;
    }
}
