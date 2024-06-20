package io.nirahtech.petvet.cluster.monitor.ui.features.cluster;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;

import io.nirahtech.petvet.cluster.monitor.data.ElectronicalCard;
import io.nirahtech.petvet.cluster.monitor.data.HeartBeat;

public final class ElectronicalCardDetailPanel extends JPanel {
    private final JPanel identityPanel;
    private final JPanel chartsPanel;

    private final TemperatureChart temperatureChart;
    private final ConsumptionChart consumptionChart;

    public ElectronicalCardDetailPanel() {
        super(new BorderLayout());
        this.identityPanel = new JPanel(new GridLayout(5, 2));

        this.identityPanel.add(new JLabel("Board ID"));
        this.identityPanel.add(new JPanel());

        this.identityPanel.add(new JLabel("MAC Address"));
        this.identityPanel.add(new JPanel());

        this.identityPanel.add(new JLabel("IP Address"));
        this.identityPanel.add(new JPanel());

        this.identityPanel.add(new JLabel("Uptime"));
        this.identityPanel.add(new JPanel());

        this.identityPanel.add(new JLabel("Location"));
        this.identityPanel.add(new JPanel());
        this.add(this.identityPanel, BorderLayout.WEST);

        this.temperatureChart = new TemperatureChart();
        this.consumptionChart = new ConsumptionChart();
        this.chartsPanel = new JPanel(new GridLayout(1, 2));
        this.chartsPanel.add(this.temperatureChart);
        this.chartsPanel.add(this.consumptionChart);

        this.add(this.chartsPanel, BorderLayout.CENTER);
    }

    public final void display(ElectronicalCard electronicalCard, Collection<HeartBeat> heartBeats) {
        final Map<LocalDateTime, Double> temperaturesSeries = new HashMap<>();
        final Map<LocalDateTime, Double> comsumptionSeries = new HashMap<>();
        synchronized(heartBeats) {
            heartBeats.forEach(heartBeat -> {
                temperaturesSeries.put(heartBeat.dateTime(), (double)heartBeat.temperature());
                comsumptionSeries.put(heartBeat.dateTime(), (double)heartBeat.consumption());
            });
        }
        this.temperatureChart.setSeriesData(temperaturesSeries);
        this.consumptionChart.setSeriesData(comsumptionSeries);
    }
}
