package io.nirahtech.petvet.simulator.cadastre.gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import io.nirahtech.petvet.simulator.cadastre.domain.Building;
import io.nirahtech.petvet.simulator.cadastre.domain.CadastralPlan;
import io.nirahtech.petvet.simulator.cadastre.domain.Land;
import io.nirahtech.petvet.simulator.cadastre.domain.Parcel;
import io.nirahtech.petvet.simulator.cadastre.domain.Section;
import io.nirahtech.petvet.simulator.cadastre.domain.Surface;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.jcadastreplanpanel.JCadastrPlanGroupNodePanel;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.jcadastreplanpanel.JCadastrePlanPanel;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers.BuildingLayer;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers.LandLayer;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers.Layer;

public class PetVetLandWindow extends JFrame {

    private final Map<Surface, Layer> layersBySurface = new HashMap<>();

    private final CadastralPlan cadastralPlan;
    private final JDrawerPanel drawerPanel;
    private final JCadastrePlanPanel cadastrePlanPanel;
    private final JButton createButton;


    public PetVetLandWindow() {
        super("PetVet : Land Simulator");
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());

        this.cadastralPlan = new CadastralPlan();

        this.drawerPanel = new JDrawerPanel(this.cadastralPlan);
        
        this.add(this.drawerPanel, BorderLayout.CENTER);

        this.cadastrePlanPanel = new JCadastrePlanPanel(cadastralPlan);
        this.cadastrePlanPanel.addOnLeftClickEventListener((node, event) -> {
            this.cadastrePlanPanel.select(node);
            Layer layer = this.layersBySurface.getOrDefault(node.getSurface(), null);
            this.drawerPanel.setSelectedLayer(layer);
        });

        this.cadastrePlanPanel.addOnRightClickEventListener((node, event) -> {
            JPopupMenu popupMenu = null;
            if (node.getSurface() instanceof CadastralPlan) {
                popupMenu = createPopupMenuForCadastreActions(node);
            } else if (node.getSurface() instanceof Section) {
                popupMenu = createPopupMenuForSectionActions(node);
            } else if (node.getSurface() instanceof Parcel) {
                popupMenu = createPopupMenuForParcelsActions(node);
            } else if (node.getSurface() instanceof Building) {
                popupMenu = createPopupMenuForBuildingActions(node);
            }
            if (Objects.nonNull(popupMenu)) {
                popupMenu.show(node, event.getX(), event.getY());
            }
        });
        
        final JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        this.createButton = new JButton("Create");

        rightPanel.add(new JScrollPane(this.cadastrePlanPanel), BorderLayout.CENTER);
        rightPanel.add(this.createButton, BorderLayout.SOUTH);
        this.add(rightPanel, BorderLayout.EAST);

    }

    private JPopupMenu createPopupMenuForCadastreActions(JCadastrPlanGroupNodePanel node) {
        final JPopupMenu popupMenu = new JPopupMenu();
        final JMenuItem createSectionMenuItem = new JMenuItem("Create new Section");
        createSectionMenuItem.addActionListener(e -> {
            if (node.getSurface() instanceof CadastralPlan) {
                // // Action à effectuer lorsque l'élément du menu est sélectionné
                final Land land = new Land();
                this.layersBySurface.put(land, new LandLayer(1));
                final Parcel parcel = new Parcel(0, land);
                final Section section = new Section(UUID.randomUUID().toString().split("-")[0], parcel);
                this.cadastralPlan.addSection(section);
                this.cadastrePlanPanel.redraw();
            }
        });
        popupMenu.add(createSectionMenuItem);
        return popupMenu;
    }

    private JPopupMenu createPopupMenuForBuildingActions(JCadastrPlanGroupNodePanel node) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteBuildingMenuItem = new JMenuItem("Delete Building");
        deleteBuildingMenuItem.addActionListener(e -> {
            if (node.getSurface() instanceof Building) {
                AtomicReference<Land> landToManage = new AtomicReference<>();
                final Building buildingToDelete = (Building) node.getSurface();
                this.cadastralPlan.getSections().forEach(section -> {
                    section.getParcels().forEach(parcel -> {
                        parcel.land().getBuildings().forEach(building -> {
                            if (building == buildingToDelete) {
                                landToManage.set(parcel.land());
                            }
                        });
                    });
                });
                if (Objects.nonNull(landToManage.get())) {
                    landToManage.get().removeBuilding(buildingToDelete);
                    this.cadastrePlanPanel.redraw();
                }
            }
        });
        popupMenu.add(deleteBuildingMenuItem);
        return popupMenu;
    }

    private JPopupMenu createPopupMenuForSectionActions(JCadastrPlanGroupNodePanel node) {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem deleteSectionMenuItem = new JMenuItem("Delete Section");
        deleteSectionMenuItem.addActionListener(e -> {
            if (node.getSurface() instanceof Section) {
                cadastralPlan.removeSection((Section) node.getSurface());
                this.cadastrePlanPanel.redraw();
            }
        });
        popupMenu.add(deleteSectionMenuItem);

        JMenuItem createParcelMenuItem = new JMenuItem("Create new Parcel");
        createParcelMenuItem.addActionListener(e -> {
            if (node.getSurface() instanceof Section) {
                Section section = (Section) node.getSurface();
                Parcel parcel = section.createNewParcel();
                this.layersBySurface.put(parcel.land(), new LandLayer(1));
                this.cadastrePlanPanel.redraw();
            }
        });
        popupMenu.add(createParcelMenuItem);
        return popupMenu;
    }

    private JPopupMenu createPopupMenuForParcelsActions(JCadastrPlanGroupNodePanel node) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteParcelMenuItem = new JMenuItem("Delete Parcel");
        AtomicReference<Section> sectionToManage = new AtomicReference<>();
        deleteParcelMenuItem.addActionListener(e -> {
            if (node.getSurface() instanceof Parcel) {
                this.cadastralPlan.getSections().forEach(section -> {
                    section.getParcels().forEach(parcel -> {
                        if (parcel == node.getSurface()) {
                            sectionToManage.set(section);
                        }
                    });
                });
                if (Objects.nonNull(sectionToManage.get())) {
                    sectionToManage.get().removeParcel((Parcel) node.getSurface());
                    this.cadastrePlanPanel.redraw();
                }
            }
        });
        JMenuItem addBuildingMenuItem = new JMenuItem("Add Building");
        addBuildingMenuItem.addActionListener(e -> {
            if (node.getSurface() instanceof Parcel) {
                Parcel parcel = (Parcel) node.getSurface();
                final Building building = new Building();
                this.layersBySurface.put(building, new BuildingLayer(2));
                parcel.land().addBuilding(building);
                this.cadastrePlanPanel.redraw();
            }
        });
        popupMenu.add(deleteParcelMenuItem);
        popupMenu.add(addBuildingMenuItem);
        return popupMenu;
    }

    public final void setOnCadastreCreatedEventLister(Consumer<CadastralPlan> onCadastreCreatedEventLister) {
        this.createButton.addActionListener(event -> {
            if (Objects.nonNull(onCadastreCreatedEventLister)) {
                onCadastreCreatedEventLister.accept(this.cadastralPlan);
            }
        });
    }
}
