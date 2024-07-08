package io.nirahtech.petvet.cluster.monitor.ui.windows;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import io.nirahtech.petvet.cluster.monitor.MonitorTask;
import io.nirahtech.petvet.cluster.monitor.data.Device;
import io.nirahtech.petvet.cluster.monitor.data.ElectronicalCard;
import io.nirahtech.petvet.cluster.monitor.data.HeartBeat;
import io.nirahtech.petvet.cluster.monitor.data.ScanNow;
import io.nirahtech.petvet.cluster.monitor.data.ScanReport;
import io.nirahtech.petvet.cluster.monitor.ui.features.cluster.ClusterPanel;
import io.nirahtech.petvet.cluster.monitor.ui.features.messages.ReceivedMessagesPanel;
import io.nirahtech.petvet.cluster.monitor.ui.features.reports.ScanReportsPanel;
import io.nirahtech.petvet.messaging.brokers.MessageBroker;
import io.nirahtech.petvet.messaging.messages.Message;
import io.nirahtech.petvet.messaging.util.MacAddress;

public class PetvetClusterMonitorWindow extends JFrame {

    private final List<Message> receivedMessages;
    private final Map<MacAddress, Set<HeartBeat>> heartBeats;
    private final SortedSet<ElectronicalCard> esps;
    private final Map<ScanNow, SortedSet<ScanReport>> scanReports;
    private final SortedSet<Device> detectedDevices;
    private final MonitorTask monitorTask;

    private final MessageBroker messageBroker;

    private final JTabbedPane tabbedPane;

    private final ReceivedMessagesPanel receivedMessagesPanel;
    private final ClusterPanel espInventoryPanel;
    private final ScanReportsPanel scanReportsPanel;

    public PetvetClusterMonitorWindow(final MessageBroker messageBroker, final InetAddress multicastGroup,
            final int multicastPort, final List<Message> receivedMessages, final SortedSet<ElectronicalCard> esps,
            final Map<ScanNow, SortedSet<ScanReport>> scanReports, final SortedSet<Device> detectedDevices,
            final Map<MacAddress, Set<HeartBeat>> heartBeats, final MonitorTask monitorTask) {
        super("NIRAH-TECHNOLOGY : PetVet Monitor - Control Panel");
        this.messageBroker = messageBroker;
        this.receivedMessages = receivedMessages;
        this.esps = esps;
        this.scanReports = scanReports;
        this.detectedDevices = detectedDevices;
        this.heartBeats = heartBeats;
        this.monitorTask = monitorTask;

        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.receivedMessagesPanel = new ReceivedMessagesPanel(this.receivedMessages, this.monitorTask);
        this.espInventoryPanel = new ClusterPanel(this.esps, this.heartBeats, this.monitorTask);
        this.scanReportsPanel = new ScanReportsPanel(this.esps, this.scanReports, this.monitorTask);

        this.tabbedPane = new JTabbedPane();
        this.tabbedPane.setTabPlacement(JTabbedPane.TOP);
        this.addTabWithIcon(this.tabbedPane, "Messages", "images/message-circle-lines.png", this.receivedMessagesPanel);
        this.addTabWithIcon(this.tabbedPane, "Cluster", "images/server-cluster.png",
                new JScrollPane(this.espInventoryPanel));
        this.addTabWithIcon(this.tabbedPane, "Scan Reports", "images/wifi-focus.png", new JScrollPane(this.scanReportsPanel));
        this.add(this.tabbedPane, BorderLayout.CENTER);

    }

    private void addTabWithIcon(JTabbedPane tabbedPane, String title, String iconPath, Component component) {
        // Charger l'icône
        URL iconUrl = getClass().getClassLoader().getResource(iconPath);
        if (iconUrl != null) {
            int desiredWidth = 20;
            int desiredHeight = desiredWidth;

            // Redimensionner l'image
            final ImageIcon imageIcon = new ImageIcon(iconUrl);
            final Image originalImage = imageIcon.getImage();
            final Image resizedImage = originalImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
            final ImageIcon resizedIcon = new ImageIcon(resizedImage);

            // Créer un JPanel pour combiner l'icône et le texte
            JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            tabPanel.setOpaque(false);

            // Créer un JLabel pour l'icône
            JLabel iconLabel = new JLabel(resizedIcon);

            // Créer un JLabel pour le texte
            JLabel textLabel = new JLabel(title);
            textLabel.setHorizontalAlignment(SwingConstants.LEFT);

            // Ajouter les composants au panneau
            tabPanel.add(iconLabel);
            tabPanel.add(textLabel);

            // Ajouter l'onglet avec le composant et le JPanel comme titre
            tabbedPane.addTab(null, component);
            int index = tabbedPane.getTabCount() - 1;
            tabbedPane.setTabComponentAt(index, tabPanel);

        } else {
            System.err.println("Icon not found : " + iconPath);
            // Ajouter l'onglet sans icône si l'icône n'est pas trouvée
            tabbedPane.addTab(title, component);
        }
    }

}
