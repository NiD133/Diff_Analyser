package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import java.awt.geom.Arc2D;

/**
 * Unit tests for the {@link StandardXYBarPainter} class.
 */
public class StandardXYBarPainterTest {

    /**
     * Verifies that the equals() method returns false when a StandardXYBarPainter
     * is compared with an object of a completely different type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentTypeObject() {
        // Arrange
        StandardXYBarPainter painter = new StandardXYBarPainter();
        Object otherObject = new Arc2D.Float();

        // Act & Assert
        assertFalse("A StandardXYBarPainter instance should not be equal to an object of a different type.",
                painter.equals(otherObject));
    }
}