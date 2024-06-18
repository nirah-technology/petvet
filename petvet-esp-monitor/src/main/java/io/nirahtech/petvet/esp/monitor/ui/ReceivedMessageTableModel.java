package io.nirahtech.petvet.esp.monitor.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import io.nirahtech.petvet.esp.monitor.messages.Message;

public class ReceivedMessageTableModel extends AbstractTableModel {

    private final String[] columnsNames = { "ESP Emitter ID", "ESP Emitter IP", "Sended At", "ESP Mode", "Message Type",
            "Data" };
    private final List<Message> receivedMessages;

    public ReceivedMessageTableModel(final List<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    @Override
    public int getRowCount() {
        return this.receivedMessages.size();
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
        Message message = this.receivedMessages.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return message.getId();
            case 1:
                return message.getEmitter();
            case 2:
                return message.sentAt();
            case 3:
                return message.isOrchestrator();
            case 4:
                return message.getType();
            case 5:
                return message.toString();
            default:
                return null;
        }
    }

}
