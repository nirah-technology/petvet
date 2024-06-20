package io.nirahtech.petvet.cluster.monitor.ui.features.messages;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import io.nirahtech.petvet.cluster.monitor.MonitorTask;
import io.nirahtech.petvet.messaging.messages.Message;

public class ReceivedMessagesPanel extends JPanel {
    

    private final List<Message> receivedMessages;


    private final ReceivedMessageTableModel receivedMessagesModel;
    private final JTable receviedMessagesTable;
    private final JButton clearMessagesButton;

    private final MonitorTask monitorTask;

    public ReceivedMessagesPanel(final List<Message> receivedMessages, MonitorTask monitorTask) {
        this.receivedMessages = receivedMessages;
        this.monitorTask = monitorTask;
        super.setLayout(new BorderLayout());


        this.receivedMessagesModel = new ReceivedMessageTableModel(this.receivedMessages);
        this.receviedMessagesTable = new JTable(this.receivedMessagesModel);
        this.receviedMessagesTable.setAutoCreateRowSorter(true);

        this.monitorTask.addOnNewMessageHandler((message) -> {
            SwingUtilities.invokeLater(() -> {this.receivedMessagesModel.fireTableDataChanged();});

        });

        this.clearMessagesButton = new JButton("Clear Messages");

        this.clearMessagesButton.addActionListener(event -> {
            this.receivedMessages.clear();
            this.receivedMessagesModel.fireTableDataChanged();
        });
        this.add(new JScrollPane(this.receviedMessagesTable), BorderLayout.CENTER);
        this.add(this.clearMessagesButton, BorderLayout.SOUTH);

    }
}
