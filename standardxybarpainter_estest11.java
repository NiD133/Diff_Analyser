package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link StandardXYBarPainter} class, focusing on the equals() method.
 */
public class StandardXYBarPainterTest {

    /**
     * Verifies the reflexive property of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void equals_onSameInstance_returnsTrue() {
        // Arrange
        StandardXYBarPainter painter = new StandardXYBarPainter();

        // Act & Assert
        assertTrue("An instance of StandardXYBarPainter should be equal to itself.",
                painter.equals(painter));
    }
}