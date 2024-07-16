package io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import io.nirahtech.petvet.simulator.cadastre.domain.Building;
import io.nirahtech.petvet.simulator.cadastre.domain.CadastralPlan;
import io.nirahtech.petvet.simulator.cadastre.domain.Land;
import io.nirahtech.petvet.simulator.cadastre.domain.Parcel;
import io.nirahtech.petvet.simulator.cadastre.domain.Section;
import io.nirahtech.petvet.simulator.cadastre.domain.Segment;

public class JLayersPanel extends JPanel {

    private final SortedSet<Layer> layers = new TreeSet<>(Comparator.comparingInt(Layer::getOrder));
    private final List<JLayerPanel> layersPanels = new ArrayList<>();

    private final AtomicReference<Layer> selectedLayer = new AtomicReference<>(null);
    private final CadastralPlan cadastrePlan;

    private final JButton createButton;

    final List<DefaultMutableTreeNode> sectionsNodes = new ArrayList<>();
    final List<DefaultMutableTreeNode> parcelsNodes = new ArrayList<>();

    private final JTree cadastreTree;

    private Consumer<Layer> onSelectedLayerEventListerner = null;
    private Consumer<Layer> onLockChangedOnLayerEventListener = null;
    private Consumer<Layer> onVisibilityChangedOnLayerEventListener = null;
    private Consumer<CadastralPlan> onCadastreCreatedEventLister = null;

    public JLayersPanel(final CadastralPlan cadastralPlan) {
        super(new BorderLayout());

        this.layers.add(new LandLayer(1));
        this.layers.add(new BuildingLayer(2));

        this.cadastrePlan = cadastralPlan;

        final DefaultMutableTreeNode cadastreTreeNode = new DefaultMutableTreeNode("Cadastre");
        final DefaultMutableTreeNode defaultSectionTreeNode = new DefaultMutableTreeNode("Default Section");
        final DefaultMutableTreeNode defaultParcelTreeNode = new DefaultMutableTreeNode("Default Parcel");

        cadastreTreeNode.add(defaultSectionTreeNode);
        defaultSectionTreeNode.add(defaultParcelTreeNode);
        this.cadastreTree = new JTree(cadastreTreeNode);

        sectionsNodes.add(defaultSectionTreeNode);
        parcelsNodes.add(defaultParcelTreeNode);

        this.cadastreTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // Récupérer le chemin du nœud sélectionné
                    TreePath path = cadastreTree.getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        // Sélectionner le nœud au clic droit
                        cadastreTree.setSelectionPath(path);

                        final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                        JPopupMenu popupMenu = null;

                        if (selectedNode == cadastreTreeNode) {
                            popupMenu = createPopupMenuForCadastreActions();
                        } else if ( sectionsNodes.contains(selectedNode)) { // selectedNode == defaultSectionTreeNode
                            if (selectedNode == defaultSectionTreeNode) {
                                popupMenu = createPopupMenuForSectionActions(false);
                            } else {
                                popupMenu = createPopupMenuForSectionActions(true);
                            }
                        } else if (parcelsNodes.contains(selectedNode)) {
                            if (selectedNode != defaultParcelTreeNode) {
                                popupMenu = createPopupMenuForParcelsActions();
                            }
                        }
                        if (Objects.nonNull(popupMenu)) {
                            popupMenu.show(cadastreTree, e.getX(), e.getY());
                        }
                    }
                }
            }
        });


        this.layers.forEach(layer -> {
            final JLayerPanel layerPanel = new JLayerPanel(layer);
            this.layersPanels.add(layerPanel);
        });

        final JPanel layersListPanel = new JPanel(new GridLayout(this.layersPanels.size(), 1));
        this.layersPanels.forEach(layerPanel -> {
            layersListPanel.add(layerPanel);
        });

        this.add(layersListPanel, BorderLayout.NORTH);

        this.createButton = new JButton("Create");

        this.createButton.addActionListener(event -> {
            if (Objects.nonNull(onCadastreCreatedEventLister)) {
                onCadastreCreatedEventLister.accept(cadastralPlan);
            }
        });

        this.add(this.cadastreTree, BorderLayout.CENTER);

        this.add(this.createButton, BorderLayout.SOUTH);
        this.propagateOnSelectedLayerEventListerner();
        layersListPanel.revalidate();
        layersListPanel.repaint();

    }

    

    private JPopupMenu createPopupMenuForCadastreActions() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem createSectionMenuItem = new JMenuItem("Create new Section");
        createSectionMenuItem.addActionListener(e -> {
            // Action à effectuer lorsque l'élément du menu est sélectionné
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.cadastreTree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                System.out.println("Node selected: " + selectedNode.getUserObject());
            }
        });
        popupMenu.add(createSectionMenuItem);
        return popupMenu;
    }

    private JPopupMenu createPopupMenuForSectionActions(final boolean allowDeletion) {
        JPopupMenu popupMenu = new JPopupMenu();

        if (allowDeletion) {
            JMenuItem deleteSectionMenuItem = new JMenuItem("Delete Section");
            deleteSectionMenuItem.addActionListener(e -> {
                // Action à effectuer lorsque l'élément du menu est sélectionné
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.cadastreTree.getLastSelectedPathComponent();
                if (selectedNode != null) {
                    System.out.println("Node selected: " + selectedNode.getUserObject());
                }
            });
            popupMenu.add(deleteSectionMenuItem);
        }
        JMenuItem createParcelMenuItem = new JMenuItem("Create new Parcel");
        createParcelMenuItem.addActionListener(e -> {
            // Action à effectuer lorsque l'élément du menu est sélectionné
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.cadastreTree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                System.out.println("Node selected: " + selectedNode.getUserObject());
            }
        });
        popupMenu.add(createParcelMenuItem);
        return popupMenu;
    }

    private JPopupMenu createPopupMenuForParcelsActions() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteParcelMenuItem = new JMenuItem("Delete Parcel");
        deleteParcelMenuItem.addActionListener(e -> {
            // Action à effectuer lorsque l'élément du menu est sélectionné
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.cadastreTree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                System.out.println("Node selected: " + selectedNode.getUserObject());
            }
        });
        popupMenu.add(deleteParcelMenuItem);
        return popupMenu;
    }


    private List<Segment> createSegments(List<Point> points) {
        List<Segment> segments = new ArrayList<>();
        int size = points.size();

        for (int index = 0; index < size; index++) {
            Point from = points.get(index);
            Point to = points.get((index + 1) % size);
            segments.add(new Segment(from, to));
        }

        return segments;
    }

    private final void propagateOnSelectedLayerEventListerner() {
        this.layersPanels.forEach(layerPanel -> {
            layerPanel.setOnSelectedLayerEventListerner(this.onSelectedLayerEventListerner);
            layerPanel.setOnLockChangedOnLayerEventListener(this.onLockChangedOnLayerEventListener);
            layerPanel.setOnVisibilityChangedOnLayerEventListener(this.onVisibilityChangedOnLayerEventListener);
        });
    }

    public void setOnSelectedLayerEventListerner(Consumer<Layer> onSelectedLayerEventListerner) {
        this.onSelectedLayerEventListerner = onSelectedLayerEventListerner;
        this.propagateOnSelectedLayerEventListerner();
    }

    public void setOnLockChangeOnLayer(Consumer<Layer> onLockChangedOnLayerEventListener) {
        this.onLockChangedOnLayerEventListener = onLockChangedOnLayerEventListener;
        this.propagateOnSelectedLayerEventListerner();
    }

    public void setOnVisibilityChangeOnLayer(Consumer<Layer> onVisibilityChangedOnLayerEventListener) {
        this.onVisibilityChangedOnLayerEventListener = onVisibilityChangedOnLayerEventListener;
        this.propagateOnSelectedLayerEventListerner();
    }

    public void setSelectedLayer(Layer layer) {
        this.selectedLayer.set(layer);
        this.layersPanels.forEach(layerPanel -> {
            layerPanel.setSelectedLayer(layer);
        });
        this.revalidate();
        this.repaint();
    }

    public final void setOnCadastreCreatedEventLister(Consumer<CadastralPlan> onCadastreCreatedEventLister) {
        this.onCadastreCreatedEventLister = onCadastreCreatedEventLister;
    }

}
