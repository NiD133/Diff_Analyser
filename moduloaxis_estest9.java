package org.jfree.chart.axis;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the ModuloAxis class.
 * This specific test case focuses on the behavior of the valueToJava2D method
 * under specific boundary conditions.
 */
public class ModuloAxis_ESTestTest9 { // In a real-world scenario, this class would be named ModuloAxisTest

    /**
     * Verifies that valueToJava2D returns the origin (0.0) for an inverted axis
     * when the drawing area has zero size. This tests a boundary condition where
     * the plotting area is effectively a single point.
     */
    @Test
    public void valueToJava2D_withInvertedAxisAndZeroSizedArea_shouldReturnOrigin() {
        // Arrange: Set up a ModuloAxis and a zero-sized drawing area.
        Range axisRange = new Range(0.0, 360.0);
        ModuloAxis moduloAxis = new ModuloAxis("Angle", axisRange);
        moduloAxis.setInverted(true);

        // A drawing area with zero width and height represents a boundary condition.
        Rectangle2D.Double zeroSizedDrawingArea = new Rectangle2D.Double();
        RectangleEdge edge = RectangleEdge.TOP; // A horizontal axis edge.

        // The data value to convert. The specific value is not critical for a zero-sized area.
        double dataValue = 45.0;

        // Act: Convert the data value to its Java2D coordinate.
        double java2dCoordinate = moduloAxis.valueToJava2D(dataValue, zeroSizedDrawingArea, edge);

        // Assert: The result should be the origin of the drawing area (0.0).
        // For a zero-width area, any value should map to the starting x-coordinate, which is 0.0.
        assertEquals(0.0, java2dCoordinate, 0.001);
    }
}