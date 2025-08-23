package org.jfree.chart.renderer.category;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link StandardBarPainter} class.
 */
public class StandardBarPainterTest {

    /**
     * Verifies that the hashCode() method is consistent for different instances.
     * <p>
     * The {@link StandardBarPainter} class is stateless. According to the
     * {@code Object.hashCode()} contract, two objects that are equal must have
     * the same hash code. Therefore, any two instances of StandardBarPainter
     * should be considered equal and produce the same hash code.
     */
    @Test
    public void hashCode_shouldBeConsistentAcrossInstances() {
        // Arrange: Create two separate instances of the stateless painter.
        StandardBarPainter painter1 = new StandardBarPainter();
        StandardBarPainter painter2 = new StandardBarPainter();

        // Act & Assert: The hash codes should be identical.
        assertEquals("Hash codes of two stateless StandardBarPainter instances must be equal.",
                painter1.hashCode(), painter2.hashCode());
    }
}