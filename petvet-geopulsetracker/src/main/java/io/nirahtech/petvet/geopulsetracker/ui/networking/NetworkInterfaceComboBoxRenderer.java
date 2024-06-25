package io.nirahtech.petvet.geopulsetracker.ui.networking;

import javax.swing.*;
import java.awt.*;
import java.net.NetworkInterface;

/**
 * NetworkInterfaceComboBoxRenderer
 */

public class NetworkInterfaceComboBoxRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value instanceof NetworkInterface) {
            NetworkInterface networkInterface = (NetworkInterface) value;
            setText(networkInterface.getName());
        }

        return this;
    }
}
