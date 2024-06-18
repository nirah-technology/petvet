package io.nirahtech.petvet.esp.monitor.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import io.nirahtech.petvet.esp.monitor.messages.Message;

public class ReceivedMessagesPanel extends JPanel {
    

    private final List<Message> receivedMessages;

    private final JPanel filterPanel;
    private final JComboBox<String> espIdFilterComboBox;
    private final JComboBox<String> espIpFilterComboBox;
    private final JComboBox<String> espModeFilterComboBox;
    private final JComboBox<String> messageTypeFilterComboBox;

    private final TableModel receivedMessagesModel;
    private final JTable receviedMessagesTable;

    public ReceivedMessagesPanel() {
        super.setLayout(new BorderLayout());

        this.espIdFilterComboBox = new JComboBox<>();
        this.espIdFilterComboBox.setEditable(true);
        this.espIpFilterComboBox = new JComboBox<>();
        this.espIpFilterComboBox.setEditable(true);
        this.espModeFilterComboBox = new JComboBox<>();
        this.espModeFilterComboBox.setEditable(true);
        this.messageTypeFilterComboBox = new JComboBox<>();
        this.messageTypeFilterComboBox.setEditable(true);

        this.filterPanel = new JPanel(new GridLayout(1, 4));
        this.filterPanel.add(this.espIdFilterComboBox);
        this.filterPanel.add(this.espIpFilterComboBox);
        this.filterPanel.add(this.espModeFilterComboBox);
        this.filterPanel.add(this.messageTypeFilterComboBox);

        this.add(this.filterPanel, BorderLayout.NORTH);

        this.receivedMessages = new ArrayList<>();
        this.receivedMessagesModel = new ReceivedMessageTableModel(this.receivedMessages);
        this.receviedMessagesTable = new JTable(this.receivedMessagesModel);
        this.add(new JScrollPane(this.receviedMessagesTable), BorderLayout.CENTER);

    }
}
