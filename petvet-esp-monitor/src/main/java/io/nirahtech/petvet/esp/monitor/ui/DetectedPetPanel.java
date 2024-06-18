package io.nirahtech.petvet.esp.monitor.ui;

import java.util.Collection;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import io.nirahtech.petvet.esp.monitor.Device;

public class DetectedPetPanel extends JPanel {
    private final JSplitPane splitPane;

    private final Collection<Device> detectedDevices;
    private final JList<Device> detectedDevicesList;

    public DetectedPetPanel(final Collection<Device> detectedDevices) {
        this.detectedDevices = detectedDevices;

        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JPanel(), new JPanel());
        this.detectedDevicesList = new JList<>();
        this.detectedDevicesList.setCellRenderer(new DeviceListCellRenderer());

    }
}
