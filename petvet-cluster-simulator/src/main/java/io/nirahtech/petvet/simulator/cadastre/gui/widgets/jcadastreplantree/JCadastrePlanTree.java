package io.nirahtech.petvet.simulator.cadastre.gui.widgets.jcadastreplantree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import io.nirahtech.petvet.simulator.cadastre.domain.Building;
import io.nirahtech.petvet.simulator.cadastre.domain.CadastralPlan;
import io.nirahtech.petvet.simulator.cadastre.domain.Land;
import io.nirahtech.petvet.simulator.cadastre.domain.Parcel;
import io.nirahtech.petvet.simulator.cadastre.domain.Section;
import io.nirahtech.petvet.simulator.cadastre.domain.Surface;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers.BuildingLayer;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers.LandLayer;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers.Layer;

public class JCadastrePlanTree extends JPanel {

    private final CadastralPlan cadastralPlan;

    private final Map<Section, DefaultMutableTreeNode> sectionsNodes = new HashMap<>();
    private final Map<Parcel, DefaultMutableTreeNode> parcelsNodes = new HashMap<>();
    private final Map<Land, DefaultMutableTreeNode> landsNodes = new HashMap<>();
    private final Map<Building, DefaultMutableTreeNode> buildingsNodes = new HashMap<>();

    private final DefaultMutableTreeNode root;
    private final DefaultTreeModel model;
    private final JTree tree;

    private final Map<Surface, Layer> layersBySurface = new HashMap<>();

    private Consumer<Layer> onSelectedLayerChanged = null;

    public JCadastrePlanTree(final CadastralPlan cadastralPlan) {
        super();
        this.setLayout(new BorderLayout());
        this.cadastralPlan = cadastralPlan;
        this.setPreferredSize(new Dimension(300, this.getPreferredSize().height));

        this.root = new DefaultMutableTreeNode("Cadastral Plan");
        this.model = new DefaultTreeModel(root);
        this.tree = new JTree(this.model);

        this.setupEventListeners();

        this.tree.setCellRenderer(new JCadastrePlanTreeCellRenderer());

        this.add(new JScrollPane(this.tree), BorderLayout.CENTER);
    }

    private final void setupEventListeners() {
        this.tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // Récupérer le chemin du nœud sélectionné
                    final TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        // Sélectionner le nœud au clic droit
                        tree.setSelectionPath(path);

                        final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path
                                .getLastPathComponent();
                        JPopupMenu popupMenu = null;

                        if (selectedNode == root) {
                            popupMenu = createPopupMenuForCadastreActions();
                        } else if (sectionsNodes.containsValue(selectedNode)) {
                            popupMenu = createPopupMenuForSectionActions(true);
                        } else if (parcelsNodes.containsValue(selectedNode)) {
                            popupMenu = createPopupMenuForParcelsActions();
                        } else if (buildingsNodes.containsValue(selectedNode)) {
                            popupMenu = createPopupMenuForBuildingActions();
                        }
                        if (Objects.nonNull(popupMenu)) {
                            popupMenu.show(tree, e.getX(), e.getY());
                        }
                    }
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    final TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                        retriveLayerByTreeNode(selectedNode).ifPresentOrElse(layer -> {
                            if (Objects.nonNull(onSelectedLayerChanged)) {
                                onSelectedLayerChanged.accept(layer);
                            }
                        }, () -> {
                            if (Objects.nonNull(onSelectedLayerChanged)) {
                                onSelectedLayerChanged.accept(null);
                            }
                        });
                        
                    }
                }
            }
        });
    }

    private JPopupMenu createPopupMenuForCadastreActions() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem createSectionMenuItem = new JMenuItem("Create new Section");
        createSectionMenuItem.addActionListener(e -> {
            // Action à effectuer lorsque l'élément du menu est sélectionné
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                final Land land = new Land();
                final Parcel parcel = new Parcel(0, land);
                final Section section = new Section(UUID.randomUUID().toString().split("-")[0], parcel);
                this.cadastralPlan.addSection(section);
                this.reloadTree();
            }
        });
        popupMenu.add(createSectionMenuItem);
        return popupMenu;
    }

    private JPopupMenu createPopupMenuForBuildingActions() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteBuildingMenuItem = new JMenuItem("Delete Building");
        deleteBuildingMenuItem.addActionListener(e -> {
            // Action à effectuer lorsque l'élément du menu est sélectionné
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                this.buildingsNodes.entrySet().stream().filter(set -> set.getValue().equals(selectedNode))
                            .findFirst().ifPresent(pair -> {
                                final Building buildingToDestroy = pair.getKey();
                                cadastralPlan.getSections().forEach(section -> {
                                    section.getParcels().forEach(parcel -> {
                                        parcel.land().getBuildings().forEach(building -> {
                                            if (building == buildingToDestroy) {
                                                parcel.land().removeBuilding(buildingToDestroy);
                                            }
                                        });
                                    });
                                });
                            });
                this.reloadTree();
            }
        });
        popupMenu.add(deleteBuildingMenuItem);
        return popupMenu;
    }

    private JPopupMenu createPopupMenuForSectionActions(final boolean allowDeletion) {
        JPopupMenu popupMenu = new JPopupMenu();

        if (allowDeletion) {
            JMenuItem deleteSectionMenuItem = new JMenuItem("Delete Section");
            deleteSectionMenuItem.addActionListener(e -> {
                // Action à effectuer lorsque l'élément du menu est sélectionné
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
                if (selectedNode != null) {
                    this.sectionsNodes.entrySet().stream().filter(set -> set.getValue().equals(selectedNode))
                            .findFirst().ifPresent(pair -> {
                                cadastralPlan.removeSection(pair.getKey());
                            });
                    this.reloadTree();
                }
            });
            popupMenu.add(deleteSectionMenuItem);
        }
        JMenuItem createParcelMenuItem = new JMenuItem("Create new Parcel");
        createParcelMenuItem.addActionListener(e -> {
            // Action à effectuer lorsque l'élément du menu est sélectionné
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                this.sectionsNodes.entrySet().stream().filter(set -> set.getValue().equals(selectedNode)).findFirst()
                        .ifPresent(pair -> {
                            final Section section = pair.getKey();
                            final Parcel parcel = new Parcel(section.getParcels().size(), new Land());
                            section.addParcel(parcel);
                        });
                this.reloadTree();
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
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                this.parcelsNodes.entrySet().stream().filter(set -> set.getValue().equals(selectedNode)).findFirst()
                        .ifPresent(pair -> {
                            final Parcel parcel = pair.getKey();
                            this.sectionsNodes.keySet().stream().filter(section -> section.has(parcel)).findFirst()
                                    .ifPresent(section -> {
                                        section.removeParcel(parcel);
                                    });
                        });
                this.reloadTree();
            }
        });
        JMenuItem addBuildingMenuItem = new JMenuItem("Add Building");
        addBuildingMenuItem.addActionListener(e -> {
            // Action à effectuer lorsque l'élément du menu est sélectionné
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
            if (selectedNode != null) {
                this.parcelsNodes.entrySet().stream().filter(set -> set.getValue().equals(selectedNode)).findFirst()
                        .ifPresent(pair -> {
                            final Parcel parcel = pair.getKey();
                            final Building building = new Building();
                            parcel.land().addBuilding(building);
                        });
                this.reloadTree();
            }
        });
        popupMenu.add(deleteParcelMenuItem);
        popupMenu.add(addBuildingMenuItem);
        return popupMenu;
    }

    public final void reloadTree() {
        this.root.removeAllChildren();
        this.sectionsNodes.clear();
        this.parcelsNodes.clear();
        this.landsNodes.clear();
        this.buildingsNodes.clear();

        // Ajouter toutes les sections et leurs parcelles
        for (Section section : this.cadastralPlan.getSections()) {
            DefaultMutableTreeNode sectionNode = new DefaultMutableTreeNode("Section " + section.identifier());
            this.sectionsNodes.put(section, sectionNode);
            root.add(sectionNode);

            for (Parcel parcel : section.getParcels()) {
                DefaultMutableTreeNode parcelNode = new DefaultMutableTreeNode("Parcel " + parcel.identifier());
                this.parcelsNodes.put(parcel, parcelNode);
                sectionNode.add(parcelNode);

                // DefaultMutableTreeNode landNode = new DefaultMutableTreeNode("Land");
                final Layer layer = this.layersBySurface.computeIfAbsent(parcel.land(), p -> new LandLayer(1));
                // parcelNode.add(landNode);
                JCadastreLayerPanel cadastreLayerPanel = new JCadastreLayerPanel(parcel.land(), layer);
                cadastreLayerPanel.addOnClickEventListener(clickedLayer -> {
                    if (Objects.nonNull(this.onSelectedLayerChanged)) {
                        this.onSelectedLayerChanged.accept(clickedLayer);
                    }
                });
                JCadastreLayerTreeNode node = new JCadastreLayerTreeNode(cadastreLayerPanel);
                this.landsNodes.put(parcel.land(), node);
                parcelNode.add(node);

                for (Building building : parcel.land().getBuildings()) {
                    // DefaultMutableTreeNode buildingNode = new DefaultMutableTreeNode("Building");
                    final Layer otherLayer = this.layersBySurface.computeIfAbsent(building, p -> new BuildingLayer(parcel.land().getBuildings().size()+2));
                    
                    JCadastreLayerTreeNode otherNode = new JCadastreLayerTreeNode(new JCadastreLayerPanel(building, otherLayer));
                    this.buildingsNodes.put(building, otherNode);
                    parcelNode.add(otherNode);
                    // parcelNode.add(buildingNode);
                }
            }
        }
        this.model.reload();
        this.revalidate();
        this.repaint();
        this.expandAllNodes();
    }

    private final Optional<Layer> retriveLayerByTreeNode(DefaultMutableTreeNode treeNode) {
        final AtomicReference<Layer> layerFound = new AtomicReference<>(null);

        landsNodes.entrySet()
                .stream()
                .filter(treeNodeInCache -> treeNodeInCache.getValue() == treeNode)
                .findFirst()
                .ifPresentOrElse(treeNodeFound -> {
                    if (this.layersBySurface.containsKey(treeNodeFound.getKey())) {
                        layerFound.set(this.layersBySurface.get(treeNodeFound.getKey()));
                    }
        }, () -> {
            buildingsNodes.entrySet()
                    .stream()
                    .filter(treeNodeInCache -> treeNodeInCache.getValue() == treeNode)
                    .findFirst()
                    .ifPresent(treeNodeFound -> {
                        if (this.layersBySurface.containsKey(treeNodeFound.getKey())) {
                            layerFound.set(this.layersBySurface.get(treeNodeFound.getKey()));
                        }
                    });
        });

        return Optional.ofNullable(layerFound.get());
    }

    private final void expandAllNodes() {
        for (int i = 0; i < this.tree.getRowCount(); i++) {
            this.tree.expandRow(i);
        }
    }

    public final void addOnSelectedLayerChanged(final Consumer<Layer> onSelectedLayerChanged) {
        this.onSelectedLayerChanged = onSelectedLayerChanged;
    }

    // private final class JCadastrePlanTreeCellRenderer extends DefaultTreeCellRenderer {

    //     private Icon leafIcon;
    //     private Icon openIcon;
    //     private Icon closedIcon;

    //     private final Icon rootIcon;
    //     private final Icon sectionIcon;
    //     private final Icon parcelIcon;
    //     private final Icon landIcon;
    //     private final Icon buildingIcon;

    //     public JCadastrePlanTreeCellRenderer() {
    //         this.leafIcon = UIManager.getIcon("FileView.fileIcon");
    //         this.openIcon = UIManager.getIcon("FileView.directoryIcon");
    //         this.closedIcon = UIManager.getIcon("FileView.directoryIcon");

    //         this.rootIcon = loadIcon("icons/cadastre.png");
    //         this.sectionIcon = loadIcon("icons/section.png");
    //         this.parcelIcon = loadIcon("icons/parcel.png");
    //         this.landIcon = loadIcon("icons/land.png");
    //         this.buildingIcon = loadIcon("icons/building.png");

    //     }

    //     private Icon loadIcon(String path) {
    //         URL url = getClass().getClassLoader().getResource(path);
    //         if (url == null) {
    //             return null;
    //         }

    //         ImageIcon icon = new ImageIcon(url);
    //         Icon defaultIcon = UIManager.getIcon("FileView.fileIcon");

    //         if (defaultIcon != null) {
    //             int width = defaultIcon.getIconWidth();
    //             int height = defaultIcon.getIconHeight();
    //             Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    //             return new ImageIcon(scaledImage);
    //         } else {
    //             return icon;
    //         }

    //     }

    //     @Override
    //     public Component getTreeCellRendererComponent(JTree tree, Object value,
    //             boolean selected, boolean expanded,
    //             boolean leaf, int row, boolean hasFocus) {
    //         super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

    //         setForeground(Color.WHITE);

    //         DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
    //         Object nodeInfo = node.getUserObject();

    //         if (nodeInfo.toString().equals("Cadastral Plan")) {
    //             setIcon(rootIcon);
    //         } else if (nodeInfo.toString().startsWith("Section")) {
    //             setIcon(sectionIcon);
    //         } else if (nodeInfo.toString().startsWith("Parcel")) {
    //             setIcon(parcelIcon);
    //         } else if (nodeInfo.toString().equals("Land")) {
    //             setIcon(landIcon);
    //         } else if (nodeInfo.toString().equals("Building")) {
    //             setIcon(buildingIcon);
    //         } else {
    //             if (leaf) {
    //                 setIcon(leafIcon);
    //             } else if (expanded) {
    //                 setIcon(openIcon);
    //             } else {
    //                 setIcon(closedIcon);
    //             }
    //         }
    //         this.setForeground(Color.WHITE);

    //         return new JCadastrePlanItemPanel(this, node);
    //     }

    // }

}
