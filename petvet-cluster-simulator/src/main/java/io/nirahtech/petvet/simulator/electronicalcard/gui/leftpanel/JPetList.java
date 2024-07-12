package io.nirahtech.petvet.simulator.electronicalcard.gui.leftpanel;

import java.util.Optional;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import io.nirahtech.petvet.simulator.electronicalcard.Pet;

public final class JPetList extends JList<Pet> {
    
    private final JPetListModel model;
    

    public JPetList() {
        super();
        this.model = new JPetListModel();
        this.setModel(this.model);
    }

    public Optional<Pet> getSelection() {
        return this.model.getSelection();
    }

    private final class JPetListModel extends DefaultListModel<Pet> {
        
        private Optional<Pet> getSelection() {
            return Optional.empty();
        }
    
    }
}
