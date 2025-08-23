package org.jfree.chart.plot.compass;

import org.junit.Test;

/**
 * Tests for the {@link MeterNeedle} class, specifically focusing on its
 * handling of null arguments.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the draw() method throws a NullPointerException when passed
     * a null Graphics2D context and a null plot area.
     *
     * The method is expected to fail fast when its required parameters are null,
     * as it cannot perform its drawing calculations without them.
     */
    @Test(expected = NullPointerException.class)
    public void drawWithNullGraphicsAndPlotAreaShouldThrowNullPointerException() {
        // Arrange: Create a concrete instance of MeterNeedle.
        // WindNeedle is a suitable concrete subclass for this test.
        WindNeedle needle = new WindNeedle();
        final double arbitraryAngle = 90.0;

        // Act & Assert: Call the draw method with null for the required
        // graphical context and plot area. This is expected to throw a
        // NullPointerException.
        needle.draw(null, null, arbitraryAngle);
    }
}