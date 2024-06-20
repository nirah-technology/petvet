package io.nirahtech;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.formdev.flatlaf.FlatDarkLaf;

public class ElectricalConsumptionTimeSeriesChartExample extends JFrame {

    public ElectricalConsumptionTimeSeriesChartExample() {
        super("Graphique de Séries Temporelles de Consommation Électrique");

        // Création du dataset (ensemble de données)
        TimeSeries series = new TimeSeries("Consommation Électrique");
        series.add(new Second(new Date()), 10.0); // Exemple de données, à remplacer par vos propres valeurs
        series.add(new Second(new Date(System.currentTimeMillis() + 1000)), 12.0);
        series.add(new Second(new Date(System.currentTimeMillis() + 2000)), 9.0);
        series.add(new Second(new Date(System.currentTimeMillis() + 3000)), 11.5);

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        // Création du graphique en ligne avec axe temporel
        JFreeChart timeSeriesChart = ChartFactory.createTimeSeriesChart(
                "Consommation Électrique",
                "Temps",
                "Consommation (Watt)",
                dataset,
                true,
                true,
                false);

        // Personnalisation du graphique
        XYPlot plot = (XYPlot) timeSeriesChart.getPlot();
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        dateAxis.setDateFormatOverride(new java.text.SimpleDateFormat("HH:mm:ss"));

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(renderer);

        // Création du panneau de graphique
        ChartPanel chartPanel = new ChartPanel(timeSeriesChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));

        // Ajout du panneau de graphique à la fenêtre
        getContentPane().add(chartPanel, BorderLayout.CENTER);

        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatDarkLaf()); // NimbusLookAndFeel
        SwingUtilities.invokeLater(() -> {
            ElectricalConsumptionTimeSeriesChartExample example = new ElectricalConsumptionTimeSeriesChartExample();
            example.setVisible(true);
        });
    }
}
