package io.nirahtech.petvet.cluster.monitor.ui.features.reports;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.SortedSet;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;

import io.nirahtech.petvet.cluster.monitor.data.ElectronicalCard;
import io.nirahtech.petvet.cluster.monitor.data.ScanReport;

public class ScannerPointOfViewPanel extends JPanel {
    public ScannerPointOfViewPanel(SortedSet<ElectronicalCard> esps, SortedSet<ScanReport> scanReports) {
        super(new GridLayout(1, 3));
        final JList<ElectronicalCard> allScanners = new JList<>();
        final JTable scanReportsByElectronicalCardTable = new JTable();
        final JPanel selectedScanResume = new JPanel();

        this.addPanel(new JLabel("List of Scanners"), allScanners);
        this.addPanel(new JLabel("List of Scan Reports"), scanReportsByElectronicalCardTable);
        this.addPanel(new JLabel("Scan Report Details"), selectedScanResume);
    }

    private final void addPanel(JLabel title, JComponent component) {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(title, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        this.add(panel);
    }

    public final void refresh() {
        
    }
}
