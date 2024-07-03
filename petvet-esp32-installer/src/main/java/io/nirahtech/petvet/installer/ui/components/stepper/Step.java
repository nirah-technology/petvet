package io.nirahtech.petvet.installer.ui.components.stepper;

import java.awt.Component;
import java.util.Objects;
import java.util.function.Consumer;

public final class Step {

    private final int value;
    private final String description;
    private final Component graphicComponent;
    private boolean isSelected;

    private Consumer<Step> onSelectedEventHandler;
    private Consumer<Step> onUnselectedEventHandler;
    

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
        this.graphicComponent.setVisible(this.isSelected);
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
    public final boolean isSelected() {
        return isSelected;
    }

    private final void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        this.graphicComponent.setVisible(isSelected);
        if (this.isSelected) {
            if (Objects.nonNull(this.onSelectedEventHandler)) {
                this.onSelectedEventHandler.accept(this);
            }
        } else {
            if (Objects.nonNull(this.onUnselectedEventHandler)) {
                this.onUnselectedEventHandler.accept(this);
            }
        }
    }

    public final void select() {
        this.setSelected(true);
    }
    public final void unselect() {
        this.setSelected(false);
    }

    void setOnSelectedEventHandler(Consumer<Step> onSelectedEventHandler) {
        this.onSelectedEventHandler = onSelectedEventHandler;
    }
    void setOnUnselectedEventHandler(Consumer<Step> onUnselectedEventHandler) {
        this.onUnselectedEventHandler = onUnselectedEventHandler;
    }
}
