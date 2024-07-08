package io.nirahtech.petvet.cluster.monitor.ui.features.reports;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

import javax.swing.JPanel;

import io.nirahtech.petvet.cluster.monitor.MonitorTask;
import io.nirahtech.petvet.cluster.monitor.data.ElectronicalCard;
import io.nirahtech.petvet.cluster.monitor.data.ScanNow;
import io.nirahtech.petvet.cluster.monitor.data.ScanReport;
import io.nirahtech.petvet.messaging.messages.ScanNowMessage;
import io.nirahtech.petvet.messaging.messages.ScanReportMessage;

public final class ScanReportsPanel extends JPanel {
    


    // [list des scans request] | (resulmé pour la session de scan) | [list des MC scanners] | (details du MC scanner) | {Rapport de scan du MC scanner}
    private final ScansReportsPointOfViewPanel scansReportsPointOfViewPanel;

    // [liste des scanner] | [tableau liste des scans] | (résumé)
    private final ScannerPointOfViewPanel scannerPointOfViewPanel;

    public ScanReportsPanel(SortedSet<ElectronicalCard> esps, Map<ScanNow, SortedSet<ScanReport>> scanReports, MonitorTask monitorTask) {
        super(new BorderLayout());
        final JPanel container = new JPanel(new GridLayout(2, 1));

        this.scansReportsPointOfViewPanel = new ScansReportsPointOfViewPanel(esps, scanReports);
        this.scannerPointOfViewPanel = new ScannerPointOfViewPanel(esps, scanReports);

        container.add(this.scansReportsPointOfViewPanel);
        container.add(this.scannerPointOfViewPanel);

        this.add(container, BorderLayout.CENTER);

        monitorTask.addOnNewMessageHandler((message) -> {
            if (message instanceof ScanNowMessage) { 
                ScanNowMessage realMessage = (ScanNowMessage) message;
                final ScanNow scanNow = ScanNow.map(realMessage);
                scanReports.put(scanNow, new TreeSet<>());
            } else if (message instanceof ScanReportMessage) {
                ScanReportMessage realMessage = (ScanReportMessage) message;
                final ScanReport scanReport = ScanReport.map(realMessage);
                UUID scanID = realMessage.getScanId();
                scanReports.keySet().stream().filter(scan -> scan.scanId().equals(scanID)).findFirst().ifPresent(scan -> {
                    scanReports.get(scan).add(scanReport);
                    this.scansReportsPointOfViewPanel.refresh();
                });
                // SwingUtilities.invokeLater(() -> {this.receivedMessagesModel.fireTableDataChanged();});
            }

        });
    }
}
