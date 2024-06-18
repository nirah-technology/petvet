package io.nirahtech.petvet.esp.monitor.ui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import io.nirahtech.petvet.esp.monitor.ESP32;

public class ESP32ListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (value instanceof ESP32) {
            ESP32 esp32 = (ESP32) value;
            setText(esp32.getId().toString().split("-")[0].toUpperCase());
        }
        
        return this;
    }
}
