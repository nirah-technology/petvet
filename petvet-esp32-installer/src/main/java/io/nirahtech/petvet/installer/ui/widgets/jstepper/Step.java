package io.nirahtech.petvet.installer.ui.widgets.jstepper;

import java.util.Objects;

import javax.swing.JPanel;

class Step {
    private final int order;
    private final String title;
    private final String description;
    private final JPanel panel;

    private boolean isSelected;
    private boolean isCompleted;

    private Runnable onSelected = null;
    private Runnable onDeselected = null;

    private Runnable onCompleted = null;


    /**
     * @param order
     * @param title
     * @param description
     * @param panel
     */
    Step(int order, String title, String description, JPanel panel) {
        this.order = order;
        this.title = title;
        this.description = description;
        this.panel = panel;
        this.isCompleted = false;
        this.isSelected = false;
    }

    /**
     * @return the order
     */
    public int getOrder() {
        return order;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the panel
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * @return the isSelected
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * @param isCompleted the isCompleted to set
     */
    private void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
        if (this.isCompleted) {
            if (Objects.nonNull(this.onCompleted)) {
                this.onCompleted.run();
            }
        }
    }


    public void uncomplete() {
        this.setCompleted(false);
    }    

    public void complete() {
        this.setCompleted(true);
    }


    /**
     * @return the isCompleted
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        this.panel.setVisible(isSelected);
        if (this.isSelected) {
            if (Objects.nonNull(this.onSelected)) {
                this.onSelected.run();
            }
        } else {
            if (Objects.nonNull(this.onDeselected)) {
                this.onDeselected.run();
            }
        }
    }

    public void select() {
        this.setSelected(true);
    }
    public void unselect() {
        this.setSelected(false);
    }

    /**
     * @param onCompleted the onCompleted to set
     */
    public void setOnCompleted(Runnable onCompleted) {
        this.onCompleted = onCompleted;
    }
    /**
     * @param onDeselected the onDeselected to set
     */
    public void setOnDeselected(Runnable onDeselected) {
        this.onDeselected = onDeselected;
    }
    /**
     * @param onSelected the onSelected to set
     */
    public void setOnSelected(Runnable onSelected) {
        this.onSelected = onSelected;
    }
}
