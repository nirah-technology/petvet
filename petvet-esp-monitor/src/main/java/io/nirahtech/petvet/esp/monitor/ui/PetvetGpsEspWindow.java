package io.nirahtech.petvet.esp.monitor.ui;

import java.awt.BorderLayout;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import io.nirahtech.petvet.esp.monitor.ESP32;
import io.nirahtech.petvet.esp.monitor.IPV4Address;
import io.nirahtech.petvet.esp.monitor.MacAddress;
import io.nirahtech.petvet.esp.monitor.Mode;
import io.nirahtech.petvet.esp.monitor.brokers.MessageBroker;

public class PetvetGpsEspWindow extends JFrame {

    private final Set<ESP32> esps;

    // private final Set<ScanReport> scanReports = new HashSet<>();
    // private ESP32 selectedESP = null;

    // private AtomicBoolean mustDisplayEspPanel = new AtomicBoolean(true);

    // private final JList<ESP32> espList;
    // private final JList<Device> devicesList;

    // private final JList<ScanReport> scanReportsList;
    private final MessageBroker messageBroker;

    private final NetworkingPanel networkingPanel;

    private final JTabbedPane tabbedPane;

    private final ReceivedMessagesPanel receivedMessagesPanel;
    private final ESPInventoryPanel espInventoryPanel;

    // private final JPanel rootPanel;
    // private final JPanel espPanel;
    // private final JPanel devicesPanel;


    public PetvetGpsEspWindow(final MessageBroker messageBroker, InetAddress multicastGroup, int multicastPort) {
        super("PETVET ESP MONITOR");
        this.messageBroker = messageBroker;
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.esps = new HashSet<>();
        // this.esps.add(new ESP32(UUID.randomUUID(), MacAddress.of("11:11:11:11:11:11"), IPV4Address.of("127.0.0.1"), Mode.NATIVE_NODE));

        // final DefaultListModel<ESP32> espListModel = new DefaultListModel<>();
        // espListModel.addAll(esps);
        // this.espList = new JList<>(espListModel);
        // this.espList.setCellRenderer(new ESP32ListCellRenderer());
        
        // final DefaultListModel<Device> devicesListModel = new DefaultListModel<>();
        // this.devicesList = new JList<>(devicesListModel);
        // this.devicesList.setCellRenderer(new ESP32ListCellRenderer());

        // final DefaultListModel<ScanReport> scanReportsListModel = new DefaultListModel<>();
        // this.scanReportsList = new JList<>(scanReportsListModel);
        // this.scanReportsList.setCellRenderer(new ESP32ListCellRenderer());

        this.networkingPanel = new NetworkingPanel(this.messageBroker, multicastGroup, multicastPort);
        this.add(this.networkingPanel, BorderLayout.NORTH);

        this.receivedMessagesPanel = new ReceivedMessagesPanel();
        this.espInventoryPanel = new ESPInventoryPanel(this.esps);

        this.tabbedPane = new JTabbedPane();
        // tabs.setPreferredSize(new Dimension(260, 0));
        // tabs.setTabPlacement(JTabbedPane.TOP);

        this.tabbedPane.addTab("Messages", this.receivedMessagesPanel);
        this.add(this.tabbedPane, BorderLayout.CENTER);
        this.tabbedPane.addTab("Cluter des ESP", new JScrollPane(this.espInventoryPanel));
        this.tabbedPane.addTab("Rapports de Scan", new JScrollPane());
        this.tabbedPane.addTab("Animaux Detectés", new JScrollPane());
        // this.tabbedPane.addTab("Animaux", new JScrollPane(this.devicesList));

        // tabs.addChangeListener(new ChangeListener() {
        //     @Override
        //     public void stateChanged(ChangeEvent e) {
        //         JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
        //         int index = sourceTabbedPane.getSelectedIndex();
        //         if (index == 0) {
        //             rootPanel.remove(devicesPanel);
        //             rootPanel.revalidate();
        //             rootPanel.repaint();
        //             rootPanel.add(espPanel);
        //             rootPanel.revalidate();
        //             rootPanel.repaint();
        //         } else {
        //             rootPanel.remove(espPanel);
        //             rootPanel.revalidate();
        //             rootPanel.repaint();
        //             rootPanel.add(devicesPanel);
        //             rootPanel.revalidate();
        //             rootPanel.repaint();
        //         }
        //     }
        // });

        // this.rootPanel = new JPanel(new BorderLayout());

        // this.espPanel = this.createEspMainPanel(); 
        // this.devicesPanel = this.createDevicesMainPanel(); 

        // this.rootPanel.add(this.espPanel, BorderLayout.CENTER);

        // JScrollPane scrollEditor = new JScrollPane(this.rootPanel);

        // JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabs, scrollEditor);

        // JPanel contentPane = (JPanel) getContentPane();
        // contentPane.add(splitter, BorderLayout.CENTER);

        this.esps.add(new ESP32(UUID.randomUUID(), MacAddress.of("AA:AA:AA:AA:AA:AA"), IPV4Address.of("127.0.0.1"), Mode.ORCHESTRATOR_NODE));

    }

    // private final JPanel createEspMainPanel() {
    //     JPanel panel = new JPanel(new BorderLayout());
    //     panel.add(this.scanReportsList, BorderLayout.WEST);
    //     return panel;
    // }


    // private final JPanel createDevicesMainPanel() {
    //     JPanel panel = new JPanel(new BorderLayout());
    //     return panel;
    // }

}
