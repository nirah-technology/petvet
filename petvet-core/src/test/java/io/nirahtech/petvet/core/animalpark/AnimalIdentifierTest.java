package io.nirahtech.petvet.core.animalpark;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class AnimalIdentifierTest {

    @Test
    @Order(1)
    public void testOf() {
        final AnimalIdentifier identifier = AnimalIdentifier.of(0L);
        assertNotNull(identifier);
        assertEquals(0L, identifier.getId());
    }


    @Test
    @Order(2)
    public void testCreate() {
        final AnimalIdentifier identifier = AnimalIdentifier.generate();
        System.out.println(identifier.getId());
        assertNotNull(identifier);
        assertTrue(identifier.getId() > 0);
    }
    
}
