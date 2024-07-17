package io.nirahtech.petvet.simulator.cadastre.gui.widgets.jcadastreplanpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import io.nirahtech.petvet.simulator.cadastre.domain.CadastralPlan;

public class JCadastrePlanPanel extends JPanel {

    private final CadastralPlan cadastralPlan;
    private JCadastrPlanGroupNodePanel root;

    private BiConsumer<JCadastrPlanGroupNodePanel, MouseEvent> onLeftClickEventListener = null;
    private BiConsumer<JCadastrPlanGroupNodePanel, MouseEvent> onRightClickEventListener = null;
    private Consumer<JCadastrPlanGroupNodePanel> onSelectionChangedEventListener = null;

    private AtomicReference<JCadastrPlanGroupNodePanel> selectedNode = new AtomicReference<>(null);

    public JCadastrePlanPanel(final CadastralPlan cadastralPlan) {
        super();
        this.cadastralPlan = cadastralPlan;
        this.setLayout(new BorderLayout());
        this.mapCadastralPlanAsPanels();
        this.attachEventListenerOnNode();
    }

    private final void attachEventListenerOnNode() {
        this.propagateOnClickEventListener(root);
    }

    private final void mapCadastralPlanAsPanels() {
        if (Objects.nonNull(this.cadastralPlan)) {
            this.removeAll();
            this.root = new JCadastrPlanGroupNodePanel("Cadastral Plan", this.cadastralPlan);
            this.cadastralPlan.getSections().forEach(section -> {
                final JCadastrPlanGroupNodePanel sectionNode = new JCadastrPlanGroupNodePanel("Section " + section.identifier(), section);
                section.getParcels().forEach(parcel -> {
                    final JCadastrPlanGroupNodePanel parcelNode = new JCadastrPlanGroupNodePanel("Parcel " + parcel.identifier(), parcel);
                    final JCadastrPlanGroupNodePanel landLeaf = new JCadastrPlanGroupNodePanel("Land", parcel.land());
                    parcelNode.addNode(landLeaf);
                    parcel.land().getBuildings().forEach(building -> {
                        final JCadastrPlanGroupNodePanel buildingLeaf = new JCadastrPlanGroupNodePanel("Building", building);
                        parcelNode.addNode(buildingLeaf);
                    });
                    sectionNode.addNode(parcelNode);
                });
                root.addNode(sectionNode);
            });
            this.add(root, BorderLayout.NORTH);
            this.revalidate();
            this.repaint();
        } else {
            this.root = null;
        }
    }

    private final void unselectNodeAndChildren(JCadastrPlanGroupNodePanel node) {
        node.setSelected(false);
        node.getNodes().forEach(childNode -> {
            unselectNodeAndChildren(childNode);
        });
    }

    private final void unselectAllNodes() {
        if (Objects.nonNull(this.root)) {
            this.unselectNodeAndChildren(this.root);
        }
    }

    private final void propagateOnClickEventListener(JCadastrPlanGroupNodePanel node) {
        node.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    selectedNode.set(node);
                    unselectAllNodes();
                    node.setSelected(true);
                    if (Objects.nonNull(onLeftClickEventListener)) {
                        onLeftClickEventListener.accept(node, event);
                    }
                    if (Objects.nonNull(onSelectionChangedEventListener)) {
                        onSelectionChangedEventListener.accept(node);
                    }
                } else if (SwingUtilities.isRightMouseButton(event)) {
                    if (Objects.nonNull(onRightClickEventListener)) {
                        onRightClickEventListener.accept(node, event);
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }
            
        });

        node.getNodes().forEach(nodeChild -> {
            this.propagateOnClickEventListener(nodeChild);
        });
    }

    public void addOnRightClickEventListener(BiConsumer<JCadastrPlanGroupNodePanel, MouseEvent> onRightClickEventListener) {
        this.onRightClickEventListener = onRightClickEventListener;
    }

    public void addOnLeftClickEventListener(BiConsumer<JCadastrPlanGroupNodePanel, MouseEvent> onLeftClickEventListener) {
        this.onLeftClickEventListener = onLeftClickEventListener;
    }

    public void addOnSelectionChangedEventListener(Consumer<JCadastrPlanGroupNodePanel> onSelectionChangedEventListener) {
        this.onSelectionChangedEventListener = onSelectionChangedEventListener;
    }

    public Optional<JCadastrPlanGroupNodePanel> getSelection() {
        return Optional.ofNullable(this.selectedNode.get());
    }
    
}
