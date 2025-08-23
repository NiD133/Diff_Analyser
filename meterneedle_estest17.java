package org.jfree.chart.plot.compass;

import org.junit.Test;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Tests for the drawing functionality of the {@link MeterNeedle} class and its subclasses.
 */
public class MeterNeedleDrawTest {

    /**
     * Verifies that the draw() method throws a NullPointerException
     * when the Graphics2D context is null. This is crucial for ensuring
     * the method handles invalid input gracefully.
     */
    @Test(expected = NullPointerException.class)
    public void drawWithNullGraphics2DShouldThrowException() {
        // Arrange: Create a needle instance and a valid plot area.
        // A PointerNeedle is a concrete implementation of the abstract MeterNeedle.
        MeterNeedle needle = new PointerNeedle();
        Rectangle2D plotArea = new Rectangle2D.Float(0, 0, 100, 100);

        // Act & Assert: Calling draw with a null Graphics2D object should throw a NullPointerException.
        needle.draw(null, plotArea);
    }
}