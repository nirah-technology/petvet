package io.nirahtech.petvet.simulator.cadastre.gui.widgets.jcadastreplanpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.nirahtech.petvet.simulator.cadastre.domain.Surface;
import io.nirahtech.petvet.simulator.cadastre.gui.WidgetUtils;

public class JCadastrPlanGroupNodePanel extends JPanel {

    private final JLabel title;
    private final JButton visibilityButton;
    private final JButton lockButton;

    private final Collection<JCadastrPlanGroupNodePanel> nodes;

    private final JPanel panelForNodes;

    private final JPanel headerPanel;
    private final JPanel mainPanel;
    private final JPanel footerPanel;

    private final Surface surface;

    private boolean isSelected = false;


    JCadastrPlanGroupNodePanel(final String text, final Surface surface) {
        super();
        this.surface = surface;

        this.visibilityButton = WidgetUtils.createSmallButton("Visibility", "hidden.png");
        
        this.title = new JLabel(text);
        
        this.lockButton = WidgetUtils.createSmallButton("Lock", "unlock.png");

        this.headerPanel = new JPanel(new BorderLayout());
        this.mainPanel = new JPanel(new BorderLayout());
        this.footerPanel = new JPanel(new GridLayout(1, 1));

        this.headerPanel.add(this.visibilityButton, BorderLayout.WEST);
        this.mainPanel.add(this.title, BorderLayout.CENTER);
        this.footerPanel.add(this.lockButton);

        this.setLayout(new BorderLayout());

        final JPanel container = new JPanel(new BorderLayout());

        container.add(this.headerPanel, BorderLayout.WEST);
        container.add(this.mainPanel, BorderLayout.CENTER);
        container.add(this.footerPanel, BorderLayout.EAST);

        this.add(container, BorderLayout.NORTH);
        this.panelForNodes = new JPanel(new BorderLayout());
        this.add(this.panelForNodes, BorderLayout.CENTER);
        this.nodes = new LinkedList<>();

    }

    public void addNode(JCadastrPlanGroupNodePanel sectionNode) {
        if (Objects.nonNull(sectionNode)) {
            if (!this.nodes.contains(sectionNode)) {
                this.nodes.add(sectionNode);
                final JPanel container = new JPanel(new GridLayout(this.nodes.size(), 1));
                this.nodes.forEach(node -> container.add(node));
                panelForNodes.removeAll();
                panelForNodes.add(container, BorderLayout.CENTER);

                this.revalidate();
                this.repaint();
            }
        }
    }

    public Collection<JCadastrPlanGroupNodePanel> getNodes() {
        return nodes;
    }

    public Surface getSurface() {
        return surface;
    }

    public boolean isSelected() {
        return isSelected;
    }
    
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if (this.isSelected) {
            this.mainPanel.setBackground(new Color(150,0,0));
        } else {
            this.mainPanel.setBackground(this.getBackground());
        }
    }

}
