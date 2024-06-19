package io.nirahtech.petvet.esp.monitor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import io.nirahtech.petvet.esp.monitor.data.Device;
import io.nirahtech.petvet.esp.monitor.data.ElectronicalCard;
import io.nirahtech.petvet.esp.monitor.data.HeartBeat;
import io.nirahtech.petvet.esp.monitor.data.ScanReport;
import io.nirahtech.petvet.esp.monitor.ui.PetvetGpsEspWindow;
import io.nirahtech.petvet.messaging.brokers.MessageBroker;
import io.nirahtech.petvet.messaging.brokers.UDPMessageBroker;
import io.nirahtech.petvet.messaging.messages.Message;
import io.nirahtech.petvet.messaging.util.MacAddress;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, UnknownHostException {

        final ExecutorService executorService = Executors.newSingleThreadExecutor();

        final Map<MacAddress, Set<HeartBeat>> heartBeats = new HashMap<>();
        final List<Message> receivedMessages = new ArrayList<>();
        final SortedSet<ElectronicalCard> esps = new TreeSet<>();
        final SortedSet<ScanReport> scanReports = new TreeSet<>();
        final SortedSet<Device> detectedDevices = new TreeSet<>();

        final MessageBroker messageBroker = UDPMessageBroker.newInstance();
        final InetAddress multicastGroup = InetAddress.getByName("224.0.1.128");
        final int multicastPort = 44666;

        final MonitorTask monitorTask = new MonitorTask(
                messageBroker,
                receivedMessages,
                esps,
                heartBeats,
                scanReports,
                detectedDevices);

        UIManager.setLookAndFeel(new NimbusLookAndFeel());
        final PetvetGpsEspWindow window = new PetvetGpsEspWindow(
                messageBroker,
                multicastGroup,
                multicastPort,
                receivedMessages,
                esps,
                scanReports,
                detectedDevices,
                heartBeats,
                monitorTask);

        window.setVisible(true);
        executorService.submit(monitorTask);
    }
}
