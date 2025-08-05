package org.jfree.chart.renderer.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link StandardBarPainter} class.
 */
public class StandardBarPainterTest {

    /**
     * Tests that two newly created {@link StandardBarPainter} objects are equal.
     */
    @Test
    public void testEquals() {
        StandardBarPainter painter1 = new StandardBarPainter();
        StandardBarPainter painter2 = new StandardBarPainter();
        assertEquals(painter1, painter2, "Two new StandardBarPainter instances should be equal.");
    }

    /**
     * Tests that two equal {@link StandardBarPainter} objects have the same hash code.
     */
    @Test
    public void testHashCode() {
        StandardBarPainter painter1 = new StandardBarPainter();
        StandardBarPainter painter2 = new StandardBarPainter();
        assertEquals(painter1, painter2, "Two new StandardBarPainter instances should be equal.");
        assertEquals(painter1.hashCode(), painter2.hashCode(), "Equal StandardBarPainter instances should have the same hash code.");
    }

    /**
     * Tests that {@link StandardBarPainter} does not implement cloning interfaces.
     * Since instances are immutable, cloning is not required.
     */
    @Test
    public void testCloning() {
        StandardBarPainter painter = new StandardBarPainter();
        assertFalse(painter instanceof Cloneable, "StandardBarPainter should not be cloneable.");
        assertFalse(painter instanceof PublicCloneable, "StandardBarPainter should not implement PublicCloneable.");
    }

    /**
     * Tests serialization and deserialization of a {@link StandardBarPainter} instance.
     * The deserialized instance should be equal to the original.
     */
    @Test
    public void testSerialization() {
        StandardBarPainter originalPainter = new StandardBarPainter();
        StandardBarPainter deserializedPainter = TestUtils.serialised(originalPainter);
        assertEquals(originalPainter, deserializedPainter, "Deserialized StandardBarPainter should be equal to the original.");
    }
}