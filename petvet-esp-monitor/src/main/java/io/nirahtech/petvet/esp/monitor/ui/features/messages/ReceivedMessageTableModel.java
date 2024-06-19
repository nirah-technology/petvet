package io.nirahtech.petvet.esp.monitor.ui.features.messages;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import io.nirahtech.petvet.messaging.messages.Message;

public class ReceivedMessageTableModel extends AbstractTableModel {

    private final String[] columnsNames = { "Emitter ID", "MAC",  "IP", "Sended At", "Mode", "Message Type", "Data" };
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
                return message.getEmitterID();
                case 1:
                    return message.getEmitterMAC();
            case 2:
                return message.getEmitterIP();
            case 3:
                return message.getSentedAt();
            case 4:
                return message.getEmitterMode();
            case 5:
                return message.getType();
            case 6:
                return message.toString();
            default:
                return null;
        }
    }

}
