package io.nirahtech.petvet.geopulsetracker;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.nirahtech.petvet.geopulsetracker.ui.PetvetClusterConnectionWindow;
import io.nirahtech.petvet.geopulsetracker.ui.ScanReportMessageSnifferTask;
import io.nirahtech.petvet.messaging.brokers.MessageBroker;
import io.nirahtech.petvet.messaging.brokers.UDPMessageBroker;
import io.nirahtech.petvet.messaging.messages.MessageType;
import io.nirahtech.petvet.messaging.messages.ScanReportMessage;

public class App {
    private static final String NETWORK_MULTICAST_GROUP_ADDRESS = "network.multicast.group.address";
    private static final String NETWORK_MULTICAST_GROUP_PORT = "network.multicast.group.port";

    public static void main(String[] args) throws UnknownHostException {
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        final MessageBroker messageBroker = UDPMessageBroker.newInstance();
        final PetvetClusterConnectionWindow window = new PetvetClusterConnectionWindow(messageBroker, InetAddress.getByName("224.0.1.128"), 44666);
        messageBroker.subscribe(MessageType.SCAN_REPORT, (message) -> {
            if (message instanceof ScanReportMessage) {
                final ScanReportMessage realMessage = (ScanReportMessage) message;
                System.out.println(realMessage);
            }
        });
        executorService.submit(new ScanReportMessageSnifferTask(messageBroker));
        window.setVisible(true);
    }
}