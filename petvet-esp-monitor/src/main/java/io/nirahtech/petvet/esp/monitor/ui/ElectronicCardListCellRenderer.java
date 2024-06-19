package io.nirahtech.petvet.esp.monitor.ui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import io.nirahtech.petvet.esp.monitor.ElectronicCard;

public class ElectronicCardListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (value instanceof ElectronicCard) {
            ElectronicCard esp32 = (ElectronicCard) value;
            setText(esp32.getId().toString().split("-")[0].toUpperCase());
        }
        
        return this;
    }
}
