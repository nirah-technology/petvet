package io.nirahtech.petvet.cluster.monitor.ui.windows;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import io.nirahtech.petvet.cluster.monitor.MonitorTask;
import io.nirahtech.petvet.cluster.monitor.data.Device;
import io.nirahtech.petvet.cluster.monitor.data.ElectronicalCard;
import io.nirahtech.petvet.cluster.monitor.data.HeartBeat;
import io.nirahtech.petvet.cluster.monitor.data.ScanReport;
import io.nirahtech.petvet.cluster.monitor.ui.features.networking.NetworkInterfaceComboBoxRenderer;
import io.nirahtech.petvet.messaging.brokers.MessageBroker;
import io.nirahtech.petvet.messaging.messages.Message;
import io.nirahtech.petvet.messaging.util.MacAddress;

public class PetvetClusterConnectionWindow extends JFrame {

    private final MessageBroker messageBroker;

    private final AtomicReference<NetworkInterface> networkInterface = new AtomicReference<>();
    private InetAddress multicastGroupAddress;
    private int multicastGroupPort;

    private final JComboBox<NetworkInterface> networkInterfacesComboBox;
    private final DefaultComboBoxModel<NetworkInterface> networkInterfacesComboBoxModel;
    private final JTextField multicastGroupAddressTextField;
    private final JSpinner multicastGroupPortSpinner;

    private final JButton actionButton;

    public PetvetClusterConnectionWindow(final MessageBroker messageBroker, final InetAddress multicastGroup,
            final int multicastPort, final List<Message> receivedMessages, final SortedSet<ElectronicalCard> esps,
            final SortedSet<ScanReport> scanReports, final SortedSet<Device> detectedDevices,
            final Map<MacAddress, Set<HeartBeat>> heartBeats, final MonitorTask monitorTask) {
        super("NIRAH-TECHNOLOGY : PetVet Monitor - Access Panel");
        this.messageBroker = messageBroker;

        this.multicastGroupAddress = multicastGroup;
        this.multicastGroupPort = multicastPort;
        this.setResizable(false);

        this.setSize(500, 250);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(1, 2));

        final JPanel logoPanel = new JPanel(new BorderLayout());
        final JPanel networkPanel = new JPanel(new GridLayout(8, 1));

        final URL imagePath = this.getClass().getClassLoader().getResource("images/nirahtech-logo.png");
        // Définir la taille souhaitée
        int desiredWidth = 200;
        int desiredHeight = 200;

        // Redimensionner l'image
        final ImageIcon imageIcon = new ImageIcon(imagePath);
        final Image originalImage = imageIcon.getImage();
        final Image resizedImage = originalImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
        final ImageIcon resizedIcon = new ImageIcon(resizedImage);

        final JLabel logo = new JLabel(resizedIcon);
        logoPanel.add(logo, BorderLayout.CENTER);
        this.add(logoPanel);

        this.multicastGroupAddressTextField = new JTextField(this.multicastGroupAddress.getHostAddress());
        SpinnerNumberModel portSpinnerModel = new SpinnerNumberModel(this.multicastGroupPort, // initial value
                1024, // min
                1024 * 65, // max
                1);

        this.networkInterfacesComboBoxModel = new DefaultComboBoxModel<>();
        this.networkInterfacesComboBox = new JComboBox<>(this.networkInterfacesComboBoxModel);
        this.networkInterfacesComboBox.setEditable(false);
        this.networkInterfacesComboBox.setRenderer(new NetworkInterfaceComboBoxRenderer());
        System.out.println("Retrieve all connected network interfaces cards...");
        try {
            this.networkInterfacesComboBoxModel.addAll(NetworkInterface.networkInterfaces().toList());
        } catch (SocketException e) {
            System.out.println("Failed to load netwok interfaces cards:");
            e.printStackTrace();
        }
        this.networkInterfacesComboBox.addActionListener((event) -> {
            NetworkInterface selectedNetworkInterface = (NetworkInterface) networkInterfacesComboBox.getSelectedItem();
            if (Objects.nonNull(selectedNetworkInterface)) {
                System.out.println("Selected network interface card changed: " + selectedNetworkInterface.getName());
                networkInterface.set(selectedNetworkInterface);
            }
        });

        this.multicastGroupPortSpinner = new JSpinner(portSpinnerModel);
        networkPanel.add(new JLabel("Network Interface Cards"));
        networkPanel.add(this.networkInterfacesComboBox);
        networkPanel.add(new JLabel("Multicast Group IP Address"));
        networkPanel.add(this.multicastGroupAddressTextField);
        networkPanel.add(new JLabel("Multicast Group Port"));
        networkPanel.add(this.multicastGroupPortSpinner);
        networkPanel.add(new JLabel(""));
        this.actionButton = new JButton("Connect");
        networkPanel.add(this.actionButton);

        this.actionButton.addActionListener((event) -> {
            if (Objects.nonNull(this.networkInterface.get())) {
                if (!this.multicastGroupAddressTextField.getText().isEmpty()) {
                    InetAddress newMulticastGroupAddress = null;
                    try {
                        newMulticastGroupAddress = InetAddress.getByName(this.multicastGroupAddressTextField.getText());
                    } catch (UnknownHostException e) {
                        JOptionPane.showMessageDialog(this, e.getMessage(), "Connection Error",
                                JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                    if (Objects.nonNull(newMulticastGroupAddress)) {
                        int port = 0;
                        if (!this.multicastGroupPortSpinner.getValue().toString().isEmpty()) {
                            try {
                                port = Integer.parseInt(this.multicastGroupPortSpinner.getValue().toString());
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(this, e.getMessage(), "Connection Error",
                                        JOptionPane.ERROR_MESSAGE);
                                e.printStackTrace();
                            }
                            final int minPort = 1;
                            final int maxPort = (1024 * 65);
                            if (port < minPort || port > maxPort) {
                                JOptionPane.showMessageDialog(this,
                                        String.format("Port is out of range: [%s, %s]", minPort, maxPort),
                                        "Connection Error", JOptionPane.ERROR_MESSAGE);
                            } else {
                                try {
                                    this.multicastGroupAddress = newMulticastGroupAddress;
                                    this.multicastGroupPort = port;
                                    this.messageBroker.connect(this.networkInterface.get(), this.multicastGroupAddress,
                                            this.multicastGroupPort);
                                    

                                    this.networkInterfacesComboBox
                                            .setEnabled(!this.networkInterfacesComboBox.isEnabled());
                                    this.multicastGroupAddressTextField
                                            .setEnabled(!this.multicastGroupAddressTextField.isEnabled());
                                    this.multicastGroupPortSpinner
                                            .setEnabled(!this.multicastGroupPortSpinner.isEnabled());
                                    System.out.println(String.format("Connected to the multicast group: udp://%s:%s", this.messageBroker.getInetAddress(), this.messageBroker.getPort()));
                                            this.setVisible(false);
                                            JFrame monitorWindow = new PetvetClusterMonitorWindow(messageBroker, multicastGroup, multicastPort, receivedMessages, esps, scanReports, detectedDevices, heartBeats, monitorTask);
                                            monitorWindow.setVisible(true);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(this, e.getMessage(), "Access Denied",
                                            JOptionPane.ERROR_MESSAGE);
                                }

                            }
                        }
                    }
                }
            }
        });

        this.add(networkPanel);
    }
}
