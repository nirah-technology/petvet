package io.nirahtech.petvet.installer.ui.widgets.jtemplatetable;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class JTemplateTable extends JTable {

    private final JTemplateTableModel tableModel;
    private Consumer<Map.Entry<String, String>> onTokenValueChanged = null;

    public JTemplateTable() {
        super();
        this.tableModel = new JTemplateTableModel();
        this.setModel(this.tableModel);

        this.getColumnModel().getColumn(1).setCellEditor(new TextFieldCellEditor());
        this.getColumnModel().getColumn(1).setCellRenderer(new TextFieldCellRenderer());

        this.tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent event) {
                if (event.getType() == TableModelEvent.UPDATE) {
                    final int row = event.getFirstRow();
                    final Map.Entry<String, String> tokenChanged = tableModel.getTokenAt(row);
                    if (Objects.nonNull(onTokenValueChanged)) {
                        onTokenValueChanged.accept(tokenChanged);
                    }
                }
            }
        });
    }

    public final void setTokens(Map<String, String> tokens) {
        this.tableModel.setTokens(tokens);
    }

    public final void addTokenName(final String tokenName) {
        this.tableModel.tokens.put(tokenName, "");
        this.tableModel.fireTableDataChanged();
    }

    public final Map<String, String> getTokens() {
        final Map<String, String> tokens = new HashMap<>();
        for (Entry<String, String> token : this.tableModel.tokens.entrySet()) {
            tokens.put(token.getKey(), token.getValue());
        }
        return tokens;
    }

    private final class JTemplateTableModel extends DefaultTableModel {

        private final Map<String, String> tokens = new HashMap<>();

        public JTemplateTableModel() {
            this.addColumn("Token Parameter");
            this.addColumn("Parameter Value");
        }

        public final void setTokens(Map<String, String> tokens) {
            this.tokens.clear();
            this.tokens.putAll(tokens);
        }

        public Map.Entry<String, String> getTokenAt(final int row) {
            return new ArrayList<>(this.tokens.entrySet()).get(row);
        }

        @Override
        public int getRowCount() {
            return Objects.isNull(this.tokens) ? 0 : tokens.size();
        }

        @Override
        public Object getValueAt(int row, int column) {
            final Entry<String, String> token = new ArrayList<>(this.tokens.entrySet()).get(row);
            switch (column) {
                case 0:
                    return token.getKey();
                case 1:
                    return token.getValue();
                default:
                    return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            if (column == 1) {
                final Entry<String, String> token = new ArrayList<>(this.tokens.entrySet()).get(row);
                tokens.put(token.getKey(), (String) aValue);
                fireTableCellUpdated(row, column);
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 1;
        }

        public int findRowByToken(String token) {
            int row = 0;
            for (String key : this.tokens.keySet()) {
                if (key.equals(token)) {
                    return row;
                }
                row++;
            }
            return -1;
        }
    }

    private static class TextFieldCellEditor extends DefaultCellEditor {
        public TextFieldCellEditor() {
            super(new JTextField());
        }
    }

    private static class TextFieldCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            JTextField textField = new JTextField((String) value);
            if (isSelected) {
                textField.setBackground(table.getSelectionBackground());
            } else {
                textField.setBackground(table.getBackground());
            }
            return textField;
        }
    }

    public void onTokenValueChanged(final Consumer<Map.Entry<String, String>> onTokenValueChanged) {
        this.onTokenValueChanged = onTokenValueChanged;
    }

    public int findRowByToken(String token) {
        return this.tableModel.findRowByToken(token);
    }
}
