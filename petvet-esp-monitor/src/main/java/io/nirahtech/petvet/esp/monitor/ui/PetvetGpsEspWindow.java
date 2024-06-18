package io.nirahtech.petvet.esp.monitor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import io.nirahtech.petvet.esp.monitor.Device;
import io.nirahtech.petvet.esp.monitor.ESP32;
import io.nirahtech.petvet.esp.monitor.IPV4Address;
import io.nirahtech.petvet.esp.monitor.MacAddress;
import io.nirahtech.petvet.esp.monitor.Mode;
import io.nirahtech.petvet.esp.monitor.ScanReport;

public class PetvetGpsEspWindow extends JFrame {

    private static final String CONNECTIVITY_BUTTON_TEXT_CONNECT = "Connect";
    private static final String CONNECTIVITY_BUTTON_TEXT_DISCONNECT = "Disonnect";

    private final Set<ESP32> esps;

    private final Set<ScanReport> scanReports = new HashSet<>();
    private final ESP32 selectedESP = null;

    private AtomicBoolean mustDisplayEspPanel = new AtomicBoolean(true);

    private final JList<ESP32> espList;
    private final JList<Device> devicesList;

    private final JList<ScanReport> scanReportsList;

    private final JTextField multicastGroupTextFiels;
    private final JTextField multicastPortTextFiels;

    private final JPanel rootPanel;
    private final JPanel espPanel;
    private final JPanel devicesPanel;

    private final JButton connectivityButton;
    private boolean isConnectedToMulticastGroup = false;

    public PetvetGpsEspWindow() {
        super("PETVET ESP MONITOR");
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.esps = new HashSet<>();
        this.esps.add(new ESP32(UUID.randomUUID(), MacAddress.of("11:11:11:11:11:11"), IPV4Address.of("127.0.0.1"), Mode.NATIVE_NODE));

        final DefaultListModel<ESP32> espListModel = new DefaultListModel<>();
        espListModel.addAll(esps);
        this.espList = new JList<>(espListModel);
        this.espList.setCellRenderer(new ESP32ListCellRenderer());
        
        final DefaultListModel<Device> devicesListModel = new DefaultListModel<>();
        this.devicesList = new JList<>(devicesListModel);
        this.devicesList.setCellRenderer(new ESP32ListCellRenderer());

        final DefaultListModel<ScanReport> scanReportsListModel = new DefaultListModel<>();
        this.scanReportsList = new JList<>(scanReportsListModel);
        this.scanReportsList.setCellRenderer(new ESP32ListCellRenderer());

        this.multicastGroupTextFiels = new JTextField("224.0.10.1");
        this.multicastPortTextFiels = new JTextField("4488");
        this.isConnectedToMulticastGroup = false;
        this.connectivityButton = new JButton(CONNECTIVITY_BUTTON_TEXT_CONNECT);
        this.connectivityButton.addActionListener(event -> {
            this.isConnectedToMulticastGroup = !this.isConnectedToMulticastGroup;
            if (this.isConnectedToMulticastGroup) {
                this.connectivityButton.setText(CONNECTIVITY_BUTTON_TEXT_DISCONNECT);
            } else {
                this.connectivityButton.setText(CONNECTIVITY_BUTTON_TEXT_CONNECT);
            }
            this.multicastGroupTextFiels.setEditable(!this.isConnectedToMulticastGroup);
            this.multicastPortTextFiels.setEditable(!this.isConnectedToMulticastGroup);
        });

        final JPanel networkPanel = new JPanel(new GridLayout(1, 3));
        networkPanel.add(this.multicastGroupTextFiels);
        networkPanel.add(this.multicastPortTextFiels);
        networkPanel.add(this.connectivityButton);

        this.add(networkPanel, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setPreferredSize(new Dimension(260, 0));
        tabs.setTabPlacement(JTabbedPane.TOP);

        tabs.addTab("ESP", new JScrollPane(this.espList));
        tabs.addTab("Animaux", new JScrollPane(this.devicesList));

        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
                int index = sourceTabbedPane.getSelectedIndex();
                if (index == 0) {
                    rootPanel.remove(devicesPanel);
                    rootPanel.revalidate();
                    rootPanel.repaint();
                    rootPanel.add(espPanel);
                    rootPanel.revalidate();
                    rootPanel.repaint();
                } else {
                    rootPanel.remove(espPanel);
                    rootPanel.revalidate();
                    rootPanel.repaint();
                    rootPanel.add(devicesPanel);
                    rootPanel.revalidate();
                    rootPanel.repaint();
                }
            }
        });

        this.rootPanel = new JPanel(new BorderLayout());

        this.espPanel = this.createEspMainPanel(); 
        this.devicesPanel = this.createDevicesMainPanel(); 

        this.rootPanel.add(this.espPanel, BorderLayout.CENTER);

        JScrollPane scrollEditor = new JScrollPane(this.rootPanel);

        JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabs, scrollEditor);

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.add(splitter, BorderLayout.CENTER);

    }

    private final JPanel createEspMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(this.scanReportsList, BorderLayout.WEST);
        return panel;
    }


    private final JPanel createDevicesMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        return panel;
    }

}
