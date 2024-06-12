package io.nirahtech.petvet.core.util.identifier;

import java.io.Serializable;
import java.util.UUID;

public final class Identifier implements Serializable {
    private UUID id;

    public Identifier(final UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public static final Identifier generate() {
        return new Identifier(UUID.randomUUID());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Identifier other = (Identifier) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
}
