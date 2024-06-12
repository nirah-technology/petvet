package io.nirahtech.petvet.core.animalpark;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class BreedTest {

    @Test
    @Order(1)
    public final void testOfForCreation() {
        final String name = "Manx";
        final Breed breed = Breed.of(name);
        assertNotNull(breed);
        assertEquals(name.toLowerCase(), breed.getName().toLowerCase());
    }

    @Test
    @Order(2)
    public final void testOfForRetrieve() {
        final String originalName = "MANX";
        final Breed breedOrignial = Breed.of(originalName);
        assertNotNull(breedOrignial);
        assertEquals(originalName.toLowerCase(), breedOrignial.getName().toLowerCase());

        final String otherName = "mAnX";
        final Breed breedOther = Breed.of(otherName);
        assertNotNull(breedOther);
        assertEquals(otherName.toLowerCase(), breedOther.getName().toLowerCase());

        assertEquals(breedOrignial, breedOther);

    }

}
