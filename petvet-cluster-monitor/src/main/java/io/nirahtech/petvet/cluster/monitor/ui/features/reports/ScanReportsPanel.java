package io.nirahtech.petvet.cluster.monitor.ui.features.reports;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.SortedSet;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import io.nirahtech.petvet.cluster.monitor.MonitorTask;
import io.nirahtech.petvet.cluster.monitor.data.ElectronicalCard;
import io.nirahtech.petvet.cluster.monitor.data.ScanReport;
import io.nirahtech.petvet.messaging.messages.ScanReportMessage;

public final class ScanReportsPanel extends JPanel {
    


    // [list des scans request] | (resulmé pour la session de scan) | [list des MC scanners] | (details du MC scanner) | {Rapport de scan du MC scanner}
    private final ScansReportsPointOfViewPanel scansReportsPointOfViewPanel;

    // [liste des scanner] | [tableau liste des scans] | (résumé)
    private final ScannerPointOfViewPanel scannerPointOfViewPanel;

    public ScanReportsPanel(SortedSet<ElectronicalCard> esps, SortedSet<ScanReport> scanReports, MonitorTask monitorTask) {
        super(new BorderLayout());
        final JPanel container = new JPanel(new GridLayout(2, 1));

        this.scansReportsPointOfViewPanel = new ScansReportsPointOfViewPanel(esps, scanReports);
        this.scannerPointOfViewPanel = new ScannerPointOfViewPanel(esps, scanReports);

        container.add(this.scansReportsPointOfViewPanel);
        container.add(this.scannerPointOfViewPanel);

        this.add(container, BorderLayout.CENTER);

        monitorTask.addOnNewMessageHandler((message) -> {
            if (message instanceof ScanReportMessage) {
                ScanReportMessage realMessage = (ScanReportMessage) message;
                final ScanReport scanReport = ScanReport.map(realMessage);
                scanReports.add(scanReport);
                this.scansReportsPointOfViewPanel.refresh();
                // SwingUtilities.invokeLater(() -> {this.receivedMessagesModel.fireTableDataChanged();});
            }

        });
    }
}
