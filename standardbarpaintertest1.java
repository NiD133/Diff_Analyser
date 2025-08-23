package org.jfree.chart.renderer.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link StandardBarPainter} class, focusing on the correctness
 * of its equals() and hashCode() implementations.
 */
class StandardBarPainterTest {

    /**
     * Verifies that two distinct instances of StandardBarPainter are considered equal,
     * as the class is stateless. This also tests for symmetry.
     */
    @Test
    void equals_withTwoSeparateInstances_shouldReturnTrue() {
        StandardBarPainter painter1 = new StandardBarPainter();
        StandardBarPainter painter2 = new StandardBarPainter();

        // Test for equality and symmetry
        assertTrue(painter1.equals(painter2), "Two separate instances should be equal.");
        assertTrue(painter2.equals(painter1), "Equality should be symmetric.");
    }

    /**
     * Verifies that an object is equal to itself (reflexivity).
     */
    @Test
    void equals_withSameInstance_shouldReturnTrue() {
        StandardBarPainter painter = new StandardBarPainter();
        assertTrue(painter.equals(painter), "An instance should be equal to itself.");
    }

    /**
     * Verifies that a StandardBarPainter instance is not equal to null.
     */
    @Test
    void equals_withNull_shouldReturnFalse() {
        StandardBarPainter painter = new StandardBarPainter();
        assertFalse(painter.equals(null), "An instance should not be equal to null.");
    }

    /**
     * Verifies that a StandardBarPainter instance is not equal to an object of a
     * different type.
     */
    @Test
    void equals_withDifferentObjectType_shouldReturnFalse() {
        StandardBarPainter painter = new StandardBarPainter();
        assertFalse(painter.equals("A String object"), "An instance should not be equal to an object of a different type.");
    }

    /**
     * Verifies that two equal StandardBarPainter objects have the same hash code,
     * fulfilling the hashCode() contract.
     */
    @Test
    void hashCode_forEqualObjects_shouldBeEqual() {
        StandardBarPainter painter1 = new StandardBarPainter();
        StandardBarPainter painter2 = new StandardBarPainter();

        // Precondition: ensure the objects are equal before testing hash codes.
        assertEquals(painter1, painter2, "Precondition failed: objects should be equal.");

        assertEquals(painter1.hashCode(), painter2.hashCode(), "Equal objects must have the same hash code.");
    }
}