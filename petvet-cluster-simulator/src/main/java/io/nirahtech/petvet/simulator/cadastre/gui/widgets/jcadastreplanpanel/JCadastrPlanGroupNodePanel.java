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
    private final JButton deleteButton;
    private final JButton lockButton;

    private final Collection<JCadastrPlanGroupNodePanel> nodes;

    private final JPanel panelForNodes;

    private final Surface surface;

    private boolean isSelected = false;


    JCadastrPlanGroupNodePanel(final String text, final Surface surface) {
        super();
        this.surface = surface;

        this.visibilityButton = WidgetUtils.createSmallButton("Visibility", "hidden.png");
        
        this.title = new JLabel(text);
        
        this.lockButton = WidgetUtils.createSmallButton("Lock", "unlock.png");
        this.deleteButton = WidgetUtils.createSmallButton("Delete", "delete.png");

        final JPanel header = new JPanel(new BorderLayout());
        final JPanel main = new JPanel(new BorderLayout());
        final JPanel footer = new JPanel(new GridLayout(1, 2));

        header.add(this.visibilityButton, BorderLayout.WEST);
        main.add(this.title, BorderLayout.CENTER);
        footer.add(this.lockButton);
        footer.add(this.deleteButton);

        this.setLayout(new BorderLayout());

        final JPanel container = new JPanel(new BorderLayout());

        container.add(header, BorderLayout.WEST);
        container.add(main, BorderLayout.CENTER);
        container.add(footer, BorderLayout.EAST);

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
    }
}
