package io.nirahtech.petvet.esp.monitor.ui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import io.nirahtech.petvet.esp.monitor.Device;

public class DeviceListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (value instanceof Device) {
            Device device = (Device) value;
            setText("MAC: " + device.getMac() + ", IP: " + device.getIp() + ", Signal Strength: " + device.getSignalStrengthInDBm() + " dBm");
        }
        
        return this;
    }
}
