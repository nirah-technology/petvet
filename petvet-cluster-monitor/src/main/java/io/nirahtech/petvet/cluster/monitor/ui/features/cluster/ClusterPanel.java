package io.nirahtech.petvet.cluster.monitor.ui.features.cluster;

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

import io.nirahtech.petvet.cluster.monitor.MonitorTask;
import io.nirahtech.petvet.cluster.monitor.data.ElectronicalCard;
import io.nirahtech.petvet.cluster.monitor.data.HeartBeat;
import io.nirahtech.petvet.messaging.messages.HeartBeatMessage;
import io.nirahtech.petvet.messaging.messages.MessageType;
import io.nirahtech.petvet.messaging.messages.VoteMessage;
import io.nirahtech.petvet.messaging.util.MacAddress;

public final class ClusterPanel extends JPanel {

    private final SortedSet<ElectronicalCard> esps;
    private final Map<MacAddress, Set<HeartBeat>> heartBeats;

    private final JSplitPane splitPane;

    private final JPanel inventoryPanel;
    
    private final ClusterTable clusterTable;

    private final ElectronicalCardDetailPanel electronicalCardDetailPanel;

    private final MonitorTask monitorTask;

    public ClusterPanel(final SortedSet<ElectronicalCard> esps,
            final Map<MacAddress, Set<HeartBeat>> heartBeats, final MonitorTask monitorTask) {
        super.setLayout(new BorderLayout());
        this.esps = esps;
        this.heartBeats = heartBeats;
        this.monitorTask = monitorTask;

        this.inventoryPanel = new JPanel(new BorderLayout());
        this.clusterTable = new ClusterTable(this.esps);
        this.electronicalCardDetailPanel = new ElectronicalCardDetailPanel();
        
        this.inventoryPanel.add(new JScrollPane(this.clusterTable), BorderLayout.CENTER);


        this.splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.inventoryPanel, this.electronicalCardDetailPanel);

        this.clusterTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = clusterTable.getSelectedRow();
                if (selectedRow > -1) {
                    final ElectronicalCard electronicalCard = new ArrayList<>(esps).get(selectedRow);
                    if (Objects.nonNull(electronicalCard)) {
                        if (this.heartBeats.containsKey(electronicalCard.getMac())) {
                            Collection<HeartBeat> electronicalCardHeartBeats = this.heartBeats
                                    .get(electronicalCard.getMac());
                                    this.electronicalCardDetailPanel.display(electronicalCard, electronicalCardHeartBeats);
                        }
                    }
                }
            }
        });

        this.monitorTask.addOnNewMessageHandler((message) -> {
            final ElectronicalCard electronicCard = ElectronicalCard.getOrCreate(
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
            SwingUtilities.invokeLater(() -> {
                final int index = this.clusterTable.getSelectedRow();
                Object[] rowData = new Object[this.clusterTable.getColumnCount()];
                for (int col = 0; col < this.clusterTable.getColumnCount(); col++) {
                    rowData[col] = this.clusterTable.getValueAt(index, col);
                }

                this.clusterTable.refresh();
                
                for (int row = 0; row < this.clusterTable.getRowCount(); row++) {
                    boolean matches = true;
                    for (int col = 0; col < this.clusterTable.getColumnCount(); col++) {
                        if (Objects.isNull(rowData[col])) {
                            matches = false;
                            break;
                        } else if (!rowData[col].equals(this.clusterTable.getValueAt(row, col))) {
                            matches = false;
                            break;
                        }
                    }
                    if (matches) {
                        this.clusterTable.addRowSelectionInterval(row, row);
                        break;
                    }
                }
            
            });
        });

        this.add(this.splitPane, BorderLayout.CENTER);
    }

}
