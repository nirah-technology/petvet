package io.nirahtech.petvet.installer.ui;

import java.awt.Component;

public class Step {
    private final int value;
    private final String description;
    private final Component graphicComponent;
    private boolean isSelected;
    

    /**
     * @param value
     * @param graphicComponent
     * @param isSelected
     * @param previousNeighbor
     * @param nextNeighbor
     */
    public Step(int value, String description, Component graphicComponent, boolean isSelected) {
        this.value = value;
        this.description = description;
        this.graphicComponent = graphicComponent;
        this.isSelected = isSelected;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the graphicComponent
     */
    public Component getGraphicComponent() {
        return graphicComponent;
    }

    /**
     * @return the isSelected
     */
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        this.graphicComponent.setVisible(isSelected);
    }

    public void select() {
        this.setSelected(true);
    }
    public void unselect() {
        this.setSelected(false);
    }
}
