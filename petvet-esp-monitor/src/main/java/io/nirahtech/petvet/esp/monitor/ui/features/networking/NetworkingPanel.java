package io.nirahtech.petvet.esp.monitor.ui.features.networking;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import io.nirahtech.petvet.messaging.brokers.MessageBroker;

public class NetworkingPanel extends JPanel {
    private static final String CONNECT = "Connect";
    private static final String DISCONNECT = "Disconnect";

    private final MessageBroker messageBroker;

    private final AtomicReference<NetworkInterface> networkInterface = new AtomicReference<>();
    private InetAddress multicastGroupAddress;
    private int multicastGroupPort;

    private final JComboBox<NetworkInterface> networkInterfacesComboBox;
    private final DefaultComboBoxModel<NetworkInterface> networkInterfacesComboBoxModel;
    private final JTextField multicastGroupAddressTextField;
    private final JSpinner multicastGroupPortSpinner;

    private final JButton actionButton;

    public NetworkingPanel(final MessageBroker messageBroker, final InetAddress multicastGroupAddress,
            final int multicastGroupPort) {
        this.multicastGroupAddress = multicastGroupAddress;
        this.multicastGroupPort = multicastGroupPort;
        this.messageBroker = messageBroker;

        this.setLayout(new GridLayout(1, 4));

        this.networkInterfacesComboBoxModel = new DefaultComboBoxModel<>();
        this.networkInterfacesComboBox = new JComboBox<>(this.networkInterfacesComboBoxModel);
        this.networkInterfacesComboBox.setEditable(false);
        this.networkInterfacesComboBox.setRenderer(new NetworkInterfaceComboBoxRenderer());
        try {
            this.networkInterfacesComboBoxModel.addAll(NetworkInterface.networkInterfaces().toList());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.networkInterfacesComboBox.addActionListener((event) -> {
            NetworkInterface selectedNetworkInterface = (NetworkInterface) networkInterfacesComboBox.getSelectedItem();
            if (Objects.nonNull(selectedNetworkInterface)) {
                networkInterface.set(selectedNetworkInterface);
            }
        });
        this.add(this.networkInterfacesComboBox);

        this.multicastGroupAddressTextField = new JTextField(this.multicastGroupAddress.getHostAddress());
        this.add(this.multicastGroupAddressTextField);
        this.add(this.multicastGroupAddressTextField);
        SpinnerNumberModel portSpinnerModel = new SpinnerNumberModel(this.multicastGroupPort, // initial value
                1024, // min
                1024 * 65, // max
                1);
        this.multicastGroupPortSpinner = new JSpinner(portSpinnerModel);
        this.add(this.multicastGroupPortSpinner);

        this.actionButton = new JButton(CONNECT);
        this.actionButton.addActionListener((event) -> {
            this.toggleButton(event);
        });
        this.add(this.actionButton);

    }

    private final void toggleButton(final ActionEvent event) {
        if (this.messageBroker.isConnected()) {
            this.messageBroker.disconnect();
            this.actionButton.setText(CONNECT);

            this.networkInterfacesComboBox.setEnabled(!this.networkInterfacesComboBox.isEnabled());
            this.multicastGroupAddressTextField.setEnabled(!this.multicastGroupAddressTextField.isEnabled());
            this.multicastGroupPortSpinner.setEnabled(!this.multicastGroupPortSpinner.isEnabled());
        } else {
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
                                    this.actionButton.setText(DISCONNECT);

                                    this.networkInterfacesComboBox
                                            .setEnabled(!this.networkInterfacesComboBox.isEnabled());
                                    this.multicastGroupAddressTextField
                                            .setEnabled(!this.multicastGroupAddressTextField.isEnabled());
                                    this.multicastGroupPortSpinner
                                            .setEnabled(!this.multicastGroupPortSpinner.isEnabled());
                                            JOptionPane.showMessageDialog(this, "Connected to the message broker.", "Connection Success",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    JOptionPane.showMessageDialog(this, e.getMessage(), "Connection Error",
                                            JOptionPane.ERROR_MESSAGE);
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    public InetAddress getMulticastGroupAddress() {
        return this.multicastGroupAddress;
    }

    public int getMulticastGroupPort() {
        return this.multicastGroupPort;
    }

}
