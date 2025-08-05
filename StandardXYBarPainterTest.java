package org.jfree.chart.renderer.xy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.jfree.chart.TestUtils;
import org.jfree.chart.api.PublicCloneable;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link StandardXYBarPainter} class.
 */
public class StandardXYBarPainterTest {

    /**
     * Tests that two instances of {@link StandardXYBarPainter} are equal.
     */
    @Test
    public void testEquals() {
        StandardXYBarPainter painter1 = new StandardXYBarPainter();
        StandardXYBarPainter painter2 = new StandardXYBarPainter();
        assertEquals(painter1, painter2, "Two new instances should be equal");
    }

    /**
     * Tests that two equal instances of {@link StandardXYBarPainter} have the same hash code.
     */
    @Test
    public void testHashcode() {
        StandardXYBarPainter painter1 = new StandardXYBarPainter();
        StandardXYBarPainter painter2 = new StandardXYBarPainter();
        assertEquals(painter1, painter2, "Two new instances should be equal");
        int hashCode1 = painter1.hashCode();
        int hashCode2 = painter2.hashCode();
        assertEquals(hashCode1, hashCode2, "Equal instances should have the same hash code");
    }

    /**
     * Tests that {@link StandardXYBarPainter} does not implement cloning.
     * This is expected because instances of this class are immutable.
     */
    @Test
    public void testCloning() {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        assertFalse(painter instanceof Cloneable, "StandardXYBarPainter should not be cloneable");
        assertFalse(painter instanceof PublicCloneable, "StandardXYBarPainter should not be publicly cloneable");
    }

    /**
     * Tests that a serialized and deserialized instance of {@link StandardXYBarPainter} is equal to the original.
     */
    @Test
    public void testSerialization() {
        StandardXYBarPainter originalPainter = new StandardXYBarPainter();
        StandardXYBarPainter deserializedPainter = TestUtils.serialised(originalPainter);
        assertEquals(originalPainter, deserializedPainter, "Deserialized instance should be equal to the original");
    }
}