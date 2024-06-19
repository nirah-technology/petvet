package io.nirahtech.petvet.esp.monitor.ui;

import java.awt.BorderLayout;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import io.nirahtech.petvet.esp.monitor.Device;
import io.nirahtech.petvet.esp.monitor.ElectronicCard;
import io.nirahtech.petvet.esp.monitor.HeartBeat;
import io.nirahtech.petvet.esp.monitor.MonitorTask;
import io.nirahtech.petvet.esp.monitor.ScanReport;
import io.nirahtech.petvet.messaging.brokers.MessageBroker;
import io.nirahtech.petvet.messaging.messages.Message;
import io.nirahtech.petvet.messaging.util.MacAddress;

public class PetvetGpsEspWindow extends JFrame {

    private final List<Message> receivedMessages;
    private final Map<MacAddress, Set<HeartBeat>> heartBeats;
    private final SortedSet<ElectronicCard> esps;
    private final SortedSet<ScanReport> scanReports;
    private final SortedSet<Device> detectedDevices;
    private final MonitorTask monitorTask;
    
    private final MessageBroker messageBroker;

    private final NetworkingPanel networkingPanel;

    private final JTabbedPane tabbedPane;

    private final ReceivedMessagesPanel receivedMessagesPanel;
    private final ElectronicCardsInventoryPanel espInventoryPanel;


    public PetvetGpsEspWindow(final MessageBroker messageBroker, final InetAddress multicastGroup, final int multicastPort, final List<Message> receivedMessages, final SortedSet<ElectronicCard> esps, final SortedSet<ScanReport> scanReports, final SortedSet<Device> detectedDevices, final Map<MacAddress, Set<HeartBeat>> heartBeats, final MonitorTask monitorTask) {
        super("PETVET ESP MONITOR");
        this.messageBroker = messageBroker;
        this.receivedMessages = receivedMessages;
        this.esps = esps;
        this.scanReports = scanReports;
        this.detectedDevices = detectedDevices;
        this.heartBeats = heartBeats;
        this.monitorTask = monitorTask;

        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        
        this.networkingPanel = new NetworkingPanel(this.messageBroker, multicastGroup, multicastPort);
        this.add(this.networkingPanel, BorderLayout.NORTH);

        this.receivedMessagesPanel = new ReceivedMessagesPanel(this.receivedMessages, this.monitorTask);
        this.espInventoryPanel = new ElectronicCardsInventoryPanel(this.esps, this.heartBeats, this.monitorTask);

        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.setTabPlacement(JTabbedPane.TOP);
        this.tabbedPane.addTab("Messages", this.receivedMessagesPanel);
        this.tabbedPane.addTab("Cluster des ESP", new JScrollPane(this.espInventoryPanel));
        this.tabbedPane.addTab("Rapports de Scan", new JScrollPane());
        this.tabbedPane.addTab("Animaux Detectés", new JScrollPane());
        this.add(this.tabbedPane, BorderLayout.CENTER);

    }

}
