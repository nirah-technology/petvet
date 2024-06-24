package io.nirahtech.petvet.geopulsetracker.ui;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SpritesGroup {
    private final Set<Sprite> sprites;

    public SpritesGroup() {
        this.sprites = new HashSet<>();
    }

    public final void addSprite(final Sprite sprite) {
        if (Objects.nonNull(sprite)) {
            this.sprites.add(sprite);
        }
    }

    public final void clear() {
        this.sprites.clear();
    }

    public final boolean contains(final Sprite sprite) {
        boolean isInside = false;
        if (Objects.nonNull(sprite)) {
            isInside = this.sprites.contains(sprite);
        }
        return isInside;
    }

    public final void removeSprite(final Sprite sprite) {
        if (Objects.nonNull(sprite)) {
            this.sprites.remove(sprite);
        }
    }
}
