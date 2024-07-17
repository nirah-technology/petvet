package io.nirahtech.petvet.simulator.cadastre.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import io.nirahtech.petvet.simulator.cadastre.domain.Building;
import io.nirahtech.petvet.simulator.cadastre.domain.CadastralPlan;
import io.nirahtech.petvet.simulator.cadastre.domain.Land;
import io.nirahtech.petvet.simulator.cadastre.domain.Parcel;
import io.nirahtech.petvet.simulator.cadastre.domain.Section;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.jcadastreplanpanel.JCadastrePlanPanel;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers.JLayersPanel;

public class PetVetLandWindow extends JFrame {

    private final CadastralPlan cadastralPlan;
    private final JDrawerPanel drawerPanel;
    private final JLayersPanel layersPanel;

    public PetVetLandWindow() {
        super("PetVet : Land Simulator");
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());

        this.cadastralPlan = new CadastralPlan();
        
        this.drawerPanel = new JDrawerPanel(this.cadastralPlan);
        this.layersPanel = new JLayersPanel(this.cadastralPlan);

        this.layersPanel.setOnSelectedLayerEventListerner(layer -> {
            this.drawerPanel.setSelectedLayer(layer);
            this.layersPanel.setSelectedLayer(layer);

        });

        this.layersPanel.setOnLockChangeOnLayer(layer -> {
            this.drawerPanel.redraw();
        });

        this.layersPanel.setOnVisibilityChangeOnLayer(layer -> {
            this.drawerPanel.redraw();
        });


        this.add(this.drawerPanel, BorderLayout.CENTER);

        final JCadastrePlanPanel cadastrePlanTree =new JCadastrePlanPanel(cadastralPlan);
        cadastrePlanTree.addOnLeftClickEventListener((node, event) -> {
            System.out.println("Selected");
        });

        cadastrePlanTree.addOnRightClickEventListener((node, event) -> {
            JPopupMenu popupMenu = null;
            if (node.getSurface() instanceof CadastralPlan) {
                popupMenu = createPopupMenuForCadastreActions(node);
            } else if (node.getSurface() instanceof Section) {
                popupMenu = createPopupMenuForSectionActions(true);
            } else if (node.getSurface() instanceof Parcel) {
                popupMenu = createPopupMenuForParcelsActions();
            } else if (node.getSurface() instanceof Building) {
                popupMenu = createPopupMenuForBuildingActions();
            }
            if (Objects.nonNull(popupMenu)) {
                popupMenu.show(node, event.getX(), event.getY());
            }
        });
        // cadastrePlanTree.addOnSelectedLayerChanged(layer -> {
        //     this.drawerPanel.setSelectedLayer(layer);
        //     this.layersPanel.setSelectedLayer(layer);
        // });

        this.add(cadastrePlanTree, BorderLayout.EAST);

    }


    private JPopupMenu createPopupMenuForCadastreActions() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem createSectionMenuItem = new JMenuItem("Create new Section");
        createSectionMenuItem.addActionListener(e -> {
            // // Action à effectuer lorsque l'élément du menu est sélectionné
            // DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
            // if (selectedNode != null) {
            //     final Land land = new Land();
            //     final Parcel parcel = new Parcel(0, land);
            //     final Section section = new Section(UUID.randomUUID().toString().split("-")[0], parcel);
            //     this.cadastralPlan.addSection(section);
            //     this.reloadTree();
            // }
        });
        popupMenu.add(createSectionMenuItem);
        return popupMenu;
    }

    private JPopupMenu createPopupMenuForBuildingActions() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteBuildingMenuItem = new JMenuItem("Delete Building");
        deleteBuildingMenuItem.addActionListener(e -> {
            // Action à effectuer lorsque l'élément du menu est sélectionné
            // DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
            // if (selectedNode != null) {
            //     this.buildingsNodes.entrySet().stream().filter(set -> set.getValue().equals(selectedNode))
            //                 .findFirst().ifPresent(pair -> {
            //                     final Building buildingToDestroy = pair.getKey();
            //                     cadastralPlan.getSections().forEach(section -> {
            //                         section.getParcels().forEach(parcel -> {
            //                             parcel.land().getBuildings().forEach(building -> {
            //                                 if (building == buildingToDestroy) {
            //                                     parcel.land().removeBuilding(buildingToDestroy);
            //                                 }
            //                             });
            //                         });
            //                     });
            //                 });
            //     this.reloadTree();
            // }
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
                // DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
                // if (selectedNode != null) {
                //     this.sectionsNodes.entrySet().stream().filter(set -> set.getValue().equals(selectedNode))
                //             .findFirst().ifPresent(pair -> {
                //                 cadastralPlan.removeSection(pair.getKey());
                //             });
                //     this.reloadTree();
                // }
            });
            popupMenu.add(deleteSectionMenuItem);
        }
        JMenuItem createParcelMenuItem = new JMenuItem("Create new Parcel");
        createParcelMenuItem.addActionListener(e -> {
            // Action à effectuer lorsque l'élément du menu est sélectionné
            // DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
            // if (selectedNode != null) {
            //     this.sectionsNodes.entrySet().stream().filter(set -> set.getValue().equals(selectedNode)).findFirst()
            //             .ifPresent(pair -> {
            //                 final Section section = pair.getKey();
            //                 final Parcel parcel = new Parcel(section.getParcels().size(), new Land());
            //                 section.addParcel(parcel);
            //             });
            //     this.reloadTree();
            // }
        });
        popupMenu.add(createParcelMenuItem);
        return popupMenu;
    }

    private JPopupMenu createPopupMenuForParcelsActions() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteParcelMenuItem = new JMenuItem("Delete Parcel");
        deleteParcelMenuItem.addActionListener(e -> {
            // Action à effectuer lorsque l'élément du menu est sélectionné
            // DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
            // if (selectedNode != null) {
            //     this.parcelsNodes.entrySet().stream().filter(set -> set.getValue().equals(selectedNode)).findFirst()
            //             .ifPresent(pair -> {
            //                 final Parcel parcel = pair.getKey();
            //                 this.sectionsNodes.keySet().stream().filter(section -> section.has(parcel)).findFirst()
            //                         .ifPresent(section -> {
            //                             section.removeParcel(parcel);
            //                         });
            //             });
            //     this.reloadTree();
            // }
        });
        JMenuItem addBuildingMenuItem = new JMenuItem("Add Building");
        addBuildingMenuItem.addActionListener(e -> {
            // Action à effectuer lorsque l'élément du menu est sélectionné
            // DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
            // if (selectedNode != null) {
            //     this.parcelsNodes.entrySet().stream().filter(set -> set.getValue().equals(selectedNode)).findFirst()
            //             .ifPresent(pair -> {
            //                 final Parcel parcel = pair.getKey();
            //                 final Building building = new Building();
            //                 parcel.land().addBuilding(building);
            //             });
            //     this.reloadTree();
            // }
        });
        popupMenu.add(deleteParcelMenuItem);
        popupMenu.add(addBuildingMenuItem);
        return popupMenu;
    }



    public final void setOnCadastreCreatedEventLister(Consumer<CadastralPlan> onCadastreCreatedEventLister) {
        this.layersPanel.setOnCadastreCreatedEventLister(onCadastreCreatedEventLister);
    }
}
