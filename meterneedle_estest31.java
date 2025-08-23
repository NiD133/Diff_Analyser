package org.jfree.chart.plot.compass;

import org.junit.Test;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Contains tests for the {@link MeterNeedle} class, focusing on its interaction
 * with drawing contexts.
 */
public class MeterNeedle_ESTestTest31 { // Note: Class name kept as per original for context.

    /**
     * Verifies that the defaultDisplay method throws a NullPointerException
     * when the Graphics2D argument is null, as this is a required parameter.
     */
    @Test(expected = NullPointerException.class)
    public void defaultDisplay_shouldThrowNullPointerException_whenGraphicsIsNull() {
        // Arrange: Create a concrete needle instance and a dummy shape.
        // WindNeedle is a simple, concrete subclass of the abstract MeterNeedle.
        MeterNeedle needle = new WindNeedle();
        Rectangle2D.Double dummyShape = new Rectangle2D.Double(0, 0, 10, 10);

        // Act & Assert: Call the method with a null Graphics2D context.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        needle.defaultDisplay(null, dummyShape);
    }
}