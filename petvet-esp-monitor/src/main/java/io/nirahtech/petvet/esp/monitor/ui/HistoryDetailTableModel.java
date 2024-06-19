package io.nirahtech.petvet.esp.monitor.ui;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.table.AbstractTableModel;

import io.nirahtech.petvet.esp.monitor.HeartBeat;

public class HistoryDetailTableModel extends AbstractTableModel {

    private final String[] columnsNames = { "Date/Time", "Address IPv4", "EmitterMode", "Uptime", "Température (°C)", "Consomation", "Localisation" };
    private Collection<HeartBeat> heartBeats;

    public HistoryDetailTableModel() {
        this.heartBeats = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return this.heartBeats.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnsNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return this.columnsNames[column];
    }

    public void setHeartBeats(Collection<HeartBeat> heartBeats) {
        this.heartBeats = heartBeats;
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        HeartBeat heartBeat = new ArrayList<>(this.heartBeats).get(rowIndex);
        switch (columnIndex) {
            case 0:
            return heartBeat.dateTime();
            case 1:
            return "";
            case 2:
            return "";
            case 3:
            return "";
            case 4:
            return "";
            case 5:
            return "";
            case 6:
            return "";
            default:
                return null;
        }
    }

}
