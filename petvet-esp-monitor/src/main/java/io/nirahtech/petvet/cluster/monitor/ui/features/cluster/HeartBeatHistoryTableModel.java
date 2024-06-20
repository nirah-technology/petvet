package io.nirahtech.petvet.cluster.monitor.ui.features.cluster;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.table.AbstractTableModel;

import io.nirahtech.petvet.cluster.monitor.data.HeartBeat;

public class HeartBeatHistoryTableModel extends AbstractTableModel {

    private final String[] columnsNames = { "Date/Time", "Address IPv4", "EmitterMode", "Uptime", "Température (°C)", "Consomation", "Localisation" };
    private Collection<HeartBeat> heartBeats;

    public HeartBeatHistoryTableModel() {
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
            return heartBeat.ip();
            case 2:
            return heartBeat.mode();
            case 3:
            return heartBeat.uptime();
            case 4:
            return heartBeat.temperature();
            case 5:
            return heartBeat.consumption();
            case 6:
            return heartBeat.location();
            default:
                return null;
        }
    }

}
