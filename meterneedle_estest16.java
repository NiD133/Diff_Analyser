package org.jfree.chart.plot.compass;

import org.junit.Test;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Unit tests for the {@link MeterNeedle} class, focusing on its drawing behavior.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the draw() method throws a NullPointerException if the Graphics2D
     * context is null. This is a critical check to ensure the method fails fast
     * with invalid arguments, preventing more obscure errors downstream.
     */
    @Test(expected = NullPointerException.class)
    public void draw_withNullGraphics2D_shouldThrowNullPointerException() {
        // Arrange: Set up the necessary objects for the draw method call.
        // We use MiddlePinNeedle as a concrete implementation of MeterNeedle.
        MeterNeedle needle = new MiddlePinNeedle();
        Rectangle2D plotArea = new Rectangle2D.Float(0, 0, 100, 100);
        Point2D rotationPoint = new Point2D.Float(50, 50);
        double angle = 45.0; // A representative angle value.

        // Act: Call the method under test with a null Graphics2D object.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        needle.draw(null, plotArea, rotationPoint, angle);
    }
}

/**
 * A concrete implementation of MeterNeedle for testing purposes,
 * as MeterNeedle is an abstract class. This class is derived from
 * the original test's usage.
 */
class MiddlePinNeedle extends MeterNeedle {
    @Override
    protected void drawNeedle(Graphics2D g2, Rectangle2D plotArea, Point2D rotate, double angle) {
        // A minimal implementation is sufficient for this test case.
    }
}