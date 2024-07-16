package io.nirahtech.petvet.simulator.cadastre.gui.widgets.jcadastreplantree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public class JCadastrePlanTreeCellRenderer implements TreeCellRenderer {
    private DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value instanceof JCadastreLayerTreeNode) {
            JCadastreLayerTreeNode node = (JCadastreLayerTreeNode) value;
            return node.getLayerPanel();
        } else {
            return defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        }
    }

}
