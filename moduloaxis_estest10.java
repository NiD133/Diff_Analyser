package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * This test case was improved for understandability. The original was an
 * auto-generated test from EvoSuite.
 */
public class ModuloAxisTest {

    /**
     * Tests that valueToJava2D returns the origin of the axis when the plot
     * area has zero width. The final coordinate calculation is proportional to
     * the plot area's width, so a width of zero should result in a coordinate of zero.
     */
    @Test
    public void valueToJava2D_withZeroWidthArea_shouldReturnMinCoordinate() {
        // Arrange
        // A ModuloAxis with a fixed data range of [0.0, 1.0].
        Range fixedRange = new Range(0.0, 1.0);
        ModuloAxis axis = new ModuloAxis("Test Modulo Axis", fixedRange);
        axis.setInverted(true);

        // Resizing the range defines the axis's scale and sets auto-range to false.
        // The specific percentage (499.311...) is arbitrary for this test's logic.
        axis.resizeRange(499.3112711);

        // Use a plot area with zero width and height. This is the key condition being tested.
        Rectangle2D plotArea = new Rectangle2D.Double();
        RectangleEdge axisEdge = RectangleEdge.BOTTOM;

        // An arbitrary value to be converted to a 2D coordinate.
        double value = -2588.490989243;

        // Act
        // Convert the data value to its corresponding Java2D coordinate.
        double java2dCoordinate = axis.valueToJava2D(value, plotArea, axisEdge);

        // Assert
        // The resizeRange method should disable auto-ranging.
        assertFalse("Auto-range should be false after resizing", axis.isAutoRange());

        // For a horizontal axis (BOTTOM edge) in a zero-width plot area, the
        // calculated coordinate should be the minimum X coordinate of the area (0.0).
        // This is because the final calculation is proportional to the area's width.
        assertEquals("Coordinate for a zero-width area should be 0.0", 0.0, java2dCoordinate, 0.01);
    }
}