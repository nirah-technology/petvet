package io.nirahtech.petvet.simulator.cadastre.gui.widgets.jcadastreplantree;

import javax.swing.tree.DefaultMutableTreeNode;

public class JCadastreLayerTreeNode extends DefaultMutableTreeNode {
    private JCadastreLayerPanel layerPanel;

    public JCadastreLayerTreeNode(JCadastreLayerPanel layerPanel) {
        this.layerPanel = layerPanel;
    }

    public JCadastreLayerPanel getLayerPanel() {
        return layerPanel;
    }
}
