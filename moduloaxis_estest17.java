package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ModuloAxis} class.
 */
// The original test class name is kept for context, but in a real scenario,
// it would be renamed to something like ModuloAxisTest.
public class ModuloAxis_ESTestTest17 extends ModuloAxis_ESTest_scaffolding {

    /**
     * Verifies that valueToJava2D returns NaN when the display range has zero length.
     * This occurs when the start and end of the display range map to the same value
     * within the axis's fixed range.
     */
    @Test
    public void valueToJava2D_withZeroLengthDisplayRange_shouldReturnNaN() {
        // Arrange
        // A ModuloAxis maps all values to a fixed range. Here, we use [0.0, 1.0].
        Range fixedRange = new Range(0.0, 1.0);
        ModuloAxis axis = new ModuloAxis("Test Modulo Axis", fixedRange);

        // Set a display range where both start (500.0) and end (2.0) map to 0.0
        // after the modulo operation against the fixed range's length (1.0).
        // This results in an effective display range of [0.0, 0.0], which has zero length.
        axis.setDisplayRange(500.0, 2.0);

        // Define a standard plotting area and edge for the conversion.
        Rectangle2D plotArea = new Rectangle2D.Double(0.0, 0.0, 100.0, 50.0);
        RectangleEdge edge = RectangleEdge.BOTTOM;
        double valueToConvert = 75.0;

        // Act
        // Attempt to convert a data value to a 2D coordinate.
        double coordinate = axis.valueToJava2D(valueToConvert, plotArea, edge);

        // Assert
        // A conversion is mathematically undefined for a zero-length axis range,
        // so the expected result is Not-a-Number (NaN).
        assertEquals(Double.NaN, coordinate, 0.0);
    }
}