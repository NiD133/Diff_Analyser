package org.jfree.chart.renderer.category;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link StandardBarPainter} class.
 */
public class StandardBarPainterTest {

    /**
     * Verifies that an instance of StandardBarPainter is equal to itself.
     * This test checks for the reflexive property of the equals() contract.
     */
    @Test
    public void equals_onSameInstance_returnsTrue() {
        // Arrange
        StandardBarPainter painter = new StandardBarPainter();

        // Act & Assert
        // An object must always be equal to itself.
        assertTrue("A StandardBarPainter instance should be equal to itself.",
                painter.equals(painter));
    }
}