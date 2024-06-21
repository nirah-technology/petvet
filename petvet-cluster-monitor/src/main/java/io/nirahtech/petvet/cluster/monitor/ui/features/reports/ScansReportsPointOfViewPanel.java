package io.nirahtech.petvet.cluster.monitor.ui.features.reports;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import io.nirahtech.petvet.cluster.monitor.data.ElectronicalCard;
import io.nirahtech.petvet.cluster.monitor.data.ScanReport;

public class ScansReportsPointOfViewPanel extends JPanel {

    private final DefaultListModel<UUID> scanRequestsIdentifierListModel;
    private final DefaultListModel<UUID> effectiveScannersListModel;
    private final SortedSet<ElectronicalCard> esps;
    private final SortedSet<ScanReport> scanReports;
    private final List<ScanReport> selectedScanReports = new ArrayList<>();
    private final List<UUID> effectivesScanners = new ArrayList<>();

    private UUID selectScanId = null;

    private final JLabel scanID = new JLabel();
    private final JLabel date = new JLabel();
    private final JLabel scanersCount = new JLabel();
    private final JLabel scanDuration = new JLabel();

    public ScansReportsPointOfViewPanel(SortedSet<ElectronicalCard> esps, SortedSet<ScanReport> scanReports) {
        super(new GridLayout(1, 5));
        this.esps = esps;
        this.scanReports = scanReports;
        this.scanRequestsIdentifierListModel = new DefaultListModel<>(); 
        this.effectiveScannersListModel = new DefaultListModel<>(); 
        final JList<UUID> scanRequestsIdentifierList = new JList<>(this.scanRequestsIdentifierListModel);
        scanRequestsIdentifierList.addListSelectionListener((event) -> {
            if (!event.getValueIsAdjusting()) {
                final int selectedIndex = scanRequestsIdentifierList.getSelectedIndex();
                if (selectedIndex != -1) {
                    this.selectScanId = scanRequestsIdentifierList.getModel().getElementAt(selectedIndex);
                    this.selectedScanReports.clear();
                    this.selectedScanReports.addAll(
                        this.scanReports
                                .stream()
                                .filter(report -> report.scanId().equals(this.selectScanId))
                                .toList()
                    );
                    
                    this.scanID.setText(this.selectScanId.toString());
                    this.scanersCount.setText(String.valueOf(this.selectedScanReports.size()));

                    this.effectiveScannersListModel.clear();
                    this.selectedScanReports.forEach(scanReport -> {
                        this.effectiveScannersListModel.addElement(scanReport.espId());
                    });
                    this.repaint();
                }
            }

        });
        final JScrollPane scanRequestsIdentifierScrollPane = new JScrollPane(scanRequestsIdentifierList);
        scanRequestsIdentifierScrollPane.setMaximumSize(new Dimension(10, 200));

        final JPanel scanResumePanel = new JPanel(new GridLayout(4, 2));
        scanResumePanel.add(new JLabel("SCAN ID: "));
        scanResumePanel.add(scanID);
        scanResumePanel.add(new JLabel("SCAN DATE: "));
        scanResumePanel.add(date);
        scanResumePanel.add(new JLabel("TOTAL SCANNERS: "));
        scanResumePanel.add(scanersCount);
        scanResumePanel.add(new JLabel("SCAN DURATION: "));
        scanResumePanel.add(scanDuration);
        final JList<UUID> activeScannersForRequest = new JList<>(this.effectiveScannersListModel);
        final JPanel selectedscanerResumePanel = new JPanel();
        final JPanel reportDetailPanel = new JPanel();
        this.addPanel(new JLabel("Scan Requests Orders"), scanRequestsIdentifierScrollPane);
        this.addPanel(new JLabel("Scan Request Overview"), scanResumePanel);
        this.addPanel(new JLabel("Effective Scanners for Scan Request"), activeScannersForRequest);
        this.addPanel(new JLabel("Scanner Resume"), selectedscanerResumePanel);
        this.addPanel(new JLabel("Scan Report Detail"), reportDetailPanel);
    }

    private final void addPanel(JLabel title, JComponent component) {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(title, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        this.add(panel);
    }

    public final void refresh() {
        scanReports.forEach(report -> {
            if (!this.scanRequestsIdentifierListModel.contains(report.scanId())) {
                this.scanRequestsIdentifierListModel.addElement(report.scanId());
            }
        });
        this.repaint();
    }
}
