package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

/**
 * Unit tests for the protected methods of the {@link MeterNeedle} class.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the {@code defaultDisplay} method throws a {@code NullPointerException}
     * when invoked with a null {@code Graphics2D} object. This ensures the method
     * correctly handles invalid rendering contexts.
     */
    @Test
    public void defaultDisplayWithNullGraphicsShouldThrowNullPointerException() {
        // Arrange: Create a concrete MeterNeedle instance and a shape to be drawn.
        // WindNeedle is used here as a concrete implementation of the abstract MeterNeedle.
        WindNeedle needle = new WindNeedle();
        Shape testShape = new Rectangle();

        // Act & Assert: Verify that calling defaultDisplay with a null Graphics2D
        // context throws the expected NullPointerException.
        assertThrows(NullPointerException.class, () -> {
            needle.defaultDisplay(null, testShape);
        });
    }
}