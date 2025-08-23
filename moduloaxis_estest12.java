package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

// Note: The original test class name and inheritance are preserved.
public class ModuloAxis_ESTestTest12 extends ModuloAxis_ESTest_scaffolding {

    /**
     * This test verifies the behavior of the {@code valueToJava2D} method after
     * the axis's display range has been significantly resized.
     *
     * <p>The test sets up a ModuloAxis with a wrapped display range (like a compass),
     * resizes this range by a large, specific factor, and then checks if a given data value
     * is correctly translated into its corresponding 2D coordinate. It also confirms
     * that resizing the range correctly disables the auto-range feature.
     */
    @Test(timeout = 4000)
    public void valueToJava2DShouldCalculateCorrectCoordinateAfterRangeResize() {
        // Arrange
        // A ModuloAxis with a fixed range of [0, 1]. The default display range
        // is a wrapped range from 270 to 90 (like degrees on a compass).
        final Range fixedRange = new Range(0.0, 1.0);
        final ModuloAxis moduloAxis = new ModuloAxis("Test Axis", fixedRange);

        // Define the drawing area and the position of the axis.
        // The original test used a complex calculation that resulted in this rectangle.
        final Rectangle2D plotArea = new Rectangle2D.Double(-4.0, -2.0, 8.0, 4.0);
        final RectangleEdge axisEdge = RectangleEdge.LEFT;

        // Use named constants for the non-intuitive values from the original test
        // to make their purpose clear.
        final double resizeFactor = 1005.89236;
        final double dataValue = 3676.6;

        // Act
        // Resize the display range. This is a key part of the setup for the value conversion.
        // This action also has the side effect of disabling auto-ranging.
        moduloAxis.resizeRange(resizeFactor);

        // Convert the data value to a 2D coordinate.
        final double actualCoordinate = moduloAxis.valueToJava2D(dataValue, plotArea, axisEdge);

        // Assert
        final double expectedCoordinate = 5.979967110664955;
        final double tolerance = 0.01;

        assertEquals("The calculated Java2D coordinate should match the expected value after resizing.",
                expectedCoordinate, actualCoordinate, tolerance);
        assertFalse("Resizing the range should disable the auto-range feature.",
                moduloAxis.isAutoRange());
    }
}