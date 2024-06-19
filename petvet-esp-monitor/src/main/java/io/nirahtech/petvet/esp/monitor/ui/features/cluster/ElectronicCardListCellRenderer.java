package io.nirahtech.petvet.esp.monitor.ui.features.cluster;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import io.nirahtech.petvet.esp.monitor.data.ElectronicalCard;

public class ElectronicCardListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (value instanceof ElectronicalCard) {
            ElectronicalCard esp32 = (ElectronicalCard) value;
            setText(esp32.getId().toString().split("-")[0].toUpperCase());
        }
        
        return this;
    }
}
