package io.nirahtech.petvet.cluster.monitor.ui.features.cluster;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class TemperatureChart extends JPanel {

    private final TimeSeries series;
    
    public TemperatureChart() {
        super(new BorderLayout());
        this.series = new TimeSeries("Board Temperature");
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(this.series);
        final JFreeChart timeSeriesChart = ChartFactory.createTimeSeriesChart(
            "Board Temperature", 
            "Time",
            "Temperature (°C)", dataset, true, true, false);

        final ChartPanel chartPanel = new ChartPanel(timeSeriesChart);
        chartPanel.setBackground(this.getBackground());
        timeSeriesChart.setBackgroundPaint(getBackground());
        this.add(chartPanel);

        // Personnalisation du graphique
        XYPlot plot = (XYPlot) timeSeriesChart.getPlot();
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        dateAxis.setDateFormatOverride(new java.text.SimpleDateFormat("HH:mm:ss"));

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(renderer);

        // Création du panneau de graphique
        chartPanel.setPreferredSize(new Dimension(100, 100));


    }

    public final void setSeriesData(Map<LocalDateTime, Double> timedSeriesData) {
        this.series.clear();
        timedSeriesData.entrySet().forEach(serie -> {
            final Date date = Date.from(serie.getKey().atZone(ZoneId.systemDefault()).toInstant());
            this.series.add(new Millisecond(date), serie.getValue());
        });
        this.repaint();
    }

}
