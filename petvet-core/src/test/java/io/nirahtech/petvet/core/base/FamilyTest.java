package io.nirahtech.petvet.core.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class FamilyTest {


    @Test
    @Order(1)
    public final void testAdd() {
        final Human me = new Human("Test", "TEST");
        final Family family = Family.getInstance();
        family.add(me);
        assertTrue(family.contains(me));
        assertEquals(1, family.listAll().count());
    }

}
