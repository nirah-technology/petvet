package io.nirahtech.petvet.esp.monitor.ui;

import java.awt.BorderLayout;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import io.nirahtech.petvet.esp.monitor.ElectronicCard;
import io.nirahtech.petvet.esp.monitor.HeartBeat;
import io.nirahtech.petvet.esp.monitor.MonitorTask;
import io.nirahtech.petvet.messaging.messages.HeartBeatMessage;
import io.nirahtech.petvet.messaging.messages.MessageType;
import io.nirahtech.petvet.messaging.messages.VoteMessage;
import io.nirahtech.petvet.messaging.util.MacAddress;

public final class ElectronicCardsInventoryPanel extends JPanel {

    private final SortedSet<ElectronicCard> esps;
    private final Map<MacAddress, Set<HeartBeat>> heartBeats;

    private final JSplitPane splitPane;

    private final JPanel inventoryPanel;
    private final ElectronicCardTableModel electronicalCardTableModel;
    private final JTable espsInventoryTable;

    private final HistoryDetailTableModel historyDetailTableModel;
    private final JTable historyDetailTable;
    private final JPanel detailsHistoryPanel;

    private final MonitorTask monitorTask;

    public ElectronicCardsInventoryPanel(final SortedSet<ElectronicCard> esps,
            final Map<MacAddress, Set<HeartBeat>> heartBeats, final MonitorTask monitorTask) {
        super.setLayout(new BorderLayout());
        this.esps = esps;
        this.heartBeats = heartBeats;
        this.monitorTask = monitorTask;

        this.inventoryPanel = new JPanel(new BorderLayout());
        this.electronicalCardTableModel = new ElectronicCardTableModel(this.esps);
        this.espsInventoryTable = new JTable(this.electronicalCardTableModel);
        this.inventoryPanel.add(new JScrollPane(this.espsInventoryTable), BorderLayout.CENTER);

        this.detailsHistoryPanel = new JPanel(new BorderLayout());
        this.historyDetailTableModel = new HistoryDetailTableModel();
        this.historyDetailTable = new JTable(this.historyDetailTableModel);
        this.detailsHistoryPanel.add(new JScrollPane(this.historyDetailTable), BorderLayout.CENTER);

        this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.inventoryPanel, this.detailsHistoryPanel);

        this.espsInventoryTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = espsInventoryTable.getSelectedRow();
                if (selectedRow >= 0) {
                    final ElectronicCard electronicalCard = new ArrayList<>(esps).get(selectedRow);
                    if (Objects.nonNull(electronicalCard)) {
                        if (this.heartBeats.containsKey(electronicalCard.getMac())) {
                            Collection<HeartBeat> electronicalCardHeartBeats = this.heartBeats
                                    .get(electronicalCard.getMac());
                            this.historyDetailTableModel.setHeartBeats(electronicalCardHeartBeats);
                            this.historyDetailTableModel.fireTableDataChanged();
                        }
                    }
                }
            }
        });

        this.monitorTask.addOnNewMessageHandler((message) -> {
            final ElectronicCard electronicCard = ElectronicCard.getOrCreate(
                    message.getEmitterID(), message.getEmitterMAC(), message.getEmitterIP(), message.getEmitterMode());
                    this.esps.add(electronicCard);
            final MacAddress mac = message.getEmitterMAC();
            if (message.getType().equals(MessageType.HEARTBEAT)) {
                final HeartBeatMessage realMessage = (HeartBeatMessage) message;
                final HeartBeat heartBeat = new HeartBeat(realMessage.getSentedAt(), realMessage.getEmitterIP(), realMessage.getEmitterMode(), Duration.ofMillis(realMessage.getUptime()), realMessage.getTemeratureInCelcus(), realMessage.getConsumptionInVolt(), realMessage.getLocation());
                Set<HeartBeat> heartBeatsOfTheCard;
                if (this.heartBeats.containsKey(mac)) {
                    heartBeatsOfTheCard = this.heartBeats.get(mac);
                } else {
                    heartBeatsOfTheCard = new HashSet<>();
                    this.heartBeats.put(mac, heartBeatsOfTheCard);
                }
                heartBeatsOfTheCard.add(heartBeat);
                electronicCard.setTemperatureInCelcus(realMessage.getTemeratureInCelcus());
                electronicCard.setConsumptionInVolt(realMessage.getConsumptionInVolt());
                electronicCard.setUptime(Duration.ofMillis(realMessage.getUptime()));
                electronicCard.setLocation(realMessage.getLocation());

            } else if (message.getType().equals(MessageType.VOTE)) {
                final VoteMessage realMessage = (VoteMessage) message;
                electronicCard.setUptime(Duration.ofMillis(realMessage.getUptime()));
            }
            SwingUtilities.invokeLater(() -> {this.electronicalCardTableModel.fireTableDataChanged();});
        });

        this.add(this.splitPane, BorderLayout.CENTER);
    }

}
