package io.nirahtech.petvet.core.base;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Collections;
import java.util.Collection;
import java.util.Set;

public final class PhotoBook implements Serializable {
    private static final int MAX_PICTURES = 100;

    public final Set<File> pictures;
    private final Pet pet;

    public PhotoBook(final Pet pet) {
        this.pet = pet;
        this.pictures = new HashSet<>();
    }

    public Pet getPet() {
        return this.pet;
    }

    public Collection<File> getPictures() {
        return Collections.unmodifiableCollection(this.pictures);
    }

    public void clear() {
        this.pictures.clear();
    }

    public boolean contains(final File file) {
        return this.pictures.contains(file);
    }

    public final void add(final File file) {
        if (!this.contains(file)) {
            if (this.pictures.size() < MAX_PICTURES) {
                this.pictures.add(file);
            }
        }
    }
    public final void remove(final File file) {
        if (this.contains(file)) {
            this.pictures.remove(file);
        }
    }
}
