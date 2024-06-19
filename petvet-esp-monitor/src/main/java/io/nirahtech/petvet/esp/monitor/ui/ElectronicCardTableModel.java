package io.nirahtech.petvet.esp.monitor.ui;

import java.util.ArrayList;
import java.util.SortedSet;

import javax.swing.table.AbstractTableModel;

import io.nirahtech.petvet.esp.monitor.ElectronicCard;

public class ElectronicCardTableModel extends AbstractTableModel {

    private final String[] columnsNames = { "ID", "MAC", "IP", "Mode", "Uptime", "Température (°C)", "Consomation", "Localisation", "Date/Time Dernier Message" };
    private final SortedSet<ElectronicCard> electronicCards;

    public ElectronicCardTableModel(final SortedSet<ElectronicCard> electronicCards) {
        this.electronicCards = electronicCards;
    }

    @Override
    public int getRowCount() {
        return this.electronicCards.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnsNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return this.columnsNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ElectronicCard electronicCard = new ArrayList<>(this.electronicCards).get(rowIndex);
        switch (columnIndex) {
            case 0:
            return electronicCard.getId();
            case 1:
            return electronicCard.getMac();
            case 2:
            return electronicCard.getIp();
            case 3:
            return electronicCard.getMode();
            case 4:
            return (electronicCard.getUptime().isPresent()) ? electronicCard.getUptime().get() : "NC";
            case 5:
            return (electronicCard.getTemperatureInCelcus().isPresent()) ? electronicCard.getTemperatureInCelcus().get() : "NC";
            case 6:
            return (electronicCard.getConsumptionInVolt().isPresent()) ? electronicCard.getConsumptionInVolt().get() : "NC";
            case 7:
            return (electronicCard.getLocation().isPresent()) ? electronicCard.getLocation().get() : "NC";
            case 8:
            return "NC";
            default:
                return null;
        }
    }

}
